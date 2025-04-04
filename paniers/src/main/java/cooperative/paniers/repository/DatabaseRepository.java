package cooperative.paniers.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import cooperative.paniers.model.Panier;
import cooperative.paniers.model.PanierProduit;

/**
 * Repository principal gérant les connexions à la base de données MariaDB et les opérations CRUD.
 * Cette classe centralise toute la logique d'accès aux données et sert de point d'entrée
 * pour les interactions avec la base de données.
 */
@ApplicationScoped
public class DatabaseRepository {

    /**
     * URL de connexion à la base de données MariaDB incluant les paramètres de configuration.
     */
    private static final String URL = "jdbc:mariadb://mysql-anarchy-acres.alwaysdata.net:3306/anarchy-acres_paniers?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";

    /**
     * Identifiant utilisateur pour la connexion à la base de données.
     */
    private static final String USER = "406080";

    /**
     * Mot de passe pour la connexion à la base de données.
     */
    private static final String PASSWORD = "jpD73FM)Q-qSwe6";

    /**
     * Produit une instance de MariaDbRepository pour l'injection CDI.
     * Cette méthode permet l'injection de la connexion dans d'autres composants.
     *
     * @return Une instance de MariaDbRepository configurée avec les paramètres de connexion,
     *         ou null en cas d'échec de connexion
     */
    @Produces
    public MariaDbRepository openDbConnection() {
        try {
            return new MariaDbRepository(URL, USER, PASSWORD);
        } catch (Exception e) {
            System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Établit une connexion à la base de données MariaDB.
     * Charge le pilote JDBC et ouvre une connexion avec les paramètres configurés.
     *
     * @return Une connexion active à la base de données
     * @throws SQLException Si une erreur survient lors de la connexion ou si le pilote est introuvable
     */
    public Connection getConnection() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MariaDB introuvable", e);
        }
    }

    /**
     * Recherche une entité par son identifiant dans la base de données.
     * Actuellement implémenté uniquement pour la classe Panier.
     *
     * @param <T> Le type générique de l'entité à rechercher
     * @param entityClass La classe de l'entité à rechercher
     * @param id L'identifiant unique de l'entité
     * @return L'entité trouvée ou null si aucune entité ne correspond à l'identifiant
     */
    @SuppressWarnings("unchecked")
    public <T> T findById(Class<T> entityClass, Integer id) {
        try (Connection conn = getConnection()) {
            if (entityClass.equals(Panier.class)) {
                String query = "SELECT * FROM panier WHERE user_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, id);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            Panier panier = new Panier();
                            panier.setUserId(rs.getInt("user_id"));
                            panier.setDateMiseAJour(rs.getTimestamp("date_mise_a_jour").toLocalDateTime());
                            return (T) panier;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sauvegarde ou met à jour une entité dans la base de données.
     * Utilise la clause SQL "ON DUPLICATE KEY UPDATE" pour gérer à la fois
     * l'insertion et la mise à jour dans une seule opération.
     * Supporte les entités Panier et PanierProduit.
     *
     * @param <T> Le type générique de l'entité à sauvegarder
     * @param entity L'entité à sauvegarder ou mettre à jour
     * @return L'entité sauvegardée
     */
    @SuppressWarnings("unchecked")
    public <T> T save(T entity) {
        try (Connection conn = getConnection()) {
            if (entity instanceof Panier) {
                Panier panier = (Panier) entity;
                String query = "INSERT INTO panier (user_id, date_mise_a_jour) VALUES (?, ?) " +
                        "ON DUPLICATE KEY UPDATE date_mise_a_jour = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, panier.getUserId());
                    pstmt.setTimestamp(2, Timestamp.valueOf(panier.getDateMiseAJour()));
                    pstmt.setTimestamp(3, Timestamp.valueOf(panier.getDateMiseAJour()));
                    pstmt.executeUpdate();
                    return entity;
                }
            } else if (entity instanceof PanierProduit) {
                PanierProduit pp = (PanierProduit) entity;
                String query = "INSERT INTO panier_produit (user_id, produit_id, quantite, prix_unitaire) " +
                        "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE quantite = ?, prix_unitaire = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, pp.getUserId());
                    pstmt.setInt(2, pp.getProduitId());
                    pstmt.setInt(3, pp.getQuantite());
                    pstmt.setBigDecimal(4, pp.getPrixUnitaire());
                    pstmt.setInt(5, pp.getQuantite());
                    pstmt.setBigDecimal(6, pp.getPrixUnitaire());
                    pstmt.executeUpdate();
                    return entity;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * Supprime une entité de la base de données par son identifiant.
     * Détermine dynamiquement le nom de la table et la colonne d'identifiant
     * en fonction de la classe d'entité fournie.
     *
     * @param <T> Le type générique de l'entité à supprimer
     * @param entityClass La classe de l'entité à supprimer
     * @param id L'identifiant unique de l'entité à supprimer
     */
    public <T> void delete(Class<T> entityClass, Integer id) {
        try (Connection conn = getConnection()) {
            String tableName = entityClass.getSimpleName().toLowerCase();
            String idFieldName = "id";

            if (entityClass.equals(Panier.class)) {
                idFieldName = "user_id";
            }

            String query = "DELETE FROM " + tableName + " WHERE " + idFieldName + " = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Classe interne encapsulant les paramètres de connexion à la base de données MariaDB.
     * Cette classe est utilisée pour l'injection de dépendances via CDI.
     */
    public static class MariaDbRepository {
        /**
         * URL de connexion à la base de données.
         */
        private final String url;

        /**
         * Nom d'utilisateur pour l'authentification.
         */
        private final String username;

        /**
         * Mot de passe pour l'authentification.
         */
        private final String password;

        /**
         * Constructeur initialisant les paramètres de connexion.
         *
         * @param url URL de connexion à la base de données MariaDB
         * @param username Nom d'utilisateur pour l'authentification
         * @param password Mot de passe pour l'authentification
         */
        public MariaDbRepository(String url, String username, String password) {
            this.url = url;
            this.username = username;
            this.password = password;
        }

        /**
         * Établit une connexion à la base de données avec les paramètres configurés.
         *
         * @return Une connexion active à la base de données
         * @throws SQLException Si une erreur survient lors de la connexion
         */
        public Connection getConnection() throws SQLException {
            return DriverManager.getConnection(url, username, password);
        }
    }
}