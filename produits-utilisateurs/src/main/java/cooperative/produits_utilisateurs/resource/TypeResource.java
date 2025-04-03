package cooperative.produits_utilisateurs.resource;

import cooperative.produits_utilisateurs.model.Type;
import cooperative.produits_utilisateurs.service.TypeService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Ressource REST pour gérer les types de produits.
 */
@Path("/types")
@Produces(MediaType.APPLICATION_JSON)
public class TypeResource {
    @Inject
    private TypeService typeService;
    
    /**
     * Récupère tous les types de produits.
     *
     * @return la réponse contenant la liste des types
     */
    @GET
    public Response getAllTypes() {
        List<Type> types = typeService.getAllTypes();
        return Response.ok(types).build();
    }
    
    /**
     * Récupère un type de produit par son ID.
     *
     * @param id l'ID du type
     * @return la réponse contenant le type ou un statut d'erreur
     */
    @GET
    @Path("/{id}")
    public Response getTypeById(@PathParam("id") Integer id) {
        Type type = typeService.getTypeById(id);
        if (type == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(type).build();
    }

    /**
     * Crée un nouveau type de produit.
     *
     * @param type le type à créer
     * @return la réponse contenant le type créé ou un statut d'erreur
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createType(Type type) {
        Type createdType = typeService.createType(type);
        if (createdType != null) {
            return Response.status(Response.Status.CREATED).entity(createdType).build();
        }
        return Response.serverError().build();
    }

    /**
     * Met à jour un type de produit existant.
     *
     * @param id l'ID du type
     * @param type le type avec les nouvelles données
     * @return la réponse contenant le type mis à jour ou un statut d'erreur
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateType(@PathParam("id") Integer id, Type type) {
        type.setId(id);
        type.setTypeId(id);

        if (type.getNom() == null || type.getNom().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Le nom du type est requis").build();
        }

        Type updatedType = typeService.updateType(id, type);

        if (updatedType == null) {
            Type existingType = typeService.getTypeById(id);
            if (existingType == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                return Response.status(Response.Status.NOT_MODIFIED).build();
            }
        }

        return Response.ok(updatedType).build();
    }

    /**
     * Supprime un type de produit par son ID.
     *
     * @param id l'ID du type
     * @return la réponse indiquant le succès ou l'échec de l'opération
     */
    @DELETE
    @Path("/{id}")
    public Response deleteType(@PathParam("id") Integer id) {
        boolean deleted = typeService.deleteType(id);
        if (deleted) {
            return Response.status(Response.Status.ACCEPTED).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
