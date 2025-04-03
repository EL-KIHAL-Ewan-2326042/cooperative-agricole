package cooperative_agricole.commandes.service;

import cooperative_agricole.commandes.model.Commande;
import cooperative_agricole.commandes.repository.DatabaseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class CommandeService {

    @Inject
    private DatabaseRepository repository;

    public List<Commande> getAllCommandes() {
        return repository.findAll(Commande.class);
    }

    public Commande getCommandeById(Integer id) {
        return repository.findById(Commande.class, id);
    }

    public Commande createCommande(Commande commande) {
        // Validation
        validateCommande(commande);

        // Initialisation des valeurs par défaut
        if (commande.getDate() == null) {
            commande.setDate(LocalDateTime.now());
        }

        if (commande.getStatut() == null || commande.getStatut().trim().isEmpty()) {
            commande.setStatut("en attente");
        }

        // Calcul du total
        if (commande.getSousTotal() != null && commande.getFraisLivraison() != null) {
            commande.calculerTotal();
        }

        return repository.save(commande);
    }

    public Commande updateCommande(Commande commande) {
        // Validation
        validateCommande(commande);

        // Recalcul du total si nécessaire
        if (commande.getSousTotal() != null && commande.getFraisLivraison() != null) {
            commande.calculerTotal();
        }

        return repository.save(commande);
    }

    public boolean deleteCommande(Integer id) {
        return repository.delete(Commande.class, id);
    }

    public List<Commande> getCommandesParClient(Integer clientId) {
        return repository.findByField(Commande.class, "client.id", clientId);
    }

    public List<Commande> getCommandesParStatut(String statut) {
        return repository.findByField(Commande.class, "statut", statut);
    }

    private void validateCommande(Commande commande) {
        if (commande.getClient() == null || commande.getClient().getId() == null) {
            throw new IllegalArgumentException("Le client est obligatoire");
        }

        if (commande.getSousTotal() == null || commande.getSousTotal().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Le sous-total doit être positif ou nul");
        }

        if (commande.getFraisLivraison() == null || commande.getFraisLivraison().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Les frais de livraison doivent être positifs ou nuls");
        }
    }
}