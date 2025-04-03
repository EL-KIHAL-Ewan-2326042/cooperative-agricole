package cooperative_agricole.commandes.repository;

import cooperative_agricole.commandes.model.Unite;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class UniteRepository {

    @Inject
    private DatabaseRepository repository;

    public List<Unite> findAll() {
        return repository.findAll(Unite.class);
    }

    public Unite findById(Integer id) {
        return repository.findById(Unite.class, id);
    }

    public Unite save(Unite unite) {
        return repository.save(unite);
    }

    public boolean delete(Integer id) {
        return repository.delete(Unite.class, id);
    }
}