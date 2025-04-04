package cooperative.paniers.resource;

import cooperative.paniers.model.Panier;
import cooperative.paniers.model.PanierProduit;
import cooperative.paniers.services.PanierService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Ressource REST exposant les API de gestion des paniers utilisateurs.
 * Cette classe définit les endpoints permettant de manipuler les paniers
 * et leurs produits via HTTP.
 */
@Path("/paniers")
@Produces(MediaType.APPLICATION_JSON)
public class PanierResource {
    /**
     * Service métier injecté qui contient la logique de gestion des paniers.
     */
    @Inject
    private PanierService panierService;

    /**
     * Récupère le panier d'un utilisateur identifié par son ID.
     * Si le panier n'existe pas, un nouveau panier vide est créé.
     *
     * @param userId L'identifiant de l'utilisateur propriétaire du panier
     * @return Une réponse HTTP contenant le panier avec ses produits au format JSON
     */
    @GET
    @Path("/{userId}")
    public Response getPanier(@PathParam("userId") Integer userId) {
        Panier panier = panierService.getPanierByUserId(userId);
        return Response.ok(panier).build();
    }

    /**
     * Ajoute un produit au panier d'un utilisateur ou met à jour sa quantité.
     *
     * @param userId L'identifiant de l'utilisateur propriétaire du panier
     * @param produitId L'identifiant du produit à ajouter au panier
     * @param quantite La quantité du produit à ajouter
     * @return Une réponse HTTP avec le produit ajouté (201 Created) ou une erreur si les paramètres
     *         sont invalides (400 Bad Request) ou si le produit n'existe pas (404 Not Found)
     */
    @POST
    @Path("/{userId}/produits")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response ajouterProduit(
            @PathParam("userId") Integer userId,
            @QueryParam("produitId") Integer produitId,
            @QueryParam("quantite") Integer quantite) {

        if (produitId == null || quantite == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Les paramètres produitId et quantite sont obligatoires")
                    .build();
        }

        PanierProduit result = panierService.ajouterProduit(userId, produitId, quantite);
        if (result == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Le produit demandé n'existe pas")
                    .build();
        }

        return Response.status(Response.Status.CREATED).entity(result).build();
    }

    /**
     * Supprime un produit spécifique du panier d'un utilisateur.
     *
     * @param userId L'identifiant de l'utilisateur propriétaire du panier
     * @param produitId L'identifiant du produit à supprimer du panier
     * @return Une réponse HTTP sans contenu (204 No Content) indiquant que la suppression a été effectuée
     */
    @DELETE
    @Path("/{userId}/produits/{produitId}")
    public Response supprimerProduit(
            @PathParam("userId") Integer userId,
            @PathParam("produitId") Integer produitId) {

        boolean success = panierService.supprimerProduit(userId, produitId);
        return Response.noContent().build();
    }

    /**
     * Vide complètement le panier d'un utilisateur en supprimant tous les produits.
     *
     * @param userId L'identifiant de l'utilisateur dont le panier doit être vidé
     * @return Une réponse HTTP sans contenu (204 No Content) indiquant que le panier a été vidé
     */
    @DELETE
    @Path("/{userId}")
    public Response viderPanier(@PathParam("userId") Integer userId) {
        boolean success = panierService.viderPanier(userId);
        return Response.noContent().build();
    }
}