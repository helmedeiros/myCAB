package app.fourthink.persistence;

import app.fourthink.model.Dispatch;

import java.util.List;

public interface DispatchRepository {

    Dispatch save(Dispatch dispatch);

    Dispatch findById(Long id);

    List<Dispatch> findActive();

    List<Dispatch> findByCabId(Long cabId);

    List<Dispatch> findByCustomerId(Long customerId);
}
