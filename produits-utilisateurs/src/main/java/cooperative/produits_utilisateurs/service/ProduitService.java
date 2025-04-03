package cooperative.produits_utilisateurs.service;

import cooperative.produits_utilisateurs.model.Produit;
import cooperative.produits_utilisateurs.model.Type;
import cooperative.produits_utilisateurs.model.Unite;
import cooperative.produits_utilisateurs.repository.ProduitRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ProduitService {

    @Inject
    private ProduitRepository produitRepository;

    @Inject
    private TypeService typeService;

    @Inject
    private UniteService uniteService;

    /**
     * Récupère tous les produits.
     *
     * @return La liste de tous les produits.
     */
    public List<Produit> getAllProduits() {
        List<Produit> produits = produitRepository.findAll();
        for (Produit produit : produits) {
            enrichProduit(produit);
        }
        return produits;
    }

    /**
     * Récupère un produit par son identifiant.
     *
     * @param id L'identifiant du produit.
     * @return Le produit correspondant, ou null s'il n'existe pas.
     */
    public Produit getProduitById(Integer id) {
        Produit produit = produitRepository.findById(id);
        if (produit == null) {
            return null;
        }

        enrichProduit(produit);
        return produit;
    }

    /**
     * Enrichit un produit avec des informations supplémentaires.
     *
     * @param produit Le produit à enrichir.
     */
    private void enrichProduit(Produit produit) {
        // Enrichir avec les informations de type
        if (produit.getTypeId() != null) {
            Type type = typeService.getTypeById(produit.getTypeId());
            if (type != null) {
                produit.setType(type);
            }
        }

        // Enrichir avec les informations d'unité
        if (produit.getUnite() != null && produit.getUnite().getId() != null) {
            Unite unite = uniteService.getUniteById(produit.getUnite().getId());
            if (unite != null) {
                produit.setUnite(unite);
            }
        }
    }

    /**
     * Recherche des produits par leur nom.
     *
     * @param nom Le nom du produit.
     * @return La liste des produits correspondant au nom.
     */
    public List<Produit> findProduitsByNom(String nom) {
        List<Produit> produits = produitRepository.findByNom(nom);
        for (Produit produit : produits) {
            enrichProduit(produit);
        }
        return produits;
    }

    /**
     * Crée un nouveau produit.
     *
     * @param produit Le produit à créer.
     * @return Le produit créé.
     */
    public Produit createProduit(Produit produit) {
        produit.setDateMiseAJour(LocalDateTime.now());

        // Sauvegarder d'abord le produit
        Produit savedProduit = produitRepository.save(produit);

        // Enrichir avec les informations complètes
        enrichProduit(savedProduit);

        return savedProduit;
    }

    /**
     * Met à jour un produit existant.
     *
     * @param id L'identifiant du produit à mettre à jour.
     * @param produit Les nouvelles informations du produit.
     * @return Le produit mis à jour.
     */
    public Produit updateProduit(Integer id, Produit produit) {
        Produit existingProduit = produitRepository.findById(id);
        if (existingProduit == null) {
            return null;
        }

        produit.setId(id);
        produit.setDateMiseAJour(LocalDateTime.now());

        // Sauvegarder les modifications
        Produit updatedProduit = produitRepository.save(produit);

        // Enrichir avec les informations complètes
        enrichProduit(updatedProduit);

        return updatedProduit;
    }

    /**
     * Supprime un produit par son identifiant.
     *
     * @param id L'identifiant du produit à supprimer.
     * @return true si le produit a été supprimé, sinon false.
     */
    public boolean deleteProduit(Integer id) {
        Produit produit = produitRepository.findById(id);
        if (produit == null) {
            return false;
        }

        produitRepository.delete(id);
        return true;
    }
}
