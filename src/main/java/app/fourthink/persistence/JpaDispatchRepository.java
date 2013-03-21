package app.fourthink.persistence;

import app.fourthink.model.Dispatch;
import app.fourthink.model.DispatchStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;

@Repository
@Transactional
public class JpaDispatchRepository implements DispatchRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Dispatch save(Dispatch dispatch) {
        if (dispatch.getId() == null) {
            em.persist(dispatch);
            return dispatch;
        }
        return em.merge(dispatch);
    }

    @Override
    public Dispatch findById(Long id) {
        return em.find(Dispatch.class, id);
    }

    @Override
    public List<Dispatch> findActive() {
        TypedQuery<Dispatch> q = em.createQuery(
                "select d from Dispatch d where d.status in :s order by d.createdAt desc",
                Dispatch.class);
        q.setParameter("s", Arrays.asList(DispatchStatus.REQUESTED, DispatchStatus.ASSIGNED));
        return q.getResultList();
    }

    @Override
    public List<Dispatch> findByCabId(Long cabId) {
        TypedQuery<Dispatch> q = em.createQuery(
                "select d from Dispatch d where d.assignedCab.id = :id order by d.createdAt desc",
                Dispatch.class);
        q.setParameter("id", cabId);
        return q.getResultList();
    }

    @Override
    public List<Dispatch> findByCustomerId(Long customerId) {
        TypedQuery<Dispatch> q = em.createQuery(
                "select d from Dispatch d where d.customer.id = :id order by d.createdAt desc",
                Dispatch.class);
        q.setParameter("id", customerId);
        return q.getResultList();
    }

    @Override
    public List<Dispatch> findPendingCustomerRequests() {
        TypedQuery<Dispatch> q = em.createQuery(
                "select d from Dispatch d where d.status = :s and d.customerInitiated = true " +
                        "order by d.createdAt asc", Dispatch.class);
        q.setParameter("s", DispatchStatus.REQUESTED);
        return q.getResultList();
    }
}
