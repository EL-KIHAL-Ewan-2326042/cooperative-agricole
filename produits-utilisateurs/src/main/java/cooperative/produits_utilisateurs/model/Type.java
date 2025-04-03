package cooperative.produits_utilisateurs.model;

/**
 * Représente un type de produit.
 */
public class Type {
    private Integer typeId;
    private String nom;

    /**
     * Obtient l'identifiant du type.
     * @return l'identifiant du type.
     */
    public Integer getId() { return typeId; }

    /**
     * Définit l'identifiant du type.
     * @param id l'identifiant du type.
     */
    public void setId(Integer id) { this.typeId = id; }

    /**
     * Obtient l'identifiant du type.
     * @return l'identifiant du type.
     */
    public Integer getTypeId() { return typeId; }

    /**
     * Définit l'identifiant du type.
     * @param typeId l'identifiant du type.
     */
    public void setTypeId(Integer typeId) { this.typeId = typeId; }

    /**
     * Obtient le nom du type.
     * @return le nom du type.
     */
    public String getNom() { return nom; }

    /**
     * Définit le nom du type.
     * @param nom le nom du type.
     */
    public void setNom(String nom) { this.nom = nom; }
}
