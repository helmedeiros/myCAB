package app.fourthink.controllers;

import app.fourthink.model.CabStatus;
import app.fourthink.model.Dispatch;
import app.fourthink.model.DispatchStatus;
import app.fourthink.model.Driver;
import app.fourthink.persistence.DriverRepository;
import app.fourthink.security.SessionGate;
import app.fourthink.persistence.DispatchRepository;
import app.fourthink.service.AutoDispatchService;
import app.fourthink.service.DispatchService;
import app.fourthink.service.FleetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/driver")
public class DriverHomeController {

    private final DriverRepository drivers;
    private final DispatchService dispatches;
    private final FleetService fleet;
    private final DispatchRepository dispatchRepository;
    private final AutoDispatchService autoDispatch;

    @Autowired
    public DriverHomeController(DriverRepository drivers, DispatchService dispatches,
                                 FleetService fleet,
                                 DispatchRepository dispatchRepository,
                                 AutoDispatchService autoDispatch) {
        this.drivers = drivers;
        this.dispatches = dispatches;
        this.fleet = fleet;
        this.dispatchRepository = dispatchRepository;
        this.autoDispatch = autoDispatch;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(HttpSession session, Model model) {
        if (!SessionGate.isAuthenticated(session)) {
            return "redirect:/login";
        }
        Driver driver = drivers.findById(SessionGate.driverId(session));
        model.addAttribute("driver", driver);
        if (driver.getCab() != null) {
            Long cabId = driver.getCab().getId();
            List<Dispatch> recent = dispatches.forCab(cabId);
            Dispatch current = null;
            for (Dispatch d : recent) {
                if (d.getStatus() == DispatchStatus.ASSIGNED) {
                    current = d;
                    break;
                }
            }
            model.addAttribute("currentDispatch", current);
            model.addAttribute("proposal", findProposalFor(cabId));
        }
        return "drivers/home";
    }

    private Dispatch findProposalFor(Long cabId) {
        for (Dispatch d : dispatchRepository.findPendingCustomerRequests()) {
            if (d.getStatus() == DispatchStatus.PROPOSED
                    && cabId.equals(d.getProposedCabId())) {
                return d;
            }
        }
        return null;
    }

    @RequestMapping(value = "/proposal/{id}/accept", method = RequestMethod.POST)
    public String acceptProposal(HttpSession session, @org.springframework.web.bind.annotation.PathVariable Long id) {
        if (!SessionGate.isAuthenticated(session)) {
            return "redirect:/login";
        }
        Driver driver = drivers.findById(SessionGate.driverId(session));
        autoDispatch.accept(id, driver.getCab().getId());
        return "redirect:/driver";
    }

    @RequestMapping(value = "/proposal/{id}/decline", method = RequestMethod.POST)
    public String declineProposal(HttpSession session, @org.springframework.web.bind.annotation.PathVariable Long id) {
        if (!SessionGate.isAuthenticated(session)) {
            return "redirect:/login";
        }
        Driver driver = drivers.findById(SessionGate.driverId(session));
        autoDispatch.decline(id, driver.getCab().getId());
        return "redirect:/driver";
    }

    @RequestMapping(value = "/online", method = RequestMethod.POST)
    public String goOnline(HttpSession session) {
        if (!SessionGate.isAuthenticated(session)) {
            return "redirect:/login";
        }
        Driver driver = drivers.findById(SessionGate.driverId(session));
        if (driver.getCab() != null) {
            fleet.updateStatus(driver.getCab().getId(), CabStatus.FREE);
        }
        return "redirect:/driver";
    }

    @RequestMapping(value = "/offline", method = RequestMethod.POST)
    public String goOffline(HttpSession session) {
        if (!SessionGate.isAuthenticated(session)) {
            return "redirect:/login";
        }
        Driver driver = drivers.findById(SessionGate.driverId(session));
        if (driver.getCab() != null) {
            fleet.updateStatus(driver.getCab().getId(), CabStatus.OFFLINE);
        }
        return "redirect:/driver";
    }

    @RequestMapping(value = "/complete", method = RequestMethod.POST)
    public String completeRide(HttpSession session, @RequestParam("dispatchId") Long dispatchId) {
        if (!SessionGate.isAuthenticated(session)) {
            return "redirect:/login";
        }
        dispatches.complete(dispatchId);
        return "redirect:/driver";
    }
}
