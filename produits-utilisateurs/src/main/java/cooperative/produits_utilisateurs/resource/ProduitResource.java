package cooperative.produits_utilisateurs.resource;

import cooperative.produits_utilisateurs.model.Produit;
import cooperative.produits_utilisateurs.service.ProduitService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/produits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProduitResource {
    
    @Inject
    private ProduitService produitService;
    
    @GET
    public Response getAllProduits() {
        List<Produit> produits = produitService.getAllProduits();
        return Response.ok(produits).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getProduitById(@PathParam("id") Integer id) {
        Produit produit = produitService.getProduitById(id);
        if (produit == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(produit).build();
    }
    
    @POST
    public Response createProduit(Produit produit) {
        Produit newProduit = produitService.createProduit(produit);
        return Response.status(Response.Status.CREATED)
                .entity(newProduit)
                .build();
    }
    
    @PUT
    @Path("/{id}")
    public Response updateProduit(@PathParam("id") Integer id, Produit produit) {
        Produit updatedProduit = produitService.updateProduit(id, produit);
        if (updatedProduit == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(updatedProduit).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response deleteProduit(@PathParam("id") Integer id) {
        boolean deleted = produitService.deleteProduit(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}