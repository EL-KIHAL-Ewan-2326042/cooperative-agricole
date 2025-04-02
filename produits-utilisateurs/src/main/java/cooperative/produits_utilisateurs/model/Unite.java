package cooperative.produits_utilisateurs.model;

public class Unite {
    private Integer uniteId;
    private String nom;
    private String symbole;

    public Integer getId() { return uniteId; }
    public void setId(Integer id) { this.uniteId = id; }

    public Integer getUniteId() { return uniteId; }
    public void setUniteId(Integer uniteId) { this.uniteId = uniteId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getSymbole() { return symbole; }
    public void setSymbole(String symbole) { this.symbole = symbole; }
}