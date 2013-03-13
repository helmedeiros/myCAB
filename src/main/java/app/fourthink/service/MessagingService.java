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

    public Message send(app.fourthink.model.RecipientAddress address, String body) {
        return send(address.getKind(), address.getId(), body);
    }

    public Message send(RecipientKind kind, Long recipientId, String body) {
        return send(kind, recipientId, body, null);
    }

    public Message send(RecipientKind kind, Long recipientId, String body, Long sourceCustomerId) {
        return send(kind, recipientId, body, sourceCustomerId, null, null);
    }

    public Message send(RecipientKind kind, Long recipientId, String body,
                         Long sourceCustomerId, String pickupAddress, String destinationAddress) {
        if (body == null || body.trim().isEmpty()) {
            throw new IllegalArgumentException("message body required");
        }
        return messages.save(new Message(kind, recipientId, body.trim(),
                sourceCustomerId, pickupAddress, destinationAddress));
    }

    public boolean hasUnread(RecipientKind kind, Long recipientId) {
        return !messages.findUnread(kind, recipientId).isEmpty();
    }

    public Message broadcast(RecipientKind kind, List<Long> recipientIds, String body) {
        Message last = null;
        for (Long id : recipientIds) {
            last = send(kind, id, body);
        }
        return last;
    }

    public List<Message> drain(RecipientKind kind, Long recipientId) {
        List<Message> unread = messages.findUnread(kind, recipientId);
        for (Message m : unread) {
            m.markRead();
            messages.save(m);
        }
        return unread;
    }

    public long unreadCount(RecipientKind kind, Long recipientId) {
        return messages.findUnread(kind, recipientId).size();
    }

    public List<Message> recent(RecipientKind kind, Long recipientId, int limit) {
        return messages.findRecent(kind, recipientId, limit);
    }

    public List<Message> pendingOperatorCalls(Long operatorId) {
        return messages.findUnreadCalls(operatorId);
    }

    public Message markRead(Long messageId) {
        Message m = messages.findById(messageId);
        if (m == null) {
            throw new IllegalArgumentException("unknown message: " + messageId);
        }
        m.markRead();
        return messages.save(m);
    }
}
