package cooperative.produits_utilisateurs.resource;

import cooperative.produits_utilisateurs.model.Produit;
import cooperative.produits_utilisateurs.model.Type;
import cooperative.produits_utilisateurs.model.Unite;
import cooperative.produits_utilisateurs.service.ProduitService;
import cooperative.produits_utilisateurs.service.TypeService;
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

    @Inject
    private TypeService typeService;

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

        // Vérification explicite que le type et l'unité sont présents
        if (produit.getType() == null && produit.getTypeId() != null) {
            Type type = typeService.getTypeById(produit.getTypeId());
            if (type != null) {
                produit.setType(type);
            }
        }

        if (produit.getUnite() == null || (produit.getUnite() != null && produit.getUnite().getNom() == null)) {
            if (produit.getUnite() != null && produit.getUnite().getId() != null) {
                Unite unite = uniteService.getUniteById(produit.getUnite().getId());
                if (unite != null) {
                    produit.setUnite(unite);
                }
            }
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
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProduitWithPath(
            @QueryParam("nom") String nom,
            @QueryParam("typeId") Integer typeId,
            @QueryParam("prix") BigDecimal prix,
            @QueryParam("quantite") Integer quantite,
            @QueryParam("uniteId") Integer uniteId) {

        // Vérification des paramètres obligatoires
        if (nom == null || typeId == null || prix == null || quantite == null || uniteId == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Tous les paramètres sont obligatoires")
                    .build();
        }

        Produit produit = new Produit();
        produit.setNom(nom);
        produit.setTypeId(typeId);
        produit.setPrix(prix);
        produit.setQuantite(quantite);

        // Récupération de l'unité par ID
        Unite unite = uniteService.getUniteById(uniteId);
        if (unite == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID d'unité invalide: " + uniteId)
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