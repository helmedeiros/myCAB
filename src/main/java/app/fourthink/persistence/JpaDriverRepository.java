package app.fourthink.persistence;

import app.fourthink.model.Driver;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class JpaDriverRepository implements DriverRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Driver save(Driver driver) {
        if (driver.getId() == null) {
            em.persist(driver);
            return driver;
        }
        return em.merge(driver);
    }

    @Override
    public Driver findById(Long id) {
        return em.find(Driver.class, id);
    }

    @Override
    public Driver findByEmail(String email) {
        return singleOrNull("select d from Driver d where lower(d.email) = :v", email.toLowerCase());
    }

    @Override
    public Driver findByLicenseNumber(String licenseNumber) {
        return singleOrNull("select d from Driver d where d.licenseNumber = :v", licenseNumber);
    }

    @Override
    public List<Driver> findAll() {
        return em.createQuery("select d from Driver d order by d.fullName", Driver.class)
                .getResultList();
    }

    @Override
    public long count() {
        return em.createQuery("select count(d) from Driver d", Long.class).getSingleResult();
    }

    @Override
    public List<Driver> findByCategory(app.fourthink.model.CabCategory category) {
        TypedQuery<Driver> q = em.createQuery(
                "select d from Driver d where d.preferredCategory = :c order by d.fullName",
                Driver.class);
        q.setParameter("c", category);
        return q.getResultList();
    }

    private Driver singleOrNull(String jpql, String value) {
        TypedQuery<Driver> q = em.createQuery(jpql, Driver.class);
        q.setParameter("v", value);
        try {
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
