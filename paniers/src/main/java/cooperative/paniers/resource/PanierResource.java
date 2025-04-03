package cooperative.paniers.resource;

import cooperative.paniers.model.Panier;
import cooperative.paniers.model.PanierProduit;
import cooperative.paniers.services.PanierService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/paniers")
@Produces(MediaType.APPLICATION_JSON)
public class PanierResource {
    @Inject
    private PanierService panierService;

    @GET
    @Path("/{userId}")
    public Response getPanier(@PathParam("userId") Integer userId) {
        Panier panier = panierService.getPanierByUserId(userId);
        return Response.ok(panier).build();
    }

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

    @DELETE
    @Path("/{userId}/produits/{produitId}")
    public Response supprimerProduit(
            @PathParam("userId") Integer userId,
            @PathParam("produitId") Integer produitId) {

        boolean success = panierService.supprimerProduit(userId, produitId);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{userId}")
    public Response viderPanier(@PathParam("userId") Integer userId) {
        boolean success = panierService.viderPanier(userId);
        return Response.noContent().build();
    }
}