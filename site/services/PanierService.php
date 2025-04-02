<?php
require_once 'models/Panier.php';
require_once 'services/ProduitService.php';

class PanierService {
    private ProduitService $produitService;

    public function __construct() {
        $this->productService = new ProduitService();
    }

    public function getPanierFromLocalStorage(): Panier {
        // This method will be called by the controller
        // Data will be injected from JavaScript
        return new Panier();
    }

    public function convertJsPanierToApi(array $jsPanier): array {
        $apiPanier = [];

        foreach ($jsPanier as $item) {
            $produit = $this->productService->getProduitById($item['id']);

            if ($produit) {
                $produitArray = $produit->toArray();
                $apiPanier[] = [
                    'id' => $produit->getId(),
                    'nom' => $produit->getName(),
                    'prix' => $produit->getPrice(),
                    'quantite' => $item['quantite'],
                    'image' => $produit->getImage(),
                    'type' => $produit->getType(),
                    'unite' => $produitArray['unite'] ?? null
                ];
            }
        }

        return $apiPanier;
    }

    public function checkStockAvailability(int $produitId, int $requestedQuantity): bool {
        $produit = $this->productService->getProduitById($produitId);
        if (!$produit) {
            return false;
        }

        return $requestedQuantity <= $produit->getQuantity();
    }
}