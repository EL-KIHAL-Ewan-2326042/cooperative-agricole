package cooperative.produits_utilisateurs.model;

public class Type {
    private Integer typeId;
    private String nom;

    public Integer getId() { return typeId; }
    public void setId(Integer id) { this.typeId = id; }

    public Integer getTypeId() { return typeId; }
    public void setTypeId(Integer typeId) { this.typeId = typeId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}