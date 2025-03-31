package cooperative.produits_utilisateurs.repository;

import java.sql.*;
import java.lang.reflect.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import cooperative.produits_utilisateurs.model.*;

public class MariaDbRepository implements DatabaseRepository {
    private Connection connection;
    
    public MariaDbRepository(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
    }
    
    @Override
    public <T> List<T> findAll(Class<T> entityClass) {
        List<T> results = new ArrayList<>();
        
        try {
            String tableName = entityClass.getSimpleName().toLowerCase();
            String query = "SELECT * FROM " + tableName;
            
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                
                while (rs.next()) {
                    results.add(mapResultSetToEntity(rs, entityClass));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return results;
    }
    
    @Override
    public <T> T findById(Class<T> entityClass, Integer id) {
        try {
            String tableName = entityClass.getSimpleName().toLowerCase();
            String query = "SELECT * FROM " + tableName + " WHERE id = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, id);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return mapResultSetToEntity(rs, entityClass);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    @Override
    public <T> T save(T entity) {
        // Implémentation simplifiée - à adapter selon les besoins
        return entity;
    }
    
    @Override
    public <T> boolean delete(Class<T> entityClass, Integer id) {
        try {
            String tableName = entityClass.getSimpleName().toLowerCase();
            String query = "DELETE FROM " + tableName + " WHERE id = ?";
            
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, id);
                int rowsAffected = pstmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    @Override
    public Connection getConnection() {
        return connection;
    }
    
    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Méthode helper pour mapper un ResultSet à une entité
    private <T> T mapResultSetToEntity(ResultSet rs, Class<T> entityClass) throws Exception {
        T entity = entityClass.getDeclaredConstructor().newInstance();
        
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            Object value = rs.getObject(i);
            
            if (value != null) {
                setFieldValue(entity, columnName, value);
            }
        }
        
        return entity;
    }
    
    private <T> void setFieldValue(T entity, String fieldName, Object value) {
        try {
            Field field = findField(entity.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                
                // Convertir le type si nécessaire
                if (field.getType() == BigDecimal.class && value instanceof Number) {
                    field.set(entity, new BigDecimal(value.toString()));
                } else if (field.getType() == LocalDateTime.class && value instanceof Timestamp) {
                    field.set(entity, ((Timestamp) value).toLocalDateTime());
                } else {
                    field.set(entity, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Field findField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() != null) {
                return findField(clazz.getSuperclass(), fieldName);
            }
            return null;
        }
    }
}