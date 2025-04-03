package cooperative.produits_utilisateurs.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Représente un produit dans le système.
 */
public class Produit {

    private Integer produitId;
    private String nom;
    private BigDecimal prix;
    private Integer quantite;
    private Integer uniteId;
    private LocalDateTime dateMiseAJour;
    private Integer typeId;
    private Type type;
    private Unite unite;

    /**
     * Obtient l'identifiant du produit.
     * @return l'identifiant du produit.
     */
    public Integer getId() { return produitId; }

    /**
     * Définit l'identifiant du produit.
     * @param id l'identifiant du produit.
     */
    public void setId(Integer id) { this.produitId = id; }

    /**
     * Obtient l'identifiant du produit.
     * @return l'identifiant du produit.
     */
    public Integer getProduitId() { return produitId; }

    /**
     * Définit l'identifiant du produit.
     * @param produitId l'identifiant du produit.
     */
    public void setProduitId(Integer produitId) { this.produitId = produitId; }

    /**
     * Obtient le nom du produit.
     * @return le nom du produit.
     */
    public String getNom() { return nom; }

    /**
     * Définit le nom du produit.
     * @param nom le nom du produit.
     */
    public void setNom(String nom) { this.nom = nom; }

    /**
     * Obtient le type du produit.
     * @return le type du produit.
     */
    public Type getType() { return type; }

    /**
     * Définit le type du produit.
     * @param type le type du produit.
     */
    public void setType(Type type) { this.type = type; }

    /**
     * Obtient l'identifiant du type du produit.
     * @return l'identifiant du type du produit.
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * Définit l'identifiant du type du produit.
     * @param typeId l'identifiant du type du produit.
     */
    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * Obtient le nom du type du produit.
     * @return le nom du type du produit.
     */
    public String getTypeName() {
        return type != null ? type.getNom() : null;
    }

    /**
     * Obtient le prix du produit.
     * @return le prix du produit.
     */
    public BigDecimal getPrix() { return prix; }

    /**
     * Définit le prix du produit.
     * @param prix le prix du produit.
     */
    public void setPrix(BigDecimal prix) { this.prix = prix; }

    /**
     * Obtient la quantité du produit.
     * @return la quantité du produit.
     */
    public Integer getQuantite() { return quantite; }

    /**
     * Définit la quantité du produit.
     * @param quantite la quantité du produit.
     */
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    /**
     * Obtient l'unité du produit.
     * @return l'unité du produit.
     */
    public Unite getUnite() { return unite; }

    /**
     * Définit l'unité du produit.
     * @param unite l'unité du produit.
     */
    public void setUnite(Unite unite) { this.unite = unite; }

    /**
     * Obtient l'identifiant de l'unité du produit.
     * @return l'identifiant de l'unité du produit.
     */
    public Integer getUniteId() { return uniteId; }

    /**
     * Définit l'identifiant de l'unité du produit.
     * @param uniteId l'identifiant de l'unité du produit.
     */
    public void setUniteId(Integer uniteId) { this.uniteId = uniteId; }

    /**
     * Obtient la date de mise à jour du produit.
     * @return la date de mise à jour du produit.
     */
    public LocalDateTime getDateMiseAJour() { return dateMiseAJour; }

    /**
     * Définit la date de mise à jour du produit.
     * @param dateMiseAJour la date de mise à jour du produit.
     */
    public void setDateMiseAJour(LocalDateTime dateMiseAJour) { this.dateMiseAJour = dateMiseAJour; }
}
