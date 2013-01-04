package app.fourthink.persistence;

import app.fourthink.model.Cab;
import app.fourthink.model.CabStatus;

import java.util.List;

public interface CabRepository {

    Cab save(Cab cab);

    Cab findById(Long id);

    Cab findByPlate(String plate);

    List<Cab> findAll();

    List<Cab> findByStatus(CabStatus status);

    void delete(Cab cab);

    long count();

    long countFreeWithLocation();

    Cab findByFleetId(String fleetId);
}
