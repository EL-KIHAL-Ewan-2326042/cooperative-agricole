package cooperative.produits_utilisateurs.resource;

import cooperative.produits_utilisateurs.model.Type;
import cooperative.produits_utilisateurs.service.TypeService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/types")
@Produces(MediaType.APPLICATION_JSON)
public class TypeResource {
    @Inject
    private TypeService typeService;
    
    @GET
    public Response getAllTypes() {
        List<Type> types = typeService.getAllTypes();
        return Response.ok(types).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getTypeById(@PathParam("id") Integer id) {
        Type type = typeService.getTypeById(id);
        if (type == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(type).build();
    }

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