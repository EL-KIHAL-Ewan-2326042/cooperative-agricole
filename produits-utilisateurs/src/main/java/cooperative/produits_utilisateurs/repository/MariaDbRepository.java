package cooperative.produits_utilisateurs.repository;

import java.sql.*;
import java.lang.reflect.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import cooperative.produits_utilisateurs.model.*;

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
        connection.setAutoCommit(true);
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
        try {
            Class<?> entityClass = entity.getClass();
            String tableName = entityClass.getSimpleName().toLowerCase();
            Integer id = getEntityId(entity);

            if (id == null || id == 0) {
                // INSERT
                return insertEntity(entity, tableName);
            } else {
                // UPDATE
                return updateEntity(entity, tableName, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return entity;
        }
    }

    private <T> T updateEntity(T entity, String tableName, Integer id) throws Exception {
        Map<String, Object> fields = getEntityFields(entity);
        fields.remove("id"); // L'ID ne doit pas être modifié

        StringBuilder setClause = new StringBuilder();
        List<Object> values = new ArrayList<>();

        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            if (values.size() > 0) {
                setClause.append(", ");
            }
            setClause.append(entry.getKey()).append(" = ?");
            values.add(entry.getValue());
        }

        // Ajouter l'ID pour la clause WHERE
        values.add(id);

        String sql = "UPDATE " + tableName + " SET " + setClause + " WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("La mise à jour a échoué, aucune ligne affectée.");
            }
        }

        return entity;
    }

    private <T> Integer getEntityId(T entity) {
        try {
            Field idField = findField(entity.getClass(), "id");
            if (idField != null) {
                idField.setAccessible(true);
                return (Integer) idField.get(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T> T insertEntity(T entity, String tableName) throws Exception {
        Map<String, Object> fields = getEntityFields(entity);
        fields.remove("id"); // Ne pas inclure l'ID dans l'insertion

        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        List<Object> values = new ArrayList<>();

        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            if (values.size() > 0) {
                columns.append(", ");
                placeholders.append(", ");
            }
            columns.append(entry.getKey());
            placeholders.append("?");
            values.add(entry.getValue());
        }

        String sql = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("La création a échoué, aucune ligne affectée.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // Définir l'ID généré
                    Field idField = findField(entity.getClass(), "id");
                    if (idField != null) {
                        idField.setAccessible(true);
                        idField.set(entity, generatedKeys.getInt(1));
                    }
                }
            }
        }

        return entity;
    }

    private <T> Map<String, Object> getEntityFields(T entity) throws Exception {
        Map<String, Object> fields = new HashMap<>();
        Class<?> clazz = entity.getClass();

        String tableName = clazz.getSimpleName().toLowerCase();
        System.out.println("DEBUG - Table: " + tableName + ", Entity: " + entity);

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object fieldValue = field.get(entity);

            if (fieldValue != null) {
                if (fieldValue instanceof Unite) {
                    // Cas spécial pour les relations
                    Unite unite = (Unite) fieldValue;
                    fields.put("unite_id", unite.getId());
                    System.out.println("DEBUG - Field: unite_id, Value: " + unite.getId());
                } else {
                    fields.put(fieldName, fieldValue);
                }
            }
        }

        return fields;
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
                setFieldValue(entity, columnName, value);
            }
        }

        if (entity instanceof Produit && ((Produit)entity).getTypeId() != null) {
            loadTypeName((Produit)entity);
        }

        return entity;
    }

    private void loadTypeName(Produit produit) {
        try {
            TypeProduit typeProduit = findById(TypeProduit.class, produit.getTypeId());
            if (typeProduit != null) {
                produit.setTypeName(typeProduit.getNom());
                produit.setType(typeProduit);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private <T> void setFieldValue(T entity, String columnName, Object value) {
        try {
            // Conversion snake_case vers camelCase
            String fieldName = columnName;
            if (columnName.contains("_")) {
                fieldName = convertToCamelCase(columnName);
            }

            // Cas spéciaux
            if (columnName.equals("unite_id") && entity instanceof Produit) {
                Produit produit = (Produit) entity;
                Unite unite = findById(Unite.class, (Integer) value);
                produit.setUnite(unite);
                return;
            } else if (columnName.equals("type_id") && entity instanceof Produit) {
                ((Produit) entity).setTypeId((Integer) value);
                return;
            }

            Field field = findField(entity.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                field.set(entity, convertValueToFieldType(field.getType(), value));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertToCamelCase(String snakeCase) {
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;

        for (char c : snakeCase.toCharArray()) {
            if (c == '_') {
                nextUpper = true;
            } else {
                result.append(nextUpper ? Character.toUpperCase(c) : c);
                nextUpper = false;
            }
        }

        return result.toString();
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