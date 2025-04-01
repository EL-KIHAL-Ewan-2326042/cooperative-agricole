<?php
require_once 'models/Produit.php';

class ProduitService {
    private array $items = [];
    private float $sousTotal = 0;
    private float $fraisLivraison = 4.99;

    public function getItems(): array {
        return $this->items;
    }

    public function getAllProduits(): array {
        $produitsData = [
            ["id" => 1, "nom" => "Carottes bio", "type" => "Légume", "prix" => 2.50, "quantite" => 100,
                "image" => "/site/public/media/carrots.png", "unite_id" => "kg"],
            ["id" => 2, "nom" => "Oeufs fermiers", "type" => "Œuf", "prix" => 3.20, "quantite" => 30,
                "image" => "/site/public/media/eggs.webp", "unite_id" => "douzaine"],
            ["id" => 3, "nom" => "Fromage de chèvre", "type" => "Fromage", "prix" => 5.00, "quantite" => 50,
                "image" => "/site/public/media/buche_chevre.png", "unite_id" => "pièce"],
            ["id" => 4, "nom" => "Saucisses", "type" => "Saucisse", "prix" => 1.00, "quantite" => 10,
                "image" => "/site/public/media/saucisses.png", "unite_id" => "pièce"]
        ];

        $produits = [];
        foreach ($produitsData as $data) {
            $produits[] = new Produit($data);
        }

        return $produits;
    }

    public function getProduitById(int $id): ?Produit {
        $produits = $this->getAllProduits();

        foreach ($produits as $produit) {
            if ($produit->getId() === $id) {
                return $produit;
            }
        }

        return null;
    }

    public function setItems(array $items): void {
        $this->items = $items;
        $this->calculerSousTotal();
    }

    public function getSousTotal(): float {
        return $this->sousTotal;
    }

    public function getFraisLivraison(): float {
        return $this->fraisLivraison;
    }

    public function ajouterItem(array $item): void {
        // Vérifier si l'item existe déjà
        $found = false;
        foreach ($this->items as &$existingItem) {
            if ($existingItem['id'] === $item['id']) {
                $existingItem['quantite'] += $item['quantite'];
                $found = true;
                break;
            }
        }

        if (!$found) {
            $this->items[] = $item;
        }

        $this->calculerSousTotal();
    }

    public function supprimerItem(int $itemId): void {
        $this->items = array_filter($this->items, function($item) use ($itemId) {
            return $item['id'] !== $itemId;
        });

        $this->calculerSousTotal();
    }

    public function modifierQuantite(int $itemId, int $quantite): void {
        foreach ($this->items as &$item) {
            if ($item['id'] === $itemId) {
                $item['quantite'] = $quantite;
                break;
            }
        }

        $this->calculerSousTotal();
    }

    private function calculerSousTotal(): void {
        $this->sousTotal = 0;
        foreach ($this->items as $item) {
            $this->sousTotal += $item['prix'] * $item['quantite'];
        }
    }

    public function getTotal(): float {
        return $this->sousTotal + ($this->sousTotal > 0 ? $this->fraisLivraison : 0);
    }

    public function toArray(): array {
        return [
            'items' => $this->items,
            'sousTotal' => $this->sousTotal,
            'fraisLivraison' => $this->fraisLivraison,
            'total' => $this->getTotal()
        ];
    }
}