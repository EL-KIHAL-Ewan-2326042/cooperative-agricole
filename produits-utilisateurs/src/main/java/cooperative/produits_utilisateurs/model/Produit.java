package cooperative.produits_utilisateurs.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Produit {

    private Integer id;
    
    private String nom;
    
    private String type;
    
    private BigDecimal prix;
    
    private BigDecimal quantite;
    
    private Unite unite;
    
    private LocalDateTime dateMiseAJour;
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }
    
    public BigDecimal getQuantite() { return quantite; }
    public void setQuantite(BigDecimal quantite) { this.quantite = quantite; }
    
    public Unite getUnite() { return unite; }
    public void setUnite(Unite unite) { this.unite = unite; }
    
    public LocalDateTime getDateMiseAJour() { return dateMiseAJour; }
    public void setDateMiseAJour(LocalDateTime dateMiseAJour) { this.dateMiseAJour = dateMiseAJour; }
}