package cooperative_agricole.commandes.commandes;

import cooperative_agricole.commandes.repository.DatabaseRepository;
import cooperative_agricole.commandes.repository.MariaDbRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
@ApplicationScoped
public class CommandesApplication extends Application {

    @Produces
    @ApplicationScoped
    public DatabaseRepository openDbConnection() {
        try {
            return new MariaDbRepository(
                    "jdbc:mariadb://mysql-anarchy-acres.alwaysdata.net:3306/anarchy-acres_commandes?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8",
                    "406080",
                    "jpD73FM)Q-qSwe6"
            );
        } catch (Exception e) {
            System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void closeDbConnection(@Disposes DatabaseRepository repository) {
        repository.close();
    }
}