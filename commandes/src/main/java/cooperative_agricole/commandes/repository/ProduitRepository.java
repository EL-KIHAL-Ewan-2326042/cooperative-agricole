package cooperative_agricole.commandes.repository;

import cooperative_agricole.commandes.model.Produit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ProduitRepository {

    @Inject
    private DatabaseRepository repository;

    public List<Produit> findAll() {
        return repository.findAll(Produit.class);
    }

    public Produit findById(Integer id) {
        return repository.findById(Produit.class, id);
    }

    public Produit save(Produit produit) {
        return repository.save(produit);
    }

    public boolean delete(Integer id) {
        return repository.delete(Produit.class, id);
    }

    public List<Produit> findByNom(String nom) {
        return repository.findByField(Produit.class, "nom", nom);
    }
}