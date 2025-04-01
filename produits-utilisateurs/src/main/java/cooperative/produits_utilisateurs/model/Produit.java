package cooperative.produits_utilisateurs.model;

import jakarta.json.bind.annotation.JsonbTransient;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Produit {

    private TypeProduit type;

    private Integer id;

    private String nom;

    private Integer typeId;

    private String typeName;

    private BigDecimal prix;

    private Integer quantite;

    private Unite unite;

    private LocalDateTime dateMiseAJour;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public TypeProduit getType() { return type; }
    public void setType(TypeProduit type) { this.type = type; }

    public Integer getTypeId() { return typeId; }
    public void setTypeId(Integer typeId) { this.typeId = typeId; }

    // Suppression de l'annotation @JsonbTransient pour exposer cette propriété
    public String getTypeName() {
        // Vérifier si typeName est null avant de concaténer
        return typeName != null ? "Type " + typeName : null;
    }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    public Unite getUnite() { return unite; }
    public void setUnite(Unite unite) { this.unite = unite; }

    public LocalDateTime getDateMiseAJour() { return dateMiseAJour; }
    public void setDateMiseAJour(LocalDateTime dateMiseAJour) { this.dateMiseAJour = dateMiseAJour; }
}