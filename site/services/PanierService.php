<?php
require_once 'models/Panier.php';
require_once 'services/ProduitService.php';

/**
 * Classe PanierService pour gérer les opérations liées au panier.
 */
class PanierService {
    /**
     * @var ProduitService Service pour gérer les produits.
     */
    private ProduitService $produitService;

    /**
     * Constructeur de la classe PanierService.
     */
    public function __construct() {
        $this->productService = new ProduitService();
    }

    /**
     * Récupère le panier depuis le stockage local.
     *
     * @return Panier Le panier récupéré.
     */
    public function getPanierFromLocalStorage(): Panier {
        // Cette méthode sera appelée par le contrôleur
        // Les données seront injectées depuis JavaScript
        return new Panier();
    }

    /**
     * Convertit un panier JavaScript en format API.
     *
     * @param array $jsPanier Le panier en format JavaScript.
     * @return array Le panier en format API.
     */
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

    /**
     * Vérifie la disponibilité du stock pour un produit donné.
     *
     * @param int $produitId L'ID du produit.
     * @param int $requestedQuantity La quantité demandée.
     * @return bool True si la quantité demandée est disponible, sinon False.
     */
    public function checkStockAvailability(int $produitId, int $requestedQuantity): bool {
        $produit = $this->productService->getProduitById($produitId);
        if (!$produit) {
            return false;
        }

        return $requestedQuantity <= $produit->getQuantity();
    }
}