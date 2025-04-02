package cooperative.produits_utilisateurs.model;

import jakarta.json.bind.annotation.JsonbTransient;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Integer getId() { return produitId; }
    public void setId(Integer id) { this.produitId = id; }

    public Integer getProduitId() { return produitId; }
    public void setProduitId(Integer produitId) { this.produitId = produitId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return type != null ? type.getNom() : null;
    }

    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    public Unite getUnite() { return unite; }
    public void setUnite(Unite unite) { this.unite = unite; }

    public Integer getUniteId() { return uniteId; }
    public void setUniteId(Integer uniteId) { this.uniteId = uniteId; }

    public LocalDateTime getDateMiseAJour() { return dateMiseAJour; }
    public void setDateMiseAJour(LocalDateTime dateMiseAJour) { this.dateMiseAJour = dateMiseAJour; }
}