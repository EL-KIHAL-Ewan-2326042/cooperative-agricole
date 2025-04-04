package cooperative.paniers.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Représente un panier d'achat associé à un utilisateur dans la coopérative agricole.
 * Cette classe permet de stocker les informations de base d'un panier ainsi que
 * la liste des produits qu'il contient.
 */
public class Panier {
    /**
     * Identifiant de l'utilisateur propriétaire du panier.
     * Sert de clé primaire dans la base de données.
     */
    private Integer userId;

    /**
     * Date et heure de la dernière modification du panier.
     * Permet de suivre les activités récentes sur le panier.
     */
    private LocalDateTime dateMiseAJour;

    /**
     * Liste des produits contenus dans le panier avec leurs quantités et prix.
     * Cette liste est chargée séparément et n'est pas directement stockée en base.
     */
    private List<PanierProduit> produits;

    /**
     * Récupère l'identifiant de l'utilisateur propriétaire du panier.
     *
     * @return L'identifiant de l'utilisateur
     */
    public Integer getUserId() { return userId; }

    /**
     * Définit l'identifiant de l'utilisateur propriétaire du panier.
     *
     * @param userId L'identifiant de l'utilisateur à associer au panier
     */
    public void setUserId(Integer userId) { this.userId = userId; }

    /**
     * Récupère la date et l'heure de la dernière mise à jour du panier.
     *
     * @return La date et l'heure de dernière modification
     */
    public LocalDateTime getDateMiseAJour() { return dateMiseAJour; }

    /**
     * Définit la date et l'heure de la dernière mise à jour du panier.
     *
     * @param dateMiseAJour La date et l'heure de dernière modification à définir
     */
    public void setDateMiseAJour(LocalDateTime dateMiseAJour) { this.dateMiseAJour = dateMiseAJour; }

    /**
     * Récupère la liste des produits contenus dans le panier.
     *
     * @return La liste des produits dans le panier
     */
    public List<PanierProduit> getProduits() { return produits; }

    /**
     * Définit la liste des produits contenus dans le panier.
     *
     * @param produits La liste des produits à associer au panier
     */
    public void setProduits(List<PanierProduit> produits) { this.produits = produits; }
}