<?php
class Panier {
    private array $items = [];
    private float $sousTotal = 0;
    private float $fraisLivraison = 4.99;

    public function __construct(array $items = []) {
        $this->items = $items;
        $this->calculerSousTotal();
    }

    public function getItems(): array {
        return $this->items;
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