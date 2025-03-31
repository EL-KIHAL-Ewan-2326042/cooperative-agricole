package cooperative.produits_utilisateurs.resource;

import cooperative.produits_utilisateurs.model.Produit;
import cooperative.produits_utilisateurs.service.ImageService;
import cooperative.produits_utilisateurs.service.ProduitService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;

@Path("/images")
public class ImageResource {
    
    @Inject
    private ImageService imageService;
    
    @Inject
    private ProduitService produitService;
    
    @GET
    @Path("/produit/{id}")
    @Produces("image/*")
    public Response getProductImage(@PathParam("id") Integer id) {
        try {
            Produit produit = produitService.getProduitById(id);
            if (produit == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            byte[] imageData = imageService.getImageData(produit.getId(), produit.getNom());
            if (imageData == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            
            return Response.ok(imageData).build();
        } catch (IOException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}