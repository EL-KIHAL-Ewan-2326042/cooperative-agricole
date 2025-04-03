package cooperative.paniers.repository;

import cooperative.paniers.model.PanierProduit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

@ApplicationScoped
public class PanierProduitRepository {
    @Inject
    private DatabaseRepository dbRepository;

    public List<PanierProduit> findByUserId(Integer userId) {
        List<PanierProduit> results = new ArrayList<>();
        try (Connection conn = dbRepository.getConnection()) {
            String query = "SELECT * FROM panier_produit WHERE user_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, userId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        PanierProduit item = new PanierProduit();
                        item.setUserId(rs.getInt("user_id"));
                        item.setProduitId(rs.getInt("produit_id"));
                        item.setQuantite(rs.getInt("quantite"));
                        item.setPrixUnitaire(rs.getBigDecimal("prix_unitaire"));
                        results.add(item);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public PanierProduit save(PanierProduit panierProduit) {
        return (PanierProduit) dbRepository.save(panierProduit);
    }

    public void deleteByUserIdAndProduitId(Integer userId, Integer produitId) {
        try (Connection conn = dbRepository.getConnection()) {
            String query = "DELETE FROM panier_produit WHERE user_id = ? AND produit_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, produitId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllByUserId(Integer userId) {
        try (Connection conn = dbRepository.getConnection()) {
            String query = "DELETE FROM panier_produit WHERE user_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, userId);
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}