package cooperative.produits_utilisateurs.service;

import cooperative.produits_utilisateurs.model.Produit;
import cooperative.produits_utilisateurs.repository.ProduitRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ProduitService {

    @Inject
    private ProduitRepository produitRepository;

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public Produit getProduitById(Integer id) {
        return produitRepository.findById(id);
    }

    public List<Produit> findProduitsByNom(String nom) {
        return produitRepository.findByNom(nom);
    }

    public Produit createProduit(Produit produit) {
        produit.setDateMiseAJour(LocalDateTime.now());
        return produitRepository.save(produit);
    }

    public Produit updateProduit(Integer id, Produit produit) {
        Produit existingProduit = produitRepository.findById(id);
        if (existingProduit == null) {
            return null;
        }

        produit.setId(id);
        produit.setDateMiseAJour(LocalDateTime.now());
        return produitRepository.save(produit);
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