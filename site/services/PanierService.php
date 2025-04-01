<?php
require_once 'models/Panier.php';
require_once 'services/ProduitService.php';

class PanierService {
    private ProduitService $produitService;

    public function __construct() {
        $this->produitService = new ProduitService();
    }

    public function getPanierFromLocalStorage(): Panier {
        // Création d'un panier vide
        $panier = new Panier();

        // Cette fonction sera appelée par JavaScript pour initialiser le panier
        // Le traitement des données se fait côté client, cette méthode est un placeholder

        return $panier;
    }

    // Cette méthode sera utilisée plus tard avec l'API
    public function sauvegarderPanier(Panier $panier): bool {
        // Sauvegarde dans la base de données
        // À implémenter plus tard
        return true;
    }

    public function verifierStockDisponible(int $produitId, int $quantiteDemandee): bool {
        $produit = $this->produitService->getProduitById($produitId);
        if (!$produit) {
            return false;
        }

        return $quantiteDemandee <= $produit->getQuantite();
    }
}