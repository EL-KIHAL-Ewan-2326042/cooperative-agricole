<?php
/**
 * Contrôleur pour la gestion des paniers.
 */
require_once 'services/PanierService.php';
require_once 'services/ApiService.php';

class PanierController {
    /**
     * @var PanierService Service pour la gestion du panier.
     */
    private PanierService $panierService;

    /**
     * @var ApiService Service pour la communication avec l'API.
     */
    private ApiService $apiService;

    /**
     * Constructeur de la classe.
     */
    public function __construct() {
        $this->panierService = new PanierService();
        $this->apiService = new ApiService();
    }

    /**
     * Affiche la page du panier.
     */
    public function index(): void {
        $pageTitle = "Votre panier - Coopérative Agricole";
        $cssFiles = ['paniers.css', 'style.css'];
        $jsFiles = ['paniers.js'];

        $panier = $this->panierService->getPanierFromLocalStorage();

        require_once 'views/templates/header.php';
        require_once 'views/panier/index.php';
        require_once 'views/templates/footer.php';
    }

    /**
     * Ajoute un produit au panier.
     */
    public function ajouter(): void {
        $produitId = $_POST['produit_id'] ?? null;
        $quantite = $_POST['quantite'] ?? 1;

        if ($produitId) {
            $this->panierService->ajouterProduit($produitId, $quantite);
            $this->setSuccessMessage("Produit ajouté au panier.");
        } else {
            $this->setErrorMessage("ID de produit invalide.");
        }

        $this->redirect("?route=panier");
    }

    /**
     * Supprime un produit du panier.
     */
    public function supprimer(): void {
        $produitId = $_POST['produit_id'] ?? null;

        if ($produitId) {
            $this->panierService->supprimerProduit($produitId);
            $this->setSuccessMessage("Produit supprimé du panier.");
        } else {
            $this->setErrorMessage("ID de produit invalide.");
        }

        $this->redirect("?route=panier");
    }

    /**
     * Vide le panier.
     */
    public function vider(): void {
        $this->panierService->viderPanier();
        $this->setSuccessMessage("Panier vidé.");
        $this->redirect("?route=panier");
    }

    /**
     * Redirige vers une URL spécifiée.
     *
     * @param string $url URL de redirection.
     */
    private function redirect(string $url): void {
        header("Location: $url");
        exit;
    }

    /**
     * Définit un message d'erreur dans la session.
     *
     * @param string $message Message d'erreur.
     */
    private function setErrorMessage(string $message): void {
        $_SESSION['error'] = $message;
    }

    /**
     * Définit un message de succès dans la session.
     *
     * @param string $message Message de succès.
     */
    private function setSuccessMessage(string $message): void {
        $_SESSION['success'] = $message;
    }
}