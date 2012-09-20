package app.fourthink.service;

import app.fourthink.model.CabCategory;
import app.fourthink.model.CabModel;
import app.fourthink.persistence.CabModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CabModelDirectory {

    private final CabModelRepository models;

    @Autowired
    public CabModelDirectory(CabModelRepository models) {
        this.models = models;
    }

    public List<CabModel> all() {
        return models.findAll();
    }

    public List<CabModel> byCategory(CabCategory category) {
        return models.findByCategory(category);
    }

    public List<CabModel> searchByMake(String fragment) {
        return models.searchByMake(fragment);
    }

    public List<CabModel> byCategoryAndMake(CabCategory category, String fragment) {
        return models.findByCategoryAndMake(category, fragment);
    }
}
