package cooperative.produits_utilisateurs.service;

import cooperative.produits_utilisateurs.model.Type;
import cooperative.produits_utilisateurs.repository.TypeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TypeService {

    @Inject
    private TypeRepository typeRepository;

    /**
     * Récupère tous les types.
     *
     * @return La liste de tous les types.
     */
    public List<Type> getAllTypes() {
        return typeRepository.findAll();
    }

    /**
     * Récupère un type par son identifiant.
     *
     * @param id L'identifiant du type.
     * @return Le type correspondant, ou null s'il n'existe pas.
     */
    public Type getTypeById(Integer id) {
        return typeRepository.findById(id);
    }

    /**
     * Crée un nouveau type.
     *
     * @param type Le type à créer.
     * @return Le type créé.
     */
    public Type createType(Type type) {
        return typeRepository.save(type);
    }

    /**
     * Met à jour un type existant.
     *
     * @param id L'identifiant du type à mettre à jour.
     * @param type Les nouvelles informations du type.
     * @return Le type mis à jour.
     */
    public Type updateType(Integer id, Type type) {

        type.setId(id);
        type.setTypeId(id);

        Type updatedType = typeRepository.save(type);

        return updatedType;
    }

    /**
     * Supprime un type par son identifiant.
     *
     * @param id L'identifiant du type à supprimer.
     * @return true si le type a été supprimé, sinon false.
     */
    public boolean deleteType(Integer id) {
        Type type = typeRepository.findById(id);
        if (type == null) {
            return false;
        }

        typeRepository.delete(id);
        return true;
    }
}
