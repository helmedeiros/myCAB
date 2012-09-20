package app.fourthink.persistence;

import app.fourthink.model.CabCategory;
import app.fourthink.model.CabModel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class JpaCabModelRepository implements CabModelRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public CabModel save(CabModel model) {
        if (model.getId() == null) {
            em.persist(model);
            return model;
        }
        return em.merge(model);
    }

    @Override
    public CabModel findById(Long id) {
        return em.find(CabModel.class, id);
    }

    @Override
    public List<CabModel> findAll() {
        TypedQuery<CabModel> q = em.createQuery(
                "select m from CabModel m order by m.make, m.model", CabModel.class);
        return q.getResultList();
    }

    @Override
    public List<CabModel> findByCategory(CabCategory category) {
        TypedQuery<CabModel> q = em.createQuery(
                "select m from CabModel m where m.category = :c order by m.make, m.model",
                CabModel.class);
        q.setParameter("c", category);
        return q.getResultList();
    }

    @Override
    public long count() {
        return em.createQuery("select count(m) from CabModel m", Long.class)
                .getSingleResult();
    }

    @Override
    public List<CabModel> searchByMake(String make) {
        TypedQuery<CabModel> q = em.createQuery(
                "select m from CabModel m where lower(m.make) like :v order by m.make, m.model",
                CabModel.class);
        q.setParameter("v", "%" + make.toLowerCase() + "%");
        return q.getResultList();
    }

    @Override
    public List<CabModel> findByCategoryAndMake(app.fourthink.model.CabCategory category, String make) {
        TypedQuery<CabModel> q = em.createQuery(
                "select m from CabModel m where m.category = :c and lower(m.make) like :v order by m.make, m.model",
                CabModel.class);
        q.setParameter("c", category);
        q.setParameter("v", "%" + make.toLowerCase() + "%");
        return q.getResultList();
    }
}
