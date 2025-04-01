package cooperative.produits_utilisateurs.service;

import cooperative.produits_utilisateurs.model.Unite;
import cooperative.produits_utilisateurs.repository.DatabaseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class UniteService {
    @Inject
    private DatabaseRepository dbRepository;
    
    public List<Unite> getAllUnites() {
        return dbRepository.findAll(Unite.class);
    }
    
    public Unite getUniteById(Integer id) {
        return dbRepository.findById(Unite.class, id);
    }
}