package cooperative_agricole.commandes.service;

import cooperative_agricole.commandes.model.Commande;
import cooperative_agricole.commandes.model.CommandeProduit;
import cooperative_agricole.commandes.model.Produit;
import cooperative_agricole.commandes.model.CommandeProduit.CommandeProduitId;
import cooperative_agricole.commandes.repository.DatabaseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class CommandeProduitService {

    @Inject
    private DatabaseRepository repository;

    @Inject
    private CommandeService commandeService;

    public List<CommandeProduit> getProduitsCommande(Integer commandeId) {
        return repository.findByField(CommandeProduit.class, "commande.id", commandeId);
    }

    public CommandeProduit ajouterProduitCommande(CommandeProduit commandeProduit) {
        // Validation
        validateCommandeProduit(commandeProduit);

        // Vérifier que la commande existe
        Commande commande = repository.findById(Commande.class, commandeProduit.getCommande().getId());
        if (commande == null) {
            throw new IllegalArgumentException("Commande non trouvée");
        }

        // Vérifier que le produit existe
        Produit produit = repository.findById(Produit.class, commandeProduit.getProduit().getId());
        if (produit == null) {
            throw new IllegalArgumentException("Produit non trouvé");
        }

        // Vérifier le stock disponible
        if (produit.getStockDisponible() < commandeProduit.getQuantite()) {
            throw new IllegalArgumentException("Stock insuffisant");
        }

        // Créer l'ID composite
        CommandeProduitId id = new CommandeProduitId(commande.getId(), produit.getId());
        commandeProduit.setId(id);
        commandeProduit.setCommande(commande);
        commandeProduit.setProduit(produit);

        // Mettre à jour le sous-total de la commande
        BigDecimal prixTotal = produit.getPrix().multiply(new BigDecimal(commandeProduit.getQuantite()));
        commande.setSousTotal(commande.getSousTotal().add(prixTotal));
        commande.calculerTotal();
        repository.save(commande);

        // Réduire le stock disponible
        produit.setStockDisponible(produit.getStockDisponible() - commandeProduit.getQuantite());
        repository.save(produit);

        return repository.save(commandeProduit);
    }

    public CommandeProduit modifierQuantiteProduit(Integer commandeId, Integer produitId, Integer quantite) {
        // Valider la quantité
        if (quantite <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }

        // Trouver la commande et le produit
        Commande commande = repository.findById(Commande.class, commandeId);
        if (commande == null) {
            throw new IllegalArgumentException("Commande non trouvée");
        }

        Produit produit = repository.findById(Produit.class, produitId);
        if (produit == null) {
            throw new IllegalArgumentException("Produit non trouvé");
        }

        // Trouver l'entrée CommandeProduit
        CommandeProduitId id = new CommandeProduitId(commandeId, produitId);
        CommandeProduit commandeProduit = repository.findByCompositeId(CommandeProduit.class, commandeId, produitId);
        if (commandeProduit == null) {
            throw new IllegalArgumentException("Produit non trouvé dans la commande");
        }

        // Calculer la différence de quantité
        int difference = quantite - commandeProduit.getQuantite();

        // Vérifier le stock disponible si on augmente la quantité
        if (difference > 0 && produit.getStockDisponible() < difference) {
            throw new IllegalArgumentException("Stock insuffisant");
        }

        // Mettre à jour le sous-total de la commande
        BigDecimal ancienPrix = produit.getPrix().multiply(new BigDecimal(commandeProduit.getQuantite()));
        BigDecimal nouveauPrix = produit.getPrix().multiply(new BigDecimal(quantite));
        commande.setSousTotal(commande.getSousTotal().subtract(ancienPrix).add(nouveauPrix));
        commande.calculerTotal();
        repository.save(commande);

        // Mettre à jour le stock disponible
        produit.setStockDisponible(produit.getStockDisponible() - difference);
        repository.save(produit);

        // Mettre à jour la quantité
        commandeProduit.setQuantite(quantite);
        return repository.save(commandeProduit);
    }

    public void supprimerProduitCommande(Integer commandeId, Integer produitId) {
        // Trouver la commande et le produit
        Commande commande = repository.findById(Commande.class, commandeId);
        if (commande == null) {
            throw new IllegalArgumentException("Commande non trouvée");
        }

        Produit produit = repository.findById(Produit.class, produitId);
        if (produit == null) {
            throw new IllegalArgumentException("Produit non trouvé");
        }

        // Trouver l'entrée CommandeProduit
        CommandeProduitId id = new CommandeProduitId(commandeId, produitId);
        CommandeProduit commandeProduit = repository.findByCompositeId(CommandeProduit.class, commandeId, produitId);
        if (commandeProduit == null) {
            throw new IllegalArgumentException("Produit non trouvé dans la commande");
        }

        // Mettre à jour le sous-total de la commande
        BigDecimal prixProduit = produit.getPrix().multiply(new BigDecimal(commandeProduit.getQuantite()));
        commande.setSousTotal(commande.getSousTotal().subtract(prixProduit));
        commande.calculerTotal();
        repository.save(commande);

        // Remettre en stock les produits
        produit.setStockDisponible(produit.getStockDisponible() + commandeProduit.getQuantite());
        repository.save(produit);

        // Supprimer l'entrée
        repository.deleteByCompositeId(CommandeProduit.class, commandeId, produitId);
    }

    private void validateCommandeProduit(CommandeProduit commandeProduit) {
        if (commandeProduit.getCommande() == null || commandeProduit.getCommande().getId() == null) {
            throw new IllegalArgumentException("La commande est obligatoire");
        }

        if (commandeProduit.getProduit() == null || commandeProduit.getProduit().getId() == null) {
            throw new IllegalArgumentException("Le produit est obligatoire");
        }

        if (commandeProduit.getQuantite() == null || commandeProduit.getQuantite() <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }
    }
}