package cooperative.produits_utilisateurs.service;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.core.Context;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@ApplicationScoped
public class ImageService {

    @Context
    private ServletContext servletContext;

    // Chemin absolu vers le dossier externe sur le serveur
    private static final String EXTERNAL_IMAGE_DIRECTORY = "/var/coop-images";

    public Path getImagePath(Integer productId, String productName) {
        // Nommage plus propre avec extension
        String fileName = productId + "_" + productName.replaceAll("[\\s+/\\\\:*?\"<>|]", "_") + ".jpg";
        Path imagesDir = Paths.get(EXTERNAL_IMAGE_DIRECTORY);

        if (!Files.exists(imagesDir)) {
            try {
                Files.createDirectories(imagesDir);
            } catch (IOException e) {
                throw new RuntimeException("Impossible de créer le répertoire des images: " + imagesDir, e);
            }
        }

        return imagesDir.resolve(fileName);
    }

    public boolean imageExists(Integer productId, String productName) {
        Path imagePath = getImagePath(productId, productName);
        return Files.exists(imagePath);
    }

    @PostConstruct
    public void initDefaultImages() {
        try {
            Path defaultImagePath = Paths.get(EXTERNAL_IMAGE_DIRECTORY, "default.jpg");

            // Copier l'image par défaut depuis les ressources de l'application
            if (!Files.exists(defaultImagePath)) {
                InputStream defaultImageStream = getClass().getResourceAsStream("/images/default.jpg");
                if (defaultImageStream != null) {
                    Files.createDirectories(defaultImagePath.getParent());
                    Files.copy(defaultImageStream, defaultImagePath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'initialisation des images par défaut: " + e.getMessage());
        }
    }

    public byte[] getImageData(Integer productId, String productName) throws IOException {
        Path imagePath = getImagePath(productId, productName);
        if (!Files.exists(imagePath)) {
            Path defaultImagePath = Paths.get(EXTERNAL_IMAGE_DIRECTORY, "default.jpg");
            if (Files.exists(defaultImagePath)) {
                return Files.readAllBytes(defaultImagePath);
            }
            return null;
        }
        return Files.readAllBytes(imagePath);
    }

    public void saveImage(Integer productId, String productName, InputStream imageData) throws IOException {
        Path destination = getImagePath(productId, productName);
        Files.copy(imageData, destination, StandardCopyOption.REPLACE_EXISTING);
    }
}