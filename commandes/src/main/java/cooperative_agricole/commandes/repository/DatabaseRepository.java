package cooperative_agricole.commandes.repository;

import java.sql.Connection;
import java.util.List;

public interface DatabaseRepository {
    <T> List<T> findAll(Class<T> entityClass);
    <T> T findById(Class<T> entityClass, Integer id);
    <T> T save(T entity);
    <T> boolean delete(Class<T> entityClass, Integer id);
    <T> List<T> findByField(Class<T> entityClass, String fieldName, Object value);
    <T> T findByCompositeId(Class<T> entityClass, Object... idParts);
    <T> boolean deleteByCompositeId(Class<T> entityClass, Object... idParts);
    Connection getConnection();
    void close();
}