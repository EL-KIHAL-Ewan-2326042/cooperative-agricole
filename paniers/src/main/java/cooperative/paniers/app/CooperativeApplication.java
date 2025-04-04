package cooperative.paniers.app;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Point d'entrée principal de l'application JAX-RS pour l'API de la coopérative agricole.
 * Cette classe configure le chemin de base pour tous les endpoints REST
 * et sert de conteneur pour les ressources et fournisseurs.
 */
@ApplicationPath("/api")
public class CooperativeApplication extends Application {
    /**
     * Constructeur par défaut.
     * Aucune configuration supplémentaire n'est nécessaire car l'application
     * utilise la découverte automatique des ressources REST.
     */
}