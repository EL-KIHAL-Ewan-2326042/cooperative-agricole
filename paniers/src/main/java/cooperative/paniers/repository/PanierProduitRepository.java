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

/**
 * Repository gérant les opérations de persistance pour les produits contenus dans les paniers.
 * Cette classe est responsable de toutes les interactions avec la table panier_produit
 * de la base de données.
 */
@ApplicationScoped
public class PanierProduitRepository {

    /**
     * Repository de base de données principal injecté pour gérer les connexions
     * et les opérations JDBC de bas niveau.
     */
    @Inject
    private DatabaseRepository dbRepository;

    /**
     * Récupère tous les produits présents dans le panier d'un utilisateur spécifique.
     *
     * @param userId L'identifiant de l'utilisateur propriétaire du panier
     * @return Une liste de tous les produits dans le panier de l'utilisateur,
     *         ou une liste vide si aucun produit trouvé
     */
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

    /**
     * Sauvegarde ou met à jour un produit dans un panier.
     * Délègue l'opération au DatabaseRepository sous-jacent.
     *
     * @param panierProduit L'objet PanierProduit à sauvegarder
     * @return L'objet PanierProduit sauvegardé, avec d'éventuelles modifications
     */
    public PanierProduit save(PanierProduit panierProduit) {
        return (PanierProduit) dbRepository.save(panierProduit);
    }

    /**
     * Supprime un produit spécifique du panier d'un utilisateur.
     *
     * @param userId L'identifiant de l'utilisateur propriétaire du panier
     * @param produitId L'identifiant du produit à supprimer du panier
     */
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

    /**
     * Supprime tous les produits du panier d'un utilisateur spécifique.
     * Cette méthode est utilisée pour vider complètement un panier.
     *
     * @param userId L'identifiant de l'utilisateur dont le panier doit être vidé
     */
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