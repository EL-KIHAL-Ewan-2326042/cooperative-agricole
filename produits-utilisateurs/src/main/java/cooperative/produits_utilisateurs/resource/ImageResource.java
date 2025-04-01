package cooperative.produits_utilisateurs.resource;

import cooperative.produits_utilisateurs.model.Produit;
import cooperative.produits_utilisateurs.service.ImageService;
import cooperative.produits_utilisateurs.service.ProduitService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import java.io.IOException;
import java.io.InputStream;

@Path("/images")
public class ImageResource {

    @Inject
    private ImageService imageService;

    @Inject
    private ProduitService produitService;

    @GET
    @Path("/produit/{id}")
    @Produces("image/jpeg")
    public Response getProductImage(@PathParam("id") Integer id) {
        try {
            Produit produit = produitService.getProduitById(id);
            if (produit == null) {
                return Response.status(Status.NOT_FOUND).build();
            }

            byte[] imageData = imageService.getImageData(produit.getId(), produit.getNom());
            if (imageData == null) {
                return Response.status(Status.NOT_FOUND).build();
            }

            return Response.ok(imageData)
                    .header("Content-Type", "image/jpeg")
                    .header("Content-Disposition", "inline; filename=\"" + produit.getNom() + ".jpg\"")
                    .build();
        } catch (IOException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/produit/{id}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadProductImage(
            @PathParam("id") Integer id,
            InputStream imageData) {

        try {
            Produit produit = produitService.getProduitById(id);
            if (produit == null) {
                return Response.status(Status.NOT_FOUND)
                        .entity("{\"error\": \"Produit non trouvé\"}").build();
            }

            imageService.saveImage(produit.getId(), produit.getNom(), imageData);

            return Response.ok("{\"success\": true}").build();

        } catch (IOException e) {
            e.printStackTrace();
            return Response.serverError()
                    .entity("{\"error\": \"" + e.getMessage() + "\"}").build();
        }
    }
}