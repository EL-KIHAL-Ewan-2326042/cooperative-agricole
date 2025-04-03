package cooperative_agricole.commandes.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Commande_Produit")
public class CommandeProduit {
    @EmbeddedId
    private CommandeProduitId id;

    @ManyToOne
    @MapsId("commandeId")
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @ManyToOne
    @MapsId("produitId")
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    // Constructeurs
    public CommandeProduit() {}

    public CommandeProduit(Commande commande, Produit produit, Integer quantite) {
        this.id = new CommandeProduitId(commande.getId(), produit.getId());
        this.commande = commande;
        this.produit = produit;
        this.quantite = quantite;
    }

    // Getters et Setters
    public CommandeProduitId getId() { return id; }
    public void setId(CommandeProduitId id) { this.id = id; }

    public Commande getCommande() { return commande; }
    public void setCommande(Commande commande) { this.commande = commande; }

    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    // Classe embarquée pour la clé composite
    @Embeddable
    public static class CommandeProduitId implements Serializable {
        @Column(name = "commande_id")
        private Integer commandeId;

        @Column(name = "produit_id")
        private Integer produitId;

        // Constructeurs
        public CommandeProduitId() {}

        public CommandeProduitId(Integer commandeId, Integer produitId) {
            this.commandeId = commandeId;
            this.produitId = produitId;
        }

        // Getters et Setters
        public Integer getCommandeId() { return commandeId; }
        public void setCommandeId(Integer commandeId) { this.commandeId = commandeId; }

        public Integer getProduitId() { return produitId; }
        public void setProduitId(Integer produitId) { this.produitId = produitId; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CommandeProduitId that = (CommandeProduitId) o;
            return Objects.equals(commandeId, that.commandeId) &&
                    Objects.equals(produitId, that.produitId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(commandeId, produitId);
        }
    }
}