package app.fourthink.controllers;

import app.fourthink.model.Message;
import app.fourthink.model.RecipientKind;
import app.fourthink.service.MessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/messages")
public class MessageApiController {

    private final MessagingService messaging;

    @Autowired
    public MessageApiController(MessagingService messaging) {
        this.messaging = messaging;
    }

    @RequestMapping(value = "/{kind}/{id}", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> drain(@PathVariable("kind") RecipientKind kind,
                                            @PathVariable("id") Long id) {
        List<Message> messages = messaging.drain(kind, id);
        List<Map<String, Object>> out = new ArrayList<Map<String, Object>>();
        for (Message m : messages) {
            app.fourthink.model.MessagePayload payload = app.fourthink.model.MessagePayload.of(m);
            Map<String, Object> entry = new HashMap<String, Object>();
            entry.put("id", payload.getId());
            entry.put("body", payload.getBody());
            entry.put("createdAt", payload.getCreatedAt());
            out.add(entry);
        }
        return out;
    }

    @RequestMapping(value = "/{kind}/{id}/unread", method = RequestMethod.GET)
    @ResponseBody
    public java.util.Map<String, Object> unread(@PathVariable("kind") RecipientKind kind,
                                                  @PathVariable("id") Long id) {
        java.util.Map<String, Object> out = new java.util.HashMap<String, Object>();
        out.put("unread", messaging.unreadCount(kind, id));
        return out;
    }

    @RequestMapping(value = "/{kind}/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> send(@PathVariable("kind") RecipientKind kind,
                                     @PathVariable("id") Long id,
                                     @RequestParam("body") String body) {
        Message m = messaging.send(kind, id, body);
        Map<String, Object> out = new HashMap<String, Object>();
        out.put("id", m.getId());
        out.put("body", m.getBody());
        return out;
    }
}
