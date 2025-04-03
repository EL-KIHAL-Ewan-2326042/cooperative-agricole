package cooperative.produits_utilisateurs.repository;

import cooperative.produits_utilisateurs.model.Produit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Repository pour les opérations CRUD sur les produits.
 */
@ApplicationScoped
public class ProduitRepository {

    @Inject
    private DatabaseRepository dbRepository;

    /**
     * Récupère tous les produits.
     *
     * @return une liste de tous les produits
     */
    public List<Produit> findAll() {
        return dbRepository.findAll(Produit.class);
    }

    /**
     * Récupère un produit par son identifiant.
     *
     * @param id l'identifiant du produit
     * @return le produit trouvé ou null si non trouvé
     */
    public Produit findById(Integer id) {
        return dbRepository.findById(Produit.class, id);
    }

    /**
     * Sauvegarde un produit dans la base de données.
     *
     * @param produit le produit à sauvegarder
     * @return le produit sauvegardé
     */
    public Produit save(Produit produit) {
        return dbRepository.save(produit);
    }

    /**
     * Supprime un produit par son identifiant.
     *
     * @param id l'identifiant du produit
     */
    public void delete(Integer id) {
        dbRepository.delete(Produit.class, id);
    }

    /**
     * Récupère les produits par leur nom.
     *
     * @param nom le nom du produit
     * @return une liste des produits trouvés
     */
    @SuppressWarnings("unchecked")
    public List<Produit> findByNom(String nom) {
        return (List<Produit>) dbRepository.findByField(Produit.class, "nom", nom);
    }
}
