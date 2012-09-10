package app.fourthink.persistence;

import app.fourthink.model.Message;
import app.fourthink.model.RecipientKind;

import java.util.List;

public interface MessageRepository {

    Message save(Message message);

    List<Message> findUnread(RecipientKind kind, Long recipientId);

    List<Message> findRecent(RecipientKind kind, Long recipientId, int limit);

    long count();
}
