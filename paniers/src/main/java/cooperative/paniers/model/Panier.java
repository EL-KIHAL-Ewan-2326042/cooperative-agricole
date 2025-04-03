package cooperative.paniers.model;

import java.time.LocalDateTime;
import java.util.List;

public class Panier {
    private Integer userId;
    private LocalDateTime dateMiseAJour;
    private List<PanierProduit> produits;

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public LocalDateTime getDateMiseAJour() { return dateMiseAJour; }
    public void setDateMiseAJour(LocalDateTime dateMiseAJour) { this.dateMiseAJour = dateMiseAJour; }

    public List<PanierProduit> getProduits() { return produits; }
    public void setProduits(List<PanierProduit> produits) { this.produits = produits; }
}