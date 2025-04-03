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
            String idColumnName = tableName + "_id";

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
            String tableName = entityClass.getSimpleName().toLowerCase();
            String idColumnName = tableName + "_id";

            // Récupérer l'ID de l'entité
            Integer id = null;
            try {
                Method getIdMethod = entityClass.getMethod("getId");
                id = (Integer) getIdMethod.invoke(entity);
            } catch (Exception e) {
                System.out.println("[DEBUG] Pas de méthode getId ou erreur: " + e.getMessage());
            }

            // Récupérer tous les champs non nuls de l'entité et les mapper vers les colonnes SQL
            Map<String, Object> columnValues = new HashMap<>();

            for (Field field : entityClass.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(entity);
                String fieldName = field.getName();

                if (value != null &&
                        !field.getType().equals(Type.class) &&
                        !field.getType().equals(Unite.class)) {

                    // Transformation des noms de champs Java en noms de colonnes SQL
                    String columnName = fieldName;

                    // Cas particuliers pour les ID
                    columnName = switch (fieldName) {
                        case "produitId" -> "produit_id";
                        case "typeId" -> "type_id";
                        case "uniteId" -> "unite_id";
                        case "dateMiseAJour" -> "date_mise_a_jour";
                        default -> columnName;
                    };

                    columnValues.put(columnName, value);
                }
            }

            StringBuilder query;

            if (id == null) {
                // INSERT
                query = new StringBuilder("INSERT INTO " + tableName + " (");
                StringBuilder placeholders = new StringBuilder();

                int i = 0;
                for (String column : columnValues.keySet()) {
                    query.append(column);
                    placeholders.append("?");

                    if (i < columnValues.size() - 1) {
                        query.append(", ");
                        placeholders.append(", ");
                    }
                    i++;
                }

                query.append(") VALUES (").append(placeholders).append(")");

            } else {
                // UPDATE
                query = new StringBuilder("UPDATE " + tableName + " SET ");

                List<String> updateColumns = new ArrayList<>();
                for (String column : columnValues.keySet()) {
                    if (!column.equals(idColumnName)) {
                        updateColumns.add(column);
                    }
                }

                for (int i = 0; i < updateColumns.size(); ++i) {
                    query.append(updateColumns.get(i)).append(" = ?");
                    if (i < updateColumns.size() - 1) {
                        query.append(", ");
                    }
                }

                query.append(" WHERE ").append(idColumnName).append(" = ?");
            }

            System.out.println("[DEBUG] Requête SQL: " + query);

            try (PreparedStatement pstmt = connection.prepareStatement(query.toString(),
                    Statement.RETURN_GENERATED_KEYS)) {

                int paramIndex = 1;

                if (id == null) {
                    // INSERT params
                    for (Object value : columnValues.values()) {
                        pstmt.setObject(paramIndex++, value);
                    }
                } else {
                    // UPDATE params
                    for (Map.Entry<String, Object> entry : columnValues.entrySet()) {
                        if (!entry.getKey().equals(idColumnName)) {
                            pstmt.setObject(paramIndex++, entry.getValue());
                        }
                    }
                    pstmt.setObject(paramIndex, id);
                }

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    if (id == null) {
                        // Pour un INSERT, récupérer l'ID généré
                        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                int newId = generatedKeys.getInt(1);

                                if (entity instanceof Type) {
                                    ((Type) entity).setTypeId(newId);
                                    ((Type) entity).setId(newId);
                                } else if (entity instanceof Unite) {
                                    ((Unite) entity).setUniteId(newId);
                                    ((Unite) entity).setId(newId);
                                } else if (entity instanceof Produit) {
                                    ((Produit) entity).setProduitId(newId);
                                    ((Produit) entity).setId(newId);
                                }
                            }
                        }
                    }
                    return entity;
                }
            }

        } catch (Exception e) {
            System.out.println("[DEBUG] Erreur dans save: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public <T> boolean delete(Class<T> entityClass, Integer id) {
        try {
            String tableName = entityClass.getSimpleName().toLowerCase();
            String idColumnName = tableName + "_id";
            String query = "DELETE FROM " + tableName + " WHERE " + idColumnName + " = ?";

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

        // Imprime tous les noms de colonnes pour le débogage
        System.out.println("[DEBUG] Colonnes trouvées:");
        for (int i = 1; i <= columnCount; i++) {
            System.out.println("[DEBUG]   " + metaData.getColumnName(i));
        }

        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            Object value = rs.getObject(i);

            if (value != null) {
                // Cas particuliers pour chaque classe
                if (entityClass == Produit.class) {
                    switch (columnName) {
                        case "produit_id" -> {
                            setFieldValue(entity, "produitId", value);
                            continue;
                        }
                        case "type_id" -> {
                            setFieldValue(entity, "typeId", value);
                            continue;
                        }
                        case "unite_id" -> {
                            setFieldValue(entity, "uniteId", value);
                            continue;
                        }
                        case "date_mise_a_jour" -> {
                            setFieldValue(entity, "dateMiseAJour", value);
                            continue;
                        }
                    }
                } else if (entityClass == Type.class) {
                    if (columnName.equals("type_id")) {
                        setFieldValue(entity, "typeId", value);
                        continue;
                    }
                } else if (entityClass == Unite.class) {
                    if (columnName.equals("unite_id")) {
                        setFieldValue(entity, "uniteId", value);
                        continue;
                    }
                }

                // Pour les autres champs, essayer le mappage direct
                try {
                    setFieldValue(entity, columnName, value);
                } catch (Exception e) {
                    System.out.println("[DEBUG] Impossible de définir la valeur pour le champ: " + columnName);
                }
            }
        }

        // Enrichissement des objets Produit avec Type et Unite
        if (entityClass == Produit.class) {
            Produit produit = (Produit) entity;

            // Enrichissement avec Type
            if (produit.getTypeId() != null) {
                Type type = getTypeById(produit.getTypeId());
                if (type != null) {
                    produit.setType(type);
                }
            }

            // Enrichissement avec Unite
            if (produit.getUniteId() != null) {
                Unite unite = getUniteById(produit.getUniteId());
                if (unite != null) {
                    produit.setUnite(unite);
                }
            }
        }

        return entity;
    }

    // Méthode pour récupérer un Type complet avec son id
    private Type getTypeById(Integer id) {
        try {
            String query = "SELECT * FROM type WHERE type_id = ?";
            System.out.println("[DEBUG] Exécution de la requête: " + query + " avec ID=" + id);

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        Type type = new Type();
                        type.setTypeId(rs.getInt("type_id"));
                        String nomType = rs.getString("nom");
                        type.setNom(nomType);
                        System.out.println("[DEBUG] Type chargé : ID=" + id + ", Nom=" + nomType);
                        return type;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[DEBUG] Erreur lors du chargement du type: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("[DEBUG] Type non trouvé pour ID=" + id);
        return null;
    }

    private Unite getUniteById(Integer id) {
        try {
            String query = "SELECT * FROM unite WHERE unite_id = ?";
            System.out.println("[DEBUG] Exécution de la requête: " + query + " avec ID=" + id);

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        Unite unite = new Unite();
                        unite.setUniteId(rs.getInt("unite_id"));
                        unite.setNom(rs.getString("nom"));
                        unite.setSymbole(rs.getString("symbole"));
                        return unite;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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