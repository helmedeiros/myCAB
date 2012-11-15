package app.fourthink.seed;

import app.fourthink.model.CabCategory;
import app.fourthink.model.CabModel;
import app.fourthink.persistence.CabModelRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class CabCatalogSeederModelsTest {

    private CabCatalogSeeder seeder;

    @Before
    public void setUp() {
        seeder = new CabCatalogSeeder(new StubRepository());
    }

    @Test
    public void includesPopularBrazilianMakes() {
        Set<String> makes = new HashSet<String>();
        for (CabModel m : seeder.defaultCatalog()) {
            makes.add(m.getMake());
        }
        assertTrue(makes.contains("Volkswagen"));
        assertTrue(makes.contains("Toyota"));
        assertTrue(makes.contains("Fiat"));
        assertTrue(makes.contains("Honda"));
    }

    private static class StubRepository implements CabModelRepository {
        private final List<CabModel> store = new ArrayList<CabModel>();

        public CabModel save(CabModel model) { store.add(model); return model; }
        public CabModel findById(Long id) { return null; }
        public List<CabModel> findAll() { return store; }
        public List<CabModel> findByCategory(CabCategory category) {
            List<CabModel> out = new ArrayList<CabModel>();
            for (CabModel m : store) if (m.getCategory() == category) out.add(m);
            return out;
        }
        public List<CabModel> searchByMake(String make) {
            List<CabModel> out = new ArrayList<CabModel>();
            for (CabModel m : store) if (m.getMake().toLowerCase().contains(make.toLowerCase())) out.add(m);
            return out;
        }
        public List<CabModel> findByCategoryAndMake(CabCategory c, String m) { return new ArrayList<CabModel>(); }
        public long count() { return store.size(); }
    }
}
