package cooperative.produits_utilisateurs.resource;

import cooperative.produits_utilisateurs.model.Produit;
import cooperative.produits_utilisateurs.model.Unite;
import cooperative.produits_utilisateurs.service.ProduitService;
import cooperative.produits_utilisateurs.service.UniteService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.List;

@Path("/produits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProduitResource {

    @Inject
    private UniteService uniteService;

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

    @GET
    @Path("/search")
    public Response findProduitsByNom(@QueryParam("nom") String nom) {
        List<Produit> produits = produitService.findProduitsByNom(nom);
        return Response.ok(produits).build();
    }

    @POST
    public Response createProduit(Produit produit) {
        Produit newProduit = produitService.createProduit(produit);
        return Response.status(Response.Status.CREATED)
                .entity(newProduit)
                .build();
    }

    @POST
    @Path("/create/{nom}/{type}/{prix}/{quantite}/{uniteId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProduitWithPath(
            @PathParam("nom") String nom,
            @PathParam("type") String type,
            @PathParam("prix") BigDecimal prix,
            @PathParam("quantite") BigDecimal quantite,
            @PathParam("uniteId") Integer uniteId) {

        Produit produit = new Produit();
        produit.setNom(nom);
        produit.setType(type);
        produit.setPrix(prix);
        produit.setQuantite(quantite);

        // Get unite by ID
        Unite unite = uniteService.getUniteById(uniteId);
        if (unite == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid unite ID: " + uniteId)
                    .build();
        }

        produit.setUnite(unite);

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