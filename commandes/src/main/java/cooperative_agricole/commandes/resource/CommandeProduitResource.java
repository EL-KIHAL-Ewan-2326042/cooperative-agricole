package cooperative_agricole.commandes.resource;

import cooperative_agricole.commandes.model.CommandeProduit;
import cooperative_agricole.commandes.service.CommandeProduitService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;

@Path("/commandes/{commandeId}/produits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommandeProduitResource {

    @Inject
    private CommandeProduitService commandeProduitService;

    @GET
    public Response getProduitsCommande(@PathParam("commandeId") Integer commandeId) {
        List<CommandeProduit> produits = commandeProduitService.getProduitsCommande(commandeId);
        return Response.ok(produits).build();
    }

    @POST
    public Response ajouterProduitCommande(
            @PathParam("commandeId") Integer commandeId,
            CommandeProduit commandeProduit) {
        try {
            commandeProduit.getCommande().setId(commandeId);
            CommandeProduit nouveauProduit = commandeProduitService.ajouterProduitCommande(commandeProduit);
            return Response.status(Status.CREATED)
                    .entity(nouveauProduit).build();
        } catch (Exception e) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    @PUT
    @Path("/{produitId}")
    public Response modifierQuantiteProduit(
            @PathParam("commandeId") Integer commandeId,
            @PathParam("produitId") Integer produitId,
            @QueryParam("quantite") Integer quantite) {
        try {
            CommandeProduit commandeProduit = commandeProduitService.modifierQuantiteProduit(commandeId, produitId, quantite);
            return Response.ok(commandeProduit).build();
        } catch (Exception e) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    @DELETE
    @Path("/{produitId}")
    public Response supprimerProduitCommande(
            @PathParam("commandeId") Integer commandeId,
            @PathParam("produitId") Integer produitId) {
        try {
            commandeProduitService.supprimerProduitCommande(commandeId, produitId);
            return Response.status(Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Status.NOT_FOUND)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
}