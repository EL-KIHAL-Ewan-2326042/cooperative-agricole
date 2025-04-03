package cooperative_agricole.commandes.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Commande")
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "sous_total", nullable = false)
    private BigDecimal sousTotal;

    @Column(name = "frais_livraison", nullable = false)
    private BigDecimal fraisLivraison;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @Column(name = "paiement")
    private String paiement;

    @Column(name = "statut")
    private String statut;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommandeProduit> commandeProduits;

    // Constructeur par défaut
    public Commande() {
        this.date = LocalDateTime.now();
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public BigDecimal getSousTotal() { return sousTotal; }
    public void setSousTotal(BigDecimal sousTotal) { this.sousTotal = sousTotal; }

    public BigDecimal getFraisLivraison() { return fraisLivraison; }
    public void setFraisLivraison(BigDecimal fraisLivraison) { this.fraisLivraison = fraisLivraison; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public String getPaiement() { return paiement; }
    public void setPaiement(String paiement) { this.paiement = paiement; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public List<CommandeProduit> getCommandeProduits() { return commandeProduits; }
    public void setCommandeProduits(List<CommandeProduit> commandeProduits) { this.commandeProduits = commandeProduits; }

    // Méthode utilitaire pour calculer le total
    public void calculerTotal() {
        this.total = this.sousTotal.add(this.fraisLivraison);
    }
}