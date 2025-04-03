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
    
    /**
     * Récupère toutes les unités.
     *
     * @return La liste de toutes les unités.
     */
    public List<Unite> getAllUnites() {
        return dbRepository.findAll(Unite.class);
    }
    
    /**
     * Récupère une unité par son identifiant.
     *
     * @param id L'identifiant de l'unité.
     * @return L'unité correspondante, ou null si elle n'existe pas.
     */
    public Unite getUniteById(Integer id) {
        return dbRepository.findById(Unite.class