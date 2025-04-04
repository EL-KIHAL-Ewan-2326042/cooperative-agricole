package cooperative.paniers.repository;

import cooperative.paniers.model.Panier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Repository responsable de la gestion des paniers utilisateurs.
 * Cette classe assure l'accès aux données des paniers dans la base de données
 * et délègue les opérations techniques au DatabaseRepository.
 */
@ApplicationScoped
public class PanierRepository {

    /**
     * Repository principal de base de données injecté qui fournit
     * les fonctionnalités de connexion et les opérations JDBC génériques.
     */
    @Inject
    private DatabaseRepository dbRepository;

    /**
     * Recherche un panier par l'identifiant de son propriétaire.
     *
     * @param userId L'identifiant de l'utilisateur propriétaire du panier
     * @return Le panier correspondant à l'utilisateur, ou null si aucun panier n'existe
     */
    public Panier findByUserId(Integer userId) {
        return dbRepository.findById(Panier.class, userId);
    }

    /**
     * Sauvegarde ou met à jour un panier dans la base de données.
     * Délègue l'opération de persistance au DatabaseRepository sous-jacent.
     *
     * @param panier L'objet Panier à sauvegarder
     * @return L'objet Panier sauvegardé, avec d'éventuelles modifications
     */
    public Panier save(Panier panier) {
        return dbRepository.save(panier);
    }

    /**
     * Supprime un panier de la base de données.
     *
     * @param userId L'identifiant de l'utilisateur dont le panier doit être supprimé
     */
    public void delete(Integer userId) {
        dbRepository.delete(Panier.class, userId);
    }
}