package cooperative.produits_utilisateurs.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Produit {

    private Integer id;
    private String nom;
    private Type type;
    private BigDecimal prix;
    private Integer quantite;
    private Unite unite;
    private LocalDateTime dateMiseAJour;
    private String imagePath;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Type getType() { return type; }
    public void setType(Type type) { this.type = type; }

    public Integer getTypeId() {
        return type != null ? type.getId() : null;
    }

    public void setTypeId(Integer typeId) {
        if (this.type == null) {
            this.type = new Type();
        }
        this.type.setId(typeId);
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

    public LocalDateTime getDateMiseAJour() { return dateMiseAJour; }
    public void setDateMiseAJour(LocalDateTime dateMiseAJour) { this.dateMiseAJour = dateMiseAJour; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}