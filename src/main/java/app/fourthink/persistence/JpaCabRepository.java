package app.fourthink.persistence;

import app.fourthink.model.Cab;
import app.fourthink.model.CabStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class JpaCabRepository implements CabRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Cab save(Cab cab) {
        if (cab.getId() == null) {
            em.persist(cab);
            return cab;
        }
        return em.merge(cab);
    }

    @Override
    public Cab findById(Long id) {
        return em.find(Cab.class, id);
    }

    @Override
    public Cab findByPlate(String plate) {
        TypedQuery<Cab> q = em.createQuery(
                "select c from Cab c where c.plate = :p", Cab.class);
        q.setParameter("p", plate);
        try {
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<Cab> findAll() {
        return em.createQuery("select c from Cab c order by c.plate", Cab.class)
                .getResultList();
    }

    @Override
    public List<Cab> findByStatus(CabStatus status) {
        TypedQuery<Cab> q = em.createQuery(
                "select c from Cab c where c.status = :s order by c.plate", Cab.class);
        q.setParameter("s", status);
        return q.getResultList();
    }

    @Override
    public void delete(Cab cab) {
        Cab attached = em.contains(cab) ? cab : em.merge(cab);
        em.remove(attached);
    }

    @Override
    public long countFreeWithLocation() {
        return em.createQuery("select count(c) from Cab c where c.status = 'FREE' and c.latitude is not null", Long.class)
                .getSingleResult();
    }

    @Override
    public long count() {
        return em.createQuery("select count(c) from Cab c", Long.class)
                .getSingleResult();
    }

    @Override
    public Cab findByFleetId(String fleetId) {
        TypedQuery<Cab> q = em.createQuery(
                "select c from Cab c where c.fleetId = :f", Cab.class);
        q.setParameter("f", fleetId);
        try {
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
