
package cooperative.paniers.model;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Représente un produit contenu dans le panier d'un utilisateur.
 * Cette classe associe un produit à un panier avec sa quantité et son prix,
 * tout en stockant les détails du produit pour un accès facile.
 */
public class PanierProduit {
    /**
     * Identifiant de l'utilisateur propriétaire du panier.
     * Fait partie de la clé primaire composite dans la base de données.
     */
    private Integer userId;

    /**
     * Identifiant du produit dans le catalogue.
     * Fait partie de la clé primaire composite dans la base de données.
     */
    private Integer produitId;

    /**
     * Quantité du produit ajoutée au panier.
     * Représente le nombre d'unités que l'utilisateur souhaite commander.
     */
    private Integer quantite;

    /**
     * Prix unitaire du produit au moment de son ajout au panier.
     * Permet de conserver le prix même si celui-ci change dans le catalogue.
     */
    private BigDecimal prixUnitaire;

    /**
     * Détails complets du produit sous forme de map.
     * Stocke dynamiquement les attributs du produit (nom, description, etc.)
     * sans nécessiter une classe Produit dédiée.
     */
    private Map<String, Object> produitDetails;

    /**
     * Récupère l'identifiant de l'utilisateur propriétaire du panier.
     *
     * @return L'identifiant de l'utilisateur
     */
    public Integer getUserId() { return userId; }

    /**
     * Définit l'identifiant de l'utilisateur propriétaire du panier.
     *
     * @param userId L'identifiant de l'utilisateur à définir
     */
    public void setUserId(Integer userId) { this.userId = userId; }

    /**
     * Récupère l'identifiant du produit.
     *
     * @return L'identifiant du produit
     */
    public Integer getProduitId() { return produitId; }

    /**
     * Définit l'identifiant du produit.
     *
     * @param produitId L'identifiant du produit à définir
     */
    public void setProduitId(Integer produitId) { this.produitId = produitId; }

    /**
     * Récupère la quantité du produit dans le panier.
     *
     * @return La quantité du produit
     */
    public Integer getQuantite() { return quantite; }

    /**
     * Définit la quantité du produit dans le panier.
     *
     * @param quantite La quantité à définir
     */
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    /**
     * Récupère le prix unitaire du produit dans le panier.
     *
     * @return Le prix unitaire du produit
     */
    public BigDecimal getPrixUnitaire() { return prixUnitaire; }

    /**
     * Définit le prix unitaire du produit dans le panier.
     *
     * @param prixUnitaire Le prix unitaire à définir
     */
    public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }

    /**
     * Récupère les détails complets du produit.
     *
     * @return Une map contenant tous les détails du produit
     */
    public Map<String, Object> getProduitDetails() { return produitDetails; }

    /**
     * Définit les détails complets du produit.
     *
     * @param produitDetails Une map contenant tous les détails du produit à définir
     */
    public void setProduitDetails(Map<String, Object> produitDetails) { this.produitDetails = produitDetails; }
}
