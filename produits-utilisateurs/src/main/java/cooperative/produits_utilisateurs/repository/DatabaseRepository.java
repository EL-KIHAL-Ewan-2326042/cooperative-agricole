package cooperative.produits_utilisateurs.repository;

import java.sql.Connection;
import java.util.List;

public interface DatabaseRepository {
    <T> List<T> findAll(Class<T> entityClass);
    <T> T findById(Class<T> entityClass, Integer id);
    <T> T save(T entity);
    <T> boolean delete(Class<T> entityClass, Integer id);
    <T> List<T> findByField(Class<T> entityClass, String fieldName, Object value);
    Connection getConnection();
    void close();
}