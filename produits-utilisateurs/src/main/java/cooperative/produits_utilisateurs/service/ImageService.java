package cooperative.produits_utilisateurs.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.core.Context;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@ApplicationScoped
public class ImageService {
    
    @Context
    private ServletContext servletContext;
    
    private static final String IMAGE_DIRECTORY = "WEB-INF/images";
    
    public Path getImagePath(Integer productId, String productName) {
        String realPath = servletContext.getRealPath("/");
        String fileName = productId + productName.replaceAll("\\s+", "_");
        Path imagesDir = Paths.get(realPath, IMAGE_DIRECTORY);
        
        // Vérifier si le répertoire existe, sinon le créer
        if (!Files.exists(imagesDir)) {
            try {
                Files.createDirectories(imagesDir);
            } catch (IOException e) {
                throw new RuntimeException("Impossible de créer le répertoire des images", e);
            }
        }
        
        return imagesDir.resolve(fileName);
    }
    
    public boolean imageExists(Integer productId, String productName) {
        Path imagePath = getImagePath(productId, productName);
        return Files.exists(imagePath);
    }
    
    public byte[] getImageData(Integer productId, String productName) throws IOException {
        Path imagePath = getImagePath(productId, productName);
        if (!Files.exists(imagePath)) {
            return null;
        }
        return Files.readAllBytes(imagePath);
    }
}