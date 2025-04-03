<?php
        require_once 'models/Produit.php';

        /**
         * Classe représentant un panier d'achats.
         */
        class Panier {
            /**
             * @var array Liste des items dans le panier.
             */
            private array $items = [];

            /**
             * Retourne les items du panier.
             *
             * @return array Liste des items.
             */
            public function getItems(): array {
                return $this->items;
            }

            /**
             * Ajoute un produit au panier.
             *
             * @param Produit $produit Le produit à ajouter.
             * @param int $quantite La quantité du produit à ajouter.
             */
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

            /**
             * Supprime un produit du panier.
             *
             * @param int $produitId L'ID du produit à supprimer.
             */
            public function removeItem(int $produitId): void {
                if (isset($this->items[$produitId])) {
                    unset($this->items[$produitId]);
                }
            }

            /**
             * Définit les items du panier à partir de données.
             *
             * @param array $items Les items à définir.
             */
            public function setItemFromData(array $items): void {
                $this->items = $items;
            }

            /**
             * Calcule le total du panier.
             *
             * @return float Le total du panier.
             */
            public function calculerTotal(): float {
                $total = 0;

                foreach ($this->items as $item) {
                    $total += $item['produit']->getPrice() * $item['quantite'];
                }

                return $total;
            }

            /**
             * Convertit le panier en tableau.
             *
             * @return array Le panier sous forme de tableau.
             */
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