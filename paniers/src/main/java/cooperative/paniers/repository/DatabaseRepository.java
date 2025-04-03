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

@ApplicationScoped
public class DatabaseRepository {

    private static final String URL = "jdbc:mariadb://mysql-anarchy-acres.alwaysdata.net:3306/anarchy-acres_paniers?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8";
    private static final String USER = "406080";
    private static final String PASSWORD = "jpD73FM)Q-qSwe6";

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

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MariaDB introuvable", e);
        }
    }

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

    public static class MariaDbRepository {
        private final String url;
        private final String username;
        private final String password;

        public MariaDbRepository(String url, String username, String password) {
            this.url = url;
            this.username = username;
            this.password = password;
        }

        public Connection getConnection() throws SQLException {
            return DriverManager.getConnection(url, username, password);
        }
    }
}