package app.fourthink.persistence;

import app.fourthink.model.Message;
import app.fourthink.model.RecipientKind;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class JpaMessageRepository implements MessageRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Message save(Message message) {
        if (message.getId() == null) {
            em.persist(message);
            return message;
        }
        return em.merge(message);
    }

    @Override
    public List<Message> findUnread(RecipientKind kind, Long recipientId) {
        TypedQuery<Message> q = em.createQuery(
                "select m from Message m where m.recipientKind = :k and m.recipientId = :i " +
                        "and m.read = false order by m.createdAt asc", Message.class);
        q.setParameter("k", kind);
        q.setParameter("i", recipientId);
        return q.getResultList();
    }

    @Override
    public List<Message> findRecent(RecipientKind kind, Long recipientId, int limit) {
        TypedQuery<Message> q = em.createQuery(
                "select m from Message m where m.recipientKind = :k and m.recipientId = :i " +
                        "order by m.createdAt desc", Message.class);
        q.setParameter("k", kind);
        q.setParameter("i", recipientId);
        q.setMaxResults(limit);
        return q.getResultList();
    }

    @Override
    public long count() {
        return em.createQuery("select count(m) from Message m", Long.class).getSingleResult();
    }
}
