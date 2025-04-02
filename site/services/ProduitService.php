<?php
require_once 'models/Produit.php';
require_once 'services/ApiService.php';

class ProduitService {
    private ApiService $apiService;

    public function __construct() {
        $this->apiService = new ApiService();
    }

    /**
     * Get all products
     * @return Produit[]
     */
    public function getAllProduits(): array {
        $apiProduits = $this->apiService->getProduits();
        return $this->convertApiProduitsToObjects($apiProduits);
    }

    /**
     * Get product by ID
     * @param int $id Produit ID
     * @return Produit|null
     */
    public function getProduitById(int $id): ?Produit {
        $produitData = $this->apiService->getProduitById($id);
        return $produitData ? $this->convertApiToProduit($produitData) : null;
    }

    /**
     * Search products by term
     * @param string $term Search term
     * @return Produit[]
     */
    public function searchProduits(string $term): array {
        $apiProduits = $this->apiService->searchProduits($term);
        return $this->convertApiProduitsToObjects($apiProduits);
    }

    /**
     * Get image URL for a product
     * @param int $produitId Produit ID
     * @return string
     */
    public function getImageUrl(int $produitId): string {
        return $this->apiService->getImageUrl($produitId);
    }

    /**
     * Convert API data to Produit objects
     * @param array $apiProduits API data
     * @return Produit[]
     */
    private function convertApiProduitsToObjects(array $apiProduits): array {
        $produits = [];
        foreach ($apiProduits as $produitData) {
            $produits[] = $this->convertApiToProduit($produitData);
        }
        return $produits;
    }

    /**
     * Convert API data to Produit object
     * @param array $produitData API data
     * @return Produit
     */
    private function convertApiToProduit(array $produitData): Produit {
        $produit = new Produit($produitData);

        if (!filter_var($produit->getImage(), FILTER_VALIDATE_URL) && $produit->getId() > 0) {
            $produit->setImage($this->getImageUrl($produit->getId()));
        }

        return $produit;
    }
}