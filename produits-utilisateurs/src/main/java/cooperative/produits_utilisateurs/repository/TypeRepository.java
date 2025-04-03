package cooperative.produits_utilisateurs.repository;

import cooperative.produits_utilisateurs.model.Type;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Repository pour les opérations CRUD sur les types.
 */
@ApplicationScoped
public class TypeRepository {

    @Inject
    private DatabaseRepository dbRepository;

    /**
     * Récupère tous les types.
     *
     * @return une liste de tous les types
     */
    public List<Type> findAll() {
        return dbRepository.findAll(Type.class);
    }

    /**
     * Récupère un type par son identifiant.
     *
     * @param id l'identifiant du type
     * @return le type trouvé ou null si non trouvé
     */
    public Type findById(Integer id) {
        return dbRepository.findById(Type.class, id);
    }

    /**
     * Sauvegarde un type dans la base de données.
     *
     * @param type le type à sauvegarder
     * @return le type sauvegardé
     */
    public Type save(Type type) {
        return dbRepository.save(type);
    }

    /**
     * Supprime un type par son identifiant.
     *
     * @param id l'identifiant du type
     */
    public void delete(Integer id) {
        dbRepository.delete(Type.class, id);
    }
}
