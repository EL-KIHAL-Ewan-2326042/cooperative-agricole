package cooperative.produits_utilisateurs.repository;

import cooperative.produits_utilisateurs.model.Produit;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import cooperative.produits_utilisateurs.model.Unite;

@ApplicationScoped
public class ProduitRepository {

    @Inject
    private DatabaseRepository dbRepository;

    public List<Produit> findAll() {
        return dbRepository.findAll(Produit.class);
    }

    public Produit findById(Integer id) {
        return dbRepository.findById(Produit.class, id);
    }

    public Produit save(Produit produit) {
        // Implémentation à adapter selon vos besoins
        return dbRepository.save(produit);
    }

    public void delete(Integer id) {
        dbRepository.delete(Produit.class, id);
    }

    public List<Produit> findByNom(String nom) {
        List<Produit> results = new ArrayList<>();

        try {
            Connection conn = dbRepository.getConnection();
            String query = "SELECT p.*, u.id as unite_id, u.nom as unite_nom, u.symbole as unite_symbole " +
                    "FROM produit p JOIN unite u ON p.unite_id = u.id " +
                    "WHERE LOWER(p.nom) LIKE LOWER(?)";

            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setString(1, "%" + nom + "%");

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Produit produit = new Produit();
                        produit.setId(rs.getInt("id"));
                        produit.setNom(rs.getString("nom"));
                        produit.setType(rs.getString("type"));
                        produit.setPrix(rs.getBigDecimal("prix"));
                        produit.setQuantite(rs.getBigDecimal("quantite"));
                        produit.setDateMiseAJour(rs.getObject("date_mise_a_jour", LocalDateTime.class));

                        Unite unite = new Unite();
                        unite.setId(rs.getInt("unite_id"));
                        unite.setNom(rs.getString("unite_nom"));
                        unite.setSymbole(rs.getString("unite_symbole"));

                        produit.setUnite(unite);
                        results.add(produit);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }
}