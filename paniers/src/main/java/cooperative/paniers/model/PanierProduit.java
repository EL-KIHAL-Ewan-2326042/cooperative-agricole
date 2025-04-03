package cooperative.paniers.model;

import java.math.BigDecimal;
import java.util.Map;

public class PanierProduit {
    private Integer userId;
    private Integer produitId;
    private Integer quantite;
    private BigDecimal prixUnitaire;
    private Map<String, Object> produitDetails; // Au lieu d'utiliser une classe Produit

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getProduitId() { return produitId; }
    public void setProduitId(Integer produitId) { this.produitId = produitId; }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    public BigDecimal getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }

    public Map<String, Object> getProduitDetails() { return produitDetails; }
    public void setProduitDetails(Map<String, Object> produitDetails) { this.produitDetails = produitDetails; }
}