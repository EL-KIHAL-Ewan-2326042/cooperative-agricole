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
    public Response createType(Type type) {
        Type newType = typeService.createType(type);
        return Response.status(Response.Status.CREATED)
                .entity(newType)
                .build();
    }
    
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateType(@PathParam("id") Integer id, Type type) {
        Type updatedType = typeService.updateType(id, type);
        if (updatedType == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updatedType).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteType(@PathParam("id") Integer id) {
        boolean deleted = typeService.deleteType(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}