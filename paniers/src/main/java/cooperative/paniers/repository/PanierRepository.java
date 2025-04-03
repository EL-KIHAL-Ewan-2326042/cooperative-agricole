package cooperative.paniers.repository;

import cooperative.paniers.model.Panier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PanierRepository {
    @Inject
    private DatabaseRepository dbRepository;

    public Panier findByUserId(Integer userId) {
        return dbRepository.findById(Panier.class, userId);
    }

    public Panier save(Panier panier) {
        return dbRepository.save(panier);
    }

    public void delete(Integer userId) {
        dbRepository.delete(Panier.class, userId);
    }
}