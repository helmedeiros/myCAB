package app.fourthink.service;

import app.fourthink.model.Message;
import app.fourthink.model.RecipientKind;
import app.fourthink.persistence.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MessagingService {

    private MessageRepository messages;

    public MessagingService() {
    }

    @Autowired
    public MessagingService(MessageRepository messages) {
        this.messages = messages;
    }

    public Message send(RecipientKind kind, Long recipientId, String body) {
        if (body == null || body.trim().isEmpty()) {
            throw new IllegalArgumentException("message body required");
        }
        return messages.save(new Message(kind, recipientId, body.trim()));
    }

    public List<Message> drain(RecipientKind kind, Long recipientId) {
        List<Message> unread = messages.findUnread(kind, recipientId);
        for (Message m : unread) {
            m.markRead();
            messages.save(m);
        }
        return unread;
    }

    public List<Message> recent(RecipientKind kind, Long recipientId, int limit) {
        return messages.findRecent(kind, recipientId, limit);
    }
}
