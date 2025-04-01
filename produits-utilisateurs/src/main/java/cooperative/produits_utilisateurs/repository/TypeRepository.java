package cooperative.produits_utilisateurs.repository;

import cooperative.produits_utilisateurs.model.Type;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class TypeRepository {

    @Inject
    private DatabaseRepository dbRepository;

    public List<Type> findAll() {
        return dbRepository.findAll(Type.class);
    }

    public Type findById(Integer id) {
        return dbRepository.findById(Type.class, id);
    }

    public Type save(Type type) {
        return dbRepository.save(type);
    }

    public void delete(Integer id) {
        dbRepository.delete(Type.class, id);
    }
}