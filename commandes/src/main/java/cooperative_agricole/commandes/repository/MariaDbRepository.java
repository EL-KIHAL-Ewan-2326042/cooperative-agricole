package cooperative_agricole.commandes.repository;

import java.sql.*;
import java.lang.reflect.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import cooperative_agricole.commandes.model.*;
import jakarta.persistence.ManyToOne;

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
        String tableName = entityClass.getSimpleName();

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
            String tableName = entityClass.getSimpleName();
            String idColumnName = "id";

            String query = "SELECT * FROM " + tableName + " WHERE " + idColumnName + " = ?";

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
            String tableName = entityClass.getSimpleName();

            // Obtenir l'ID de l'entité
            Field idField = findField(entityClass, "id");
            idField.setAccessible(true);
            Integer id = (Integer) idField.get(entity);

            if (id == null) {
                // Insertion
                return insertEntity(entity, entityClass, tableName);
            } else {
                // Mise à jour
                return updateEntity(entity, entityClass, tableName, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return entity;
    }

    private <T> T insertEntity(T entity, Class<?> entityClass, String tableName) throws Exception {
        // Obtenir tous les champs à insérer
        List<Field> fields = getAllFields(entityClass);
        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        List<Object> values = new ArrayList<>();

        boolean first = true;
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();

            // Ignorer les champs ID générés automatiquement et les collections
            if (fieldName.equals("id") || Collection.class.isAssignableFrom(field.getType())) {
                continue;
            }

            // Traiter les relations OneToMany et ManyToOne
            if (field.isAnnotationPresent(ManyToOne.class)) {
                Object relatedEntity = field.get(entity);
                if (relatedEntity != null) {
                    Field relatedIdField = findField(relatedEntity.getClass(), "id");
                    relatedIdField.setAccessible(true);
                    Object relatedId = relatedIdField.get(relatedEntity);

                    if (!first) {
                        columns.append(", ");
                        placeholders.append(", ");
                    }
                    columns.append(fieldName + "_id");
                    placeholders.append("?");
                    values.add(relatedId);
                    first = false;
                }
            } else if (!Collection.class.isAssignableFrom(field.getType())) {
                Object value = field.get(entity);
                if (value != null) {
                    if (!first) {
                        columns.append(", ");
                        placeholders.append(", ");
                    }
                    columns.append(fieldName);
                    placeholders.append("?");
                    values.add(value);
                    first = false;
                }
            }
        }

        String query = "INSERT INTO " + tableName + " (" + columns.toString() +
                ") VALUES (" + placeholders.toString() + ")";

        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < values.size(); i++) {
                pstmt.setObject(i + 1, values.get(i));
            }

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        Field idField = findField(entityClass, "id");
                        idField.setAccessible(true);
                        idField.set(entity, generatedKeys.getInt(1));
                    }
                }
            }
        }

        return entity;
    }

    private <T> T updateEntity(T entity, Class<?> entityClass, String tableName, Integer id) throws Exception {
        // Obtenir tous les champs à mettre à jour
        List<Field> fields = getAllFields(entityClass);
        StringBuilder setClause = new StringBuilder();
        List<Object> values = new ArrayList<>();

        boolean first = true;
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();

            // Ignorer l'ID et les collections
            if (fieldName.equals("id") || Collection.class.isAssignableFrom(field.getType())) {
                continue;
            }

            // Traiter les relations OneToMany et ManyToOne
            if (field.isAnnotationPresent(ManyToOne.class)) {
                Object relatedEntity = field.get(entity);
                if (relatedEntity != null) {
                    Field relatedIdField = findField(relatedEntity.getClass(), "id");
                    relatedIdField.setAccessible(true);
                    Object relatedId = relatedIdField.get(relatedEntity);

                    if (!first) {
                        setClause.append(", ");
                    }
                    setClause.append(fieldName + "_id = ?");
                    values.add(relatedId);
                    first = false;
                }
            } else if (!Collection.class.isAssignableFrom(field.getType())) {
                Object value = field.get(entity);
                if (value != null) {
                    if (!first) {
                        setClause.append(", ");
                    }
                    setClause.append(fieldName + " = ?");
                    values.add(value);
                    first = false;
                }
            }
        }

        String query = "UPDATE " + tableName + " SET " + setClause.toString() + " WHERE id = ?";
        values.add(id);

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            for (int i = 0; i < values.size(); i++) {
                pstmt.setObject(i + 1, values.get(i));
            }

            pstmt.executeUpdate();
        }

        return entity;
    }

    @Override
    public <T> boolean delete(Class<T> entityClass, Integer id) {
        try {
            String tableName = entityClass.getSimpleName();
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
        String tableName = entityClass.getSimpleName();

        // Gérer les relations (par exemple, client.id)
        String[] parts = fieldName.split("\\.");
        String columnName = parts.length > 1 ? parts[0] + "_id" : fieldName;

        try {
            String query = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setObject(1, value);

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
    public <T> T findByCompositeId(Class<T> entityClass, Object... idParts) {
        return null;
    }

    @Override
    public <T> boolean deleteByCompositeId(Class<T> entityClass, Object... idParts) {
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

    private <T> T mapResultSetToEntity(ResultSet rs, Class<T> entityClass) throws Exception {
        T entity = entityClass.getDeclaredConstructor().newInstance();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            Object value = rs.getObject(i);

            if (value != null) {
                if (columnName.endsWith("_id") && !columnName.equals("id")) {
                    // C'est une clé étrangère
                    String relationFieldName = columnName.substring(0, columnName.length() - 3);
                    Field field = findField(entityClass, relationFieldName);

                    if (field != null) {
                        Class<?> relationClass = field.getType();
                        Object relationEntity = findById(relationClass, (Integer) value);

                        if (relationEntity != null) {
                            field.setAccessible(true);
                            field.set(entity, relationEntity);
                        }
                    }
                } else {
                    // Champ normal
                    setFieldValue(entity, columnName, value);
                }
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

    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        // Ajouter les champs de cette classe
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

        // Ajouter les champs des classes parentes
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && superClass != Object.class) {
            fields.addAll(getAllFields(superClass));
        }

        return fields;
    }
}