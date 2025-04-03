package cooperative.produits_utilisateurs.resource;

import cooperative.produits_utilisateurs.model.Unite;
import cooperative.produits_utilisateurs.service.UniteService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Ressource REST pour gérer les unités de mesure des produits.
 */
@Path("/unites")
@Produces(MediaType.APPLICATION_JSON)
public class UniteResource {
    @Inject
    private UniteService uniteService;
    
    /**
     * Récupère toutes les unités de mesure.
     *
     * @return la réponse contenant la liste des unités
     */
    @GET
    public Response getAllUnites() {
        List<Unite> unites = uniteService.getAllUnites();
        return Response.ok(unites).build();
    }
    
    /**
     * Récupère une unité de mesure par son ID.
     *
     * @param id l'ID de l'unité
     * @return la réponse contenant l'unité ou un statut d'erreur
     */
    @GET
    @Path("/{id}")
    public Response getUniteById(@PathParam("id") Integer id) {
        Unite unite = uniteService.getUniteById(id);
        if (unite == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(unite).build();
    }
}
