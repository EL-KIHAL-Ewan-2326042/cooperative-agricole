package cooperative_agricole.commandes.resource;

import cooperative_agricole.commandes.model.Commande;
import cooperative_agricole.commandes.service.CommandeService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;

@Path("/commandes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommandeResource {

    @Inject
    private CommandeService commandeService;

    @GET
    public Response getAllCommandes() {
        List<Commande> commandes = commandeService.getAllCommandes();
        return Response.ok(commandes).build();
    }

    @GET
    @Path("/{id}")
    public Response getCommandeById(@PathParam("id") Integer id) {
        Commande commande = commandeService.getCommandeById(id);
        if (commande == null) {
            return Response.status(Status.NOT_FOUND)
                    .entity("{\"error\": \"Commande non trouvée\"}").build();
        }
        return Response.ok(commande).build();
    }

    @POST
    public Response createCommande(Commande commande) {
        try {
            Commande nouvelleCommande = commandeService.createCommande(commande);
            return Response.status(Status.CREATED)
                    .entity(nouvelleCommande).build();
        } catch (Exception e) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateCommande(@PathParam("id") Integer id, Commande commande) {
        if (commandeService.getCommandeById(id) == null) {
            return Response.status(Status.NOT_FOUND)
                    .entity("{\"error\": \"Commande non trouvée\"}").build();
        }

        try {
            commande.setId(id);
            Commande commandeMiseAJour = commandeService.updateCommande(commande);
            return Response.ok(commandeMiseAJour).build();
        } catch (Exception e) {
            return Response.status(Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCommande(@PathParam("id") Integer id) {
        if (commandeService.getCommandeById(id) == null) {
            return Response.status(Status.NOT_FOUND)
                    .entity("{\"error\": \"Commande non trouvée\"}").build();
        }

        commandeService.deleteCommande(id);
        return Response.status(Status.NO_CONTENT).build();
    }

    @GET
    @Path("/client/{clientId}")
    public Response getCommandesParClient(@PathParam("clientId") Integer clientId) {
        List<Commande> commandes = commandeService.getCommandesParClient(clientId);
        return Response.ok(commandes).build();
    }

    @GET
    @Path("/statut/{statut}")
    public Response getCommandesParStatut(@PathParam("statut") String statut) {
        List<Commande> commandes = commandeService.getCommandesParStatut(statut);
        return Response.ok(commandes).build();
    }
}