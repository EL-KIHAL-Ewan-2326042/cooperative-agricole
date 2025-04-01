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

    public List<Produit> getAllProduits() {
        List<Produit> produits = produitRepository.findAll();
        for (Produit produit : produits) {
            enrichProduit(produit);
        }
        return produits;
    }

    public Produit getProduitById(Integer id) {
        Produit produit = produitRepository.findById(id);
        if (produit == null) {
            return null;
        }

        enrichProduit(produit);
        return produit;
    }

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

    public List<Produit> findProduitsByNom(String nom) {
        List<Produit> produits = produitRepository.findByNom(nom);
        for (Produit produit : produits) {
            enrichProduit(produit);
        }
        return produits;
    }

    public Produit createProduit(Produit produit) {
        produit.setDateMiseAJour(LocalDateTime.now());

        // Sauvegarder d'abord le produit
        Produit savedProduit = produitRepository.save(produit);

        // Enrichir avec les informations complètes
        enrichProduit(savedProduit);

        return savedProduit;
    }

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

    public boolean deleteProduit(Integer id) {
        Produit produit = produitRepository.findById(id);
        if (produit == null) {
            return false;
        }

        produitRepository.delete(id);
        return true;
    }
}