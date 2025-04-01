<?php
require_once 'services/PanierService.php';

class PanierController {
    private PanierService $panierService;

    public function __construct() {
        $this->panierService = new PanierService();
    }

    public function index(): void {
        $pageTitle = "Votre Panier - Coopérative Agricole";
        $cssFiles = ['paniers.css', 'style.css'];
        $jsFiles = ['paniers.js'];

        $panier = $this->panierService->getPanierFromLocalStorage();

        require_once 'views/templates/header.php';
        require_once 'views/panier/index.php';
        require_once 'views/templates/footer.php';
    }
}