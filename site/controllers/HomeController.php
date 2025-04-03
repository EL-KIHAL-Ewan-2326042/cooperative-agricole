<?php
require_once 'services/ProduitService.php';

class HomeController {
    private ProduitService $produitService;

    public function __construct() {
        $this->produitService = new ProduitService();
    }

    public function index(): void {
        $produits = $this->produitService->getAllProduits();

        $produitsArray = array_map(function($produit) {
            return $produit->toArray();
        }, $produits);

        if (empty($produitsArray)) {
            $produitsArray = [];
        }

        $pageTitle = "Accueil - Coopérative Agricole";
        $cssFiles = ['style.css'];
        $jsFiles = ['script.js'];

        require_once 'views/templates/header.php';
        require_once 'views/home/index.php';
        require_once 'views/templates/footer.php';
    }
}