package cooperative.produits_utilisateurs.model;

/**
 * Représente une unité de mesure.
 */
public class Unite {
    private Integer uniteId;
    private String nom;
    private String symbole;

    /**
     * Obtient l'identifiant de l'unité.
     * @return l'identifiant de l'unité.
     */
    public Integer getId() { return uniteId; }

    /**
     * Définit l'identifiant de l'unité.
     * @param id l'identifiant de l'unité.
     */
    public void setId(Integer id) { this.uniteId = id; }

    /**
     * Obtient l'identifiant de l'unité.
     * @return l'identifiant de l'unité.
     */
    public Integer getUniteId() { return uniteId; }

    /**
     * Définit l'identifiant de l'unité.
     * @param uniteId l'identifiant de l'unité.
     */
    public void setUniteId(Integer uniteId) { this.uniteId = uniteId; }

    /**
     * Obtient le nom de l'unité.
     * @return le nom de l'unité.
     */
    public String getNom() { return nom; }

    /**
     * Définit le nom de l'unité.
     * @param nom le nom de l'unité.
     */
    public void setNom(String nom) { this.nom = nom; }

    /**
     * Obtient le symbole de l'unité.
     * @return le symbole de l'unité.
     */
    public String getSymbole() { return symbole; }

    /**
     * Définit le symbole de l'unité.
     * @param symbole le symbole de l'unité.
     */
    public void setSymbole(String symbole) { this.symbole = symbole; }
}
