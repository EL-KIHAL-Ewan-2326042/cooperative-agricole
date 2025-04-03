package cooperative_agricole.commandes.repository;

import cooperative_agricole.commandes.model.Commande;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class CommandeRepository {

    @Inject
    private DatabaseRepository repository;

    public List<Commande> findAll() {
        return repository.findAll(Commande.class);
    }

    public Commande findById(Integer id) {
        return repository.findById(Commande.class, id);
    }

    public Commande save(Commande commande) {
        return repository.save(commande);
    }

    public boolean delete(Integer id) {
        return repository.delete(Commande.class, id);
    }

    public List<Commande> findByClientId(Integer clientId) {
        return repository.findByField(Commande.class, "client.id", clientId);
    }

    public List<Commande> findByStatut(String statut) {
        return repository.findByField(Commande.class, "statut", statut);
    }
}