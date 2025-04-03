package cooperative_agricole.commandes.service;

import cooperative_agricole.commandes.model.Client;
import cooperative_agricole.commandes.repository.DatabaseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ClientService {

    @Inject
    private DatabaseRepository repository;

    public List<Client> getAllClients() {
        return repository.findAll(Client.class);
    }

    public Client getClientById(Integer id) {
        return repository.findById(Client.class, id);
    }

    public Client createClient(Client client) {
        // Validation
        validateClient(client);

        // Vérifier que l'email n'est pas déjà utilisé
        List<Client> clientsExistants = repository.findByField(Client.class, "email", client.getEmail());
        if (!clientsExistants.isEmpty()) {
            throw new IllegalArgumentException("Un client avec cet email existe déjà");
        }

        return repository.save(client);
    }

    public Client updateClient(Client client) {
        // Validation
        validateClient(client);

        // Vérifier que l'email n'est pas déjà utilisé par un autre client
        List<Client> clientsExistants = repository.findByField(Client.class, "email", client.getEmail());
        if (!clientsExistants.isEmpty() && !clientsExistants.get(0).getId().equals(client.getId())) {
            throw new IllegalArgumentException("Un autre client avec cet email existe déjà");
        }

        return repository.save(client);
    }

    public Client getClientByEmail(String email) {
        List<Client> clients = repository.findByField(Client.class, "email", email);
        return clients.isEmpty() ? null : clients.get(0);
    }

    private void validateClient(Client client) {
        if (client.getNom() == null || client.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }

        if (client.getPrenom() == null || client.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom est obligatoire");
        }

        if (client.getEmail() == null || client.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'email est obligatoire");
        }

        // Validation simple de l'email
        if (!client.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Format d'email invalide");
        }
    }
}