package app.fourthink.seed;

import app.fourthink.model.CabCategory;
import app.fourthink.model.CabModel;
import app.fourthink.persistence.CabModelRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CabCatalogSeederTest {

    private InMemoryRepository repository;
    private CabCatalogSeeder seeder;

    @Before
    public void setUp() {
        repository = new InMemoryRepository();
        seeder = new CabCatalogSeeder(repository);
    }

    @Test
    public void catalogIncludesAllThreeCategories() {
        Map<CabCategory, Integer> counts = new HashMap<CabCategory, Integer>();
        for (CabModel m : seeder.defaultCatalog()) {
            Integer n = counts.get(m.getCategory());
            counts.put(m.getCategory(), n == null ? 1 : n + 1);
        }
        assertTrue(counts.get(CabCategory.NORMAL) >= 8);
        assertTrue(counts.get(CabCategory.CONFORTO) >= 8);
        assertTrue(counts.get(CabCategory.GRANDE) >= 8);
    }

    @Test
    public void seedingPopulatesAnEmptyRepository() {
        seeder.seed();
        assertEquals(seeder.defaultCatalog().size(), repository.count());
    }

    @Test
    public void seedingIsIdempotentWhenRepositoryNonEmpty() {
        seeder.seed();
        long after = repository.count();
        seeder.seed();
        assertEquals(after, repository.count());
    }

    private static class InMemoryRepository implements CabModelRepository {
        private final List<CabModel> store = new ArrayList<CabModel>();
        private long sequence = 0;

        public CabModel save(CabModel model) {
            store.add(model);
            sequence++;
            return model;
        }

        public CabModel findById(Long id) {
            return null;
        }

        public List<CabModel> findAll() {
            return new ArrayList<CabModel>(store);
        }

        public List<CabModel> findByCategory(CabCategory category) {
            List<CabModel> out = new ArrayList<CabModel>();
            for (CabModel m : store) {
                if (m.getCategory() == category) out.add(m);
            }
            return out;
        }

        public long count() {
            return store.size();
        }

        public List<CabModel> findByCategoryAndMake(CabCategory category, String make) {
            List<CabModel> out = new ArrayList<CabModel>();
            for (CabModel m : store) {
                if (m.getCategory() == category && m.getMake().toLowerCase().contains(make.toLowerCase())) {
                    out.add(m);
                }
            }
            return out;
        }

        public List<CabModel> searchByMake(String make) {
            List<CabModel> out = new ArrayList<CabModel>();
            for (CabModel m : store) {
                if (m.getMake().toLowerCase().contains(make.toLowerCase())) out.add(m);
            }
            return out;
        }
    }
}
