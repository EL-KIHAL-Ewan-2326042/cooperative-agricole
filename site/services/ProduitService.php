<?php
require_once 'models/Produit.php';
require_once 'services/ApiService.php';

/**
 * Classe ProduitService pour gérer les opérations liées aux produits.
 */
class ProduitService {
    /**
     * @var ApiService Service pour interagir avec l'API.
     */
    private ApiService $apiService;

    /**
     * Constructeur de la classe ProduitService.
     */
    public function __construct() {
        $this->apiService = new ApiService();
    }

    /**
     * Récupère tous les produits.
     *
     * @return Produit[] Liste des produits.
     */
    public function getAllProduits(): array {
        $apiProduits = $this->apiService->getProduits();
        return $this->convertApiProduitsToObjects($apiProduits);
    }

    /**
     * Récupère un produit par son ID.
     *
     * @param int $id ID du produit.
     * @return Produit|null Le produit ou null s'il n'existe pas.
     */
    public function getProduitById(int $id): ?Produit {
        $produitData = $this->apiService->getProduitById($id);
        return $produitData ? $this->convertApiToProduit($produitData) : null;
    }

    /**
     * Recherche des produits par terme.
     *
     * @param string $term Terme de recherche.
     * @return Produit[] Liste des produits correspondants.
     */
    public function searchProduits(string $term): array {
        $apiProduits = $this->apiService->searchProduits($term);
        return $this->convertApiProduitsToObjects($apiProduits);
    }

    /**
     * Récupère l'URL de l'image d'un produit.
     *
     * @param int $produitId ID du produit.
     * @return string URL de l'image du produit.
     */
    public function getImageUrl(int $produitId): string {
        return $this->apiService->getImageUrl($produitId);
    }

    /**
     * Convertit les données de l'API en objets Produit.
     *
     * @param array $apiProduits Données de l'API.
     * @return Produit[] Liste des objets Produit.
     */
    private function convertApiProduitsToObjects(array $apiProduits): array {
        $produits = [];
        foreach ($apiProduits as $produitData) {
            $produits[] = $this->convertApiToProduit($produitData);
        }
        return $produits;
    }

    /**
     * Convertit les données de l'API en objet Produit.
     *
     * @param array $produitData Données de l'API.
     * @return Produit L'objet Produit.
     */
    private function convertApiToProduit(array $produitData): Produit {
        $produit = new Produit($produitData);

        if (!filter_var($produit->getImage(), FILTER_VALIDATE_URL) && $produit->getId() > 0) {
            $produit->setImage($this->getImageUrl($produit->getId()));
        }

        return $produit;
    }
}