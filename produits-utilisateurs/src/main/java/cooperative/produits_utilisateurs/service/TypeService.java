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

    public List<Type> getAllTypes() {
        return typeRepository.findAll();
    }

    public Type getTypeById(Integer id) {
        return typeRepository.findById(id);
    }

    public Type createType(Type type) {
        return typeRepository.save(type);
    }

    public Type updateType(Integer id, Type type) {
        Type existingType = typeRepository.findById(id);
        if (existingType == null) {
            return null;
        }

        type.setTypeId(id);
        return typeRepository.save(type);
    }

    public boolean deleteType(Integer id) {
        Type type = typeRepository.findById(id);
        if (type == null) {
            return false;
        }

        typeRepository.delete(id);
        return true;
    }
}