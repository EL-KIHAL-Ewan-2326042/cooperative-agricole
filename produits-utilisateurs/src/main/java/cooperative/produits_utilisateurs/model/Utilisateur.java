package cooperative.produits_utilisateurs.model;

import java.time.LocalDateTime;

/**
 * Représente un utilisateur du système.
 */
public class Utilisateur {

    private Integer id;
    private String nom;
    private String email;
    private String motDePasse;
    private Role role = Role.client;
    private LocalDateTime dateInscription;

    /**
     * Rôle de l'utilisateur.
     */
    public enum Role {
        client, gestionnaire
    }

    /**
     * Obtient l'identifiant de l'utilisateur.
     * @return l'identifiant de l'utilisateur.
     */
    public Integer getId() { return id; }

    /**
     * Définit l'identifiant de l'utilisateur.
     * @param id l'identifiant de l'utilisateur.
     */
    public void setId(Integer id) { this.id = id; }

    /**
     * Obtient le nom de l'utilisateur.
     * @return le nom de l'utilisateur.
     */
    public String getNom() { return nom; }

    /**
     * Définit le nom de l'utilisateur.
     * @param nom le nom de l'utilisateur.
     */
    public void setNom(String nom) { this.nom = nom; }

    /**
     * Obtient l'email de l'utilisateur.
     * @return l'email de l'utilisateur.
     */
    public String getEmail() { return email; }

    /**
     * Définit l'email de l'utilisateur.
     * @param email l'email de l'utilisateur.
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Obtient le mot de passe de l'utilisateur.
     * @return le mot de passe de l'utilisateur.
     */
    public String getMotDePasse() { return motDePasse; }

    /**
     * Définit le mot de passe de l'utilisateur.
     * @param motDePasse le mot de passe de l'utilisateur.
     */
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    /**
     * Obtient le rôle de l'utilisateur.
     * @return le rôle de l'utilisateur.
     */
    public Role getRole() { return role; }

    /**
     * Définit le rôle de l'utilisateur.
     * @param role le rôle de l'utilisateur.
     */
    public void setRole(Role role) { this.role = role; }

    /**
     * Obtient la date d'inscription de l'utilisateur.
     * @return la date d'inscription de l'utilisateur.
     */
    public LocalDateTime getDateInscription() { return dateInscription; }

    /**
     * Définit la date d'inscription de l'utilisateur.
     * @param dateInscription la date d'inscription de l'utilisateur.
     */
    public void setDateInscription(LocalDateTime dateInscription) { this.dateInscription = dateInscription; }
}
