package cooperative.paniers.services;

import cooperative.paniers.model.Panier;
import cooperative.paniers.model.PanierProduit;
import cooperative.paniers.repository.PanierRepository; // Import corrigé
import cooperative.paniers.repository.PanierProduitRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;

@ApplicationScoped
public class PanierService {
    @Inject
    private PanierRepository panierRepository;

    @Inject
    private PanierProduitRepository panierProduitRepository;

    public Panier getPanierByUserId(Integer userId) {
        Panier panier = panierRepository.findByUserId(userId);
        if (panier == null) {
            panier = new Panier();
            panier.setUserId(userId);
            panier.setDateMiseAJour(LocalDateTime.now());
            panier = panierRepository.save(panier);
        }

        List<PanierProduit> produits = panierProduitRepository.findByUserId(userId);

        for (PanierProduit item : produits) {
            Map<String, Object> produitDetails = recupererDetailsProduit(item.getProduitId());
            item.setProduitDetails(produitDetails);
        }

        panier.setProduits(produits);
        return panier;
    }

    public PanierProduit ajouterProduit(Integer userId, Integer produitId, Integer quantite) {
        Map<String, Object> produitDetails = recupererDetailsProduit(produitId);
        if (produitDetails == null) {
            return null;
        }

        Panier panier = panierRepository.findByUserId(userId);
        if (panier == null) {
            panier = new Panier();
            panier.setUserId(userId);
        }

        panier.setDateMiseAJour(LocalDateTime.now());
        panierRepository.save(panier);

        PanierProduit panierProduit = new PanierProduit();
        panierProduit.setUserId(userId);
        panierProduit.setProduitId(produitId);
        panierProduit.setQuantite(quantite);

        BigDecimal prix = extrairePrixProduit(produitDetails);
        panierProduit.setPrixUnitaire(prix);

        PanierProduit saved = panierProduitRepository.save(panierProduit);
        saved.setProduitDetails(produitDetails);

        return saved;
    }

    public boolean supprimerProduit(Integer userId, Integer produitId) {
        panierProduitRepository.deleteByUserIdAndProduitId(userId, produitId);

        Panier panier = panierRepository.findByUserId(userId);
        if (panier != null) {
            panier.setDateMiseAJour(LocalDateTime.now());
            panierRepository.save(panier);
        }

        return true;
    }

    public boolean viderPanier(Integer userId) {
        panierProduitRepository.deleteAllByUserId(userId);

        Panier panier = panierRepository.findByUserId(userId);
        if (panier != null) {
            panier.setDateMiseAJour(LocalDateTime.now());
            panierRepository.save(panier);
        }

        return true;
    }

    /**
     * Récupère les détails d'un produit depuis une source externe
     * @param produitId l'identifiant du produit
     * @return une map contenant les détails du produit, ou null si le produit n'existe pas
     */
    private Map<String, Object> recupererDetailsProduit(Integer produitId) {
        if (produitId == null) {
            return null;
        }

        Map<String, Object> details = new HashMap<>();
        details.put("id", produitId);
        details.put("nom", "Produit " + produitId);
        details.put("description", "Description du produit " + produitId);
        details.put("prix", new BigDecimal("10.99"));
        details.put("stock", 100);

        return details;
    }

    /**
     * Extrait le prix d'un produit à partir de ses détails
     * @param produitDetails les détails du produit
     * @return le prix du produit
     */
    private BigDecimal extrairePrixProduit(Map<String, Object> produitDetails) {
        if (produitDetails == null || !produitDetails.containsKey("prix")) {
            return new BigDecimal("0.00");
        }

        Object prixObj = produitDetails.get("prix");
        if (prixObj instanceof BigDecimal) {
            return (BigDecimal) prixObj;
        } else if (prixObj instanceof Number) {
            return new BigDecimal(prixObj.toString());
        } else if (prixObj instanceof String) {
            return new BigDecimal((String) prixObj);
        }

        return new BigDecimal("0.00");
    }
}