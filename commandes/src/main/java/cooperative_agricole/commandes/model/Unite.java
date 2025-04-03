package cooperative_agricole.commandes.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Unite")
public class Unite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "symbole", nullable = false)
    private String symbole;

    @OneToMany(mappedBy = "unite")
    private List<Produit> produits;

    // Constructeur par défaut
    public Unite() {}

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getSymbole() { return symbole; }
    public void setSymbole(String symbole) { this.symbole = symbole; }

    public List<Produit> getProduits() { return produits; }
    public void setProduits(List<Produit> produits) { this.produits = produits; }
}