package cooperative.produits_utilisateurs.repository;

import java.sql.*;
import java.lang.reflect.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import cooperative.produits_utilisateurs.model.*;
import cooperative.produits_utilisateurs.model.Type;

public class MariaDbRepository implements DatabaseRepository {
    private Connection connection;

    static {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur de chargement du driver MariaDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public MariaDbRepository(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
    }

    @Override
    public <T> List<T> findAll(Class<T> entityClass) {
        List<T> results = new ArrayList<>();
        String tableName = entityClass.getSimpleName().toLowerCase();

        try {
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
        // Implémentation à adapter selon vos besoins
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
    public <T> List<T> findByField(Class<T> entityClass, String fieldName, Object value) {
        List<T> results = new ArrayList<>();
        String tableName = entityClass.getSimpleName().toLowerCase();

        try {
            String query = "SELECT * FROM " + tableName + " WHERE " + fieldName + " LIKE ?";

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setString(1, "%" + value.toString() + "%");

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        results.add(mapResultSetToEntity(rs, entityClass));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
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

    private <T> T mapResultSetToEntity(ResultSet rs, Class<T> entityClass) throws Exception {
        T entity = entityClass.getDeclaredConstructor().newInstance();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            Object value = rs.getObject(i);

            if (value != null) {
                // Gestion spécifique pour les relations type_id et unite_id
                if (entityClass == Produit.class) {
                    if (columnName.equals("type_id")) {
                        Type type = new Type();
                        type.setId((Integer) value);
                        setFieldValue(entity, "type", type);
                        continue;
                    } else if (columnName.equals("unite_id")) {
                        Unite unite = new Unite();
                        unite.setId((Integer) value);
                        setFieldValue(entity, "unite", unite);
                        continue;
                    }
                }

                // Traitement standard pour les autres champs
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
                field.set(entity, convertValueToFieldType(field.getType(), value));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object convertValueToFieldType(Class<?> fieldType, Object value) {
        if (value == null) return null;

        if (fieldType == BigDecimal.class && value instanceof Number) {
            return new BigDecimal(value.toString());
        } else if (fieldType == LocalDateTime.class && value instanceof Timestamp) {
            return ((Timestamp) value).toLocalDateTime();
        }

        return value;
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