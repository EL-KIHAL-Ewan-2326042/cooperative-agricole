package cooperative.produits_utilisateurs.repository;

import cooperative.produits_utilisateurs.model.Produit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ProduitRepository {

    @Inject
    private DatabaseRepository dbRepository;

    public List<Produit> findAll() {
        return dbRepository.findAll(Produit.class);
    }

    public Produit findById(Integer id) {
        return dbRepository.findById(Produit.class, id);
    }

    public Produit save(Produit produit) {
        return dbRepository.save(produit);
    }

    public void delete(Integer id) {
        dbRepository.delete(Produit.class, id);
    }

    @SuppressWarnings("unchecked")
    public List<Produit> findByNom(String nom) {
        return (List<Produit>) dbRepository.findByField(Produit.class, "nom", nom);
    }
}