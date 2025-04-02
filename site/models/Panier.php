<?php
require_once 'models/Produit.php';

class Panier {
    private array $items = [];

    public function getItems(): array {
        return $this->items;
    }

    public function addItem(Produit $produit, int $quantite = 1): void {
        $id = $produit->getId();

        if (isset($this->items[$id])) {
            $this->items[$id]['quantite'] += $quantite;
        } else {
            $this->items[$id] = [
                'produit' => $produit,
                'quantite' => $quantite
            ];
        }
    }

    public function removeItem(int $produitId): void {
        if (isset($this->items[$produitId])) {
            unset($this->items[$produitId]);
        }
    }

    public function setItemFromData(array $items): void {
        $this->items = $items;
    }

    public function calculerTotal(): float {
        $total = 0;

        foreach ($this->items as $item) {
            $total += $item['produit']->getPrice() * $item['quantite'];
        }

        return $total;
    }

    public function toArray(): array {
        $result = [];

        foreach ($this->items as $id => $item) {
            $produitArray = $item['produit']->toArray();

            $result[] = [
                'id' => $id,
                'produit' => $produitArray,
                'quantite' => $item['quantite'],
                'unite' => $produitArray['unite'],
                'symbole_unite' => $produitArray['symbole_unite'],
                'nom_unite' => $produitArray['nom_unite']
            ];
        }

        return $result;
    }
}