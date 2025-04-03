package cooperative.produits_utilisateurs.produitsutilisateurs;

import cooperative.produits_utilisateurs.repository.DatabaseRepository;
import cooperative.produits_utilisateurs.repository.MariaDbRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Classe principale de l'application coopérative.
 */
@ApplicationPath("/api")
@ApplicationScoped
public class CooperativeApplication extends Application {

    /**
     * Ouvre une connexion à la base de données.
     * @return une instance de {@link DatabaseRepository}.
     */
    @Produces
    @ApplicationScoped
    public DatabaseRepository openDbConnection() {
        try {
            return new MariaDbRepository(
                "jdbc:mariadb://mysql-anarchy-acres.alwaysdata.net:3306/anarchy-acres_produits_utilisateurs?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8",
                "406080",
                "jpD73FM)Q-qSwe6"
            );
        } catch (Exception e) {
            System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Ferme la connexion à la base de données.
     * @param repository l'instance de {@link DatabaseRepository} à fermer.
     */
    public void closeDbConnection(@Disposes DatabaseRepository repository) {
        repository.close();
    }
}
