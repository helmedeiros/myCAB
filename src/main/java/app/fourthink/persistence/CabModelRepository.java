package app.fourthink.persistence;

import app.fourthink.model.CabCategory;
import app.fourthink.model.CabModel;

import java.util.List;

public interface CabModelRepository {

    CabModel save(CabModel model);

    CabModel findById(Long id);

    List<CabModel> findAll();

    List<CabModel> findByCategory(CabCategory category);

    long count();

    java.util.List<app.fourthink.model.CabModel> searchByMake(String make);
}
