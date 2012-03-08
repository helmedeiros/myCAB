package app.fourthink.seed;

import app.fourthink.model.CabCategory;
import app.fourthink.model.CabModel;
import app.fourthink.persistence.CabModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class CabCatalogSeeder {

    private final CabModelRepository repository;
    private final List<CabModel> catalog = new ArrayList<CabModel>();

    @Autowired
    public CabCatalogSeeder(CabModelRepository repository) {
        this.repository = repository;
        registerDefaults();
    }

    protected void registerDefaults() {
        register("Volkswagen", "Gol", CabCategory.NORMAL);
        register("Fiat", "Palio", CabCategory.NORMAL);
        register("Chevrolet", "Celta", CabCategory.NORMAL);
    }

    public List<CabModel> defaultCatalog() {
        return new ArrayList<CabModel>(catalog);
    }

    protected void register(String make, String model, CabCategory category) {
        catalog.add(new CabModel(make, model, category));
    }

    @PostConstruct
    public void seed() {
        if (repository.count() > 0) {
            return;
        }
        for (CabModel m : defaultCatalog()) {
            repository.save(m);
        }
    }
}
