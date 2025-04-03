<?php
/**
 * Contrôleur pour la gestion de l'upload d'images.
 */
require_once 'services/ProduitService.php';

class ImageUploadController
{
    /**
     * @var ProduitService Service pour la gestion des produits.
     */
    private $produitService;

    /**
     * @var string URL de base de l'API.
     */
    private $apiBaseUrl = "http://79.72.25.28:8080/api";

    /**
     * Constructeur de la classe.
     */
    public function __construct()
    {
        if (session_status() === PHP_SESSION_NONE) {
            session_start();
        }
        $this->produitService = new ProduitService();
    }

    /**
     * Affiche la page d'upload d'images.
     */
    public function index(): void
    {
        $pageTitle = "Upload d'images - Coopérative Agricole";
        $cssFiles = ['upload.css', 'style.css'];
        $produits = $this->produitService->getAllProduits();

        require_once 'views/templates/header.php';
        require_once 'views/upload/image_upload.php';
        require_once 'views/templates/footer.php';
    }

    /**
     * Gère l'upload d'une image.
     */
    public function upload(): void
    {
        if (!isset($_FILES['image']) || $_FILES['image']['error'] !== UPLOAD_ERR_OK) {
            $this->setErrorMessage("Erreur lors de l'upload: " . $this->getUploadErrorMessage($_FILES['image']['error'] ?? -1));
            $this->redirect("/?route=image_upload");
            return;
        }

        if (!isset($_POST['produit_id']) || !is_numeric($_POST['produit_id'])) {
            $this->setErrorMessage("ID de produit invalide.");
            $this->redirect("/?route=image_upload");
            return;
        }

        $produitId = (int)$_POST['produit_id'];
        $imagePath = $_FILES['image']['tmp_name'];
        $imageType = mime_content_type($imagePath);

        if (!in_array($imageType, ['image/jpeg', 'image/png', 'image/webp'])) {
            $this->setErrorMessage("Type de fichier non supporté. Utilisez JPG, PNG ou WEBP.");
            $this->redirect("/?route=image_upload");
            return;
        }

        $result = $this->uploadImageToApi($imagePath, $produitId, $_FILES['image']['name']);

        if ($result) {
            $this->setSuccessMessage("Image uploadée avec succès pour le produit #$produitId");
        } else {
            $this->setErrorMessage("Échec de l'upload. Consultez les logs de débogage.");
        }
        $this->redirect("/?route=image_upload");
    }

    /**
     * Upload l'image vers l'API.
     *
     * @param string $imagePath Chemin de l'image.
     * @param int $produitId ID du produit.
     * @param string $imageName Nom de l'image.
     * @return bool Retourne true en cas de succès, false sinon.
     */
    private function uploadImageToApi(string $imagePath, int $produitId, string $imageName): bool
    {
        $url = "http://79.72.25.28:8080/api/images/produit/{$produitId}";

        $postData = [
            'image' => new CURLFile($imagePath)
        ];

        $ch = curl_init();
        curl_setopt_array($ch, [
            CURLOPT_URL => $url,
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_POST => true,
            CURLOPT_POSTFIELDS => $postData,
            CURLOPT_HTTPHEADER => [
                "Content-Type: multipart/form-data"
            ]
        ]);

        $response = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        $error = curl_error($ch);

        curl_close($ch);

        if ($error) {
            $_SESSION['debug_info'][] = "cURL Error: $error";
            return false;
        }

        if ($httpCode != 200) {
            $_SESSION['debug_info'][] = "Erreur API: $response";
            return false;
        }

        return true;
    }

    /**
     * Retourne le message d'erreur correspondant au code d'erreur d'upload.
     *
     * @param int $errorCode Code d'erreur.
     * @return string Message d'erreur.
     */
    private function getUploadErrorMessage(int $errorCode): string
    {
        $errors = [
            UPLOAD_ERR_INI_SIZE => "L'image dépasse la taille maximale autorisée par PHP",
            UPLOAD_ERR_FORM_SIZE => "L'image dépasse la taille maximale autorisée par le formulaire",
            UPLOAD_ERR_PARTIAL => "L'image n'a été que partiellement téléchargée",
            UPLOAD_ERR_NO_FILE => "Aucune image n'a été téléchargée",
            UPLOAD_ERR_NO_TMP_DIR => "Dossier temporaire manquant",
            UPLOAD_ERR_CANT_WRITE => "Échec d'écriture du fichier",
            UPLOAD_ERR_EXTENSION => "Upload arrêté par une extension PHP"
        ];

        return $errors[$errorCode] ?? "Erreur inconnue (code: $errorCode)";
    }

    /**
     * Redirige vers une URL spécifiée.
     *
     * @param string $url URL de redirection.
     */
    private function redirect(string $url): void
    {
        header("Location: $url");
        exit;
    }

    /**
     * Définit un message d'erreur dans la session.
     *
     * @param string $message Message d'erreur.
     */
    private function setErrorMessage(string $message): void
    {
        $_SESSION['error'] = $message;
    }

    /**
     * Définit un message de succès dans la session.
     *
     * @param string $message Message de succès.
     */
    private function setSuccessMessage(string $message): void
    {
        $_SESSION['success'] = $message;
    }
}