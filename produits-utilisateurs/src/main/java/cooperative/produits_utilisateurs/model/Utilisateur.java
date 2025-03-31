package cooperative.produits_utilisateurs.model;

import java.time.LocalDateTime;

public class Utilisateur {

    private Integer id;
    
    private String nom;
    
    private String email;
    
    private String motDePasse;
    
    private Role role = Role.client;
    
    private LocalDateTime dateInscription;
    
    public enum Role {
        client, gestionnaire
    }
    
    // Getters et setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    public LocalDateTime getDateInscription() { return dateInscription; }
    public void setDateInscription(LocalDateTime dateInscription) { this.dateInscription = dateInscription; }
}