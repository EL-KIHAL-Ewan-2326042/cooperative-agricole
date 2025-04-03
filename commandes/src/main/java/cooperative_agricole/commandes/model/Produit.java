package cooperative_agricole.commandes.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Produit")
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "type")
    private String type;

    @Column(name = "prix", nullable = false)
    private BigDecimal prix;

    @Column(name = "image")
    private String image;

    @Column(name = "stock_disponible", nullable = false)
    private Integer stockDisponible;

    @ManyToOne
    @JoinColumn(name = "unite_id")
    private Unite unite;

    @OneToMany(mappedBy = "produit")
    private List<CommandeProduit> commandeProduits;

    // Constructeur par défaut
    public Produit() {}

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public Integer getStockDisponible() { return stockDisponible; }
    public void setStockDisponible(Integer stockDisponible) { this.stockDisponible = stockDisponible; }

    public Unite getUnite() { return unite; }
    public void setUnite(Unite unite) { this.unite = unite; }

    public List<CommandeProduit> getCommandeProduits() { return commandeProduits; }
    public void setCommandeProduits(List<CommandeProduit> commandeProduits) { this.commandeProduits = commandeProduits; }
}