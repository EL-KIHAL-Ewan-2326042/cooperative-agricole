package cooperative_agricole.commandes.repository;

import cooperative_agricole.commandes.model.Client;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class ClientRepository {

    @Inject
    private DatabaseRepository repository;

    public List<Client> findAll() {
        return repository.findAll(Client.class);
    }

    public Client findById(Integer id) {
        return repository.findById(Client.class, id);
    }

    public Client save(Client client) {
        return repository.save(client);
    }

    public boolean delete(Integer id) {
        return repository.delete(Client.class, id);
    }

    public List<Client> findByEmail(String email) {
        return repository.findByField(Client.class, "email", email);
    }
}