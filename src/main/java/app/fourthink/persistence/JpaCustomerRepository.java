package app.fourthink.persistence;

import app.fourthink.model.Customer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class JpaCustomerRepository implements CustomerRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Customer save(Customer customer) {
        if (customer.getId() == null) {
            em.persist(customer);
            return customer;
        }
        return em.merge(customer);
    }

    @Override
    public Customer findById(Long id) {
        return em.find(Customer.class, id);
    }

    @Override
    public List<Customer> findAll() {
        return em.createQuery("select c from Customer c order by c.name", Customer.class)
                .getResultList();
    }

    @Override
    public List<Customer> searchByName(String fragment) {
        TypedQuery<Customer> q = em.createQuery(
                "select c from Customer c where lower(c.name) like :n order by c.name",
                Customer.class);
        q.setParameter("n", "%" + fragment.toLowerCase() + "%");
        return q.getResultList();
    }

    @Override
    public Customer findByPhone(String phone) {
        TypedQuery<Customer> q = em.createQuery(
                "select c from Customer c where c.phone = :p", Customer.class);
        q.setParameter("p", phone);
        try {
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public void delete(Customer customer) {
        Customer attached = em.contains(customer) ? customer : em.merge(customer);
        em.remove(attached);
    }

    @Override
    public long count() {
        return em.createQuery("select count(c) from Customer c", Long.class)
                .getSingleResult();
    }

    @Override
    public List<Customer> findRecent(int limit) {
        TypedQuery<Customer> q = em.createQuery(
                "select c from Customer c order by c.id desc", Customer.class);
        q.setMaxResults(limit);
        return q.getResultList();
    }

    @Override
    public long countActive() {
        return em.createQuery("select count(c) from Customer c", Long.class).getSingleResult();
    }

    @Override
    public Customer findByEmail(String email) {
        TypedQuery<Customer> q = em.createQuery(
                "select c from Customer c where lower(c.email) = :e", Customer.class);
        q.setParameter("e", email.toLowerCase());
        try {
            return q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
