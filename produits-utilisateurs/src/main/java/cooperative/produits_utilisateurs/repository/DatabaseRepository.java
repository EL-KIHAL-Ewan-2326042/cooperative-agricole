package cooperative.produits_utilisateurs.repository;

import java.sql.Connection;
import java.util.List;

/**
 * Interface pour les opérations de base de données.
 */
public interface DatabaseRepository {

    /**
     * Récupère toutes les entités d'un type donné.
     *
     * @param entityClass la classe de l'entité
     * @param <T> le type de l'entité
     * @return une liste de toutes les entités
     */
    <T> List<T> findAll(Class<T> entityClass);

    /**
     * Récupère une entité par son identifiant.
     *
     * @param entityClass la classe de l'entité
     * @param id l'identifiant de l'entité
     * @param <T> le type de l'entité
     * @return l'entité trouvée ou null si non trouvée
     */
    <T> T findById(Class<T> entityClass, Integer id);

    /**
     * Sauvegarde une entité dans la base de données.
     *
     * @param entity l'entité à sauvegarder
     * @param <T> le type de l'entité
     * @return l'entité sauvegardée
     */
    <T> T save(T entity);

    /**
     * Supprime une entité par son identifiant.
     *
     * @param entityClass la classe de l'entité
     * @param id l'identifiant de l'entité
     * @param <T> le type de l'entité
     * @return true si l'entité a été supprimée, false sinon
     */
    <T> boolean delete(Class<T> entityClass, Integer id);

    /**
     * Récupère les entités par un champ spécifique.
     *
     * @param entityClass la classe de l'entité
     * @param fieldName le nom du champ
     * @param value la valeur du champ
     * @param <T> le type de l'entité
     * @return une liste des entités trouvées
     */
    <T> List<T> findByField(Class<T> entityClass, String fieldName, Object value);

    /**
     * Ferme la connexion à la base de données.
     */
    void close();
}
