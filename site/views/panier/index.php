<?php
?>

<main class="panier-container">
    <h2>Votre panier</h2>

    <div id="panier-vide" class="panier-message">
        <img src="<?= BASE_URL ?>/public/media/panier-vide.png" alt="Panier vide">
        <p>Votre panier est vide</p>
        <div class="panier-buttons">
            <a href="<?= BASE_URL ?>?route=home" class="retour-boutique-btn">Retourner à la boutique</a>
            <a href="<?= BASE_URL ?>?route=commande/historique" class="voir-commandes-btn">Voir mes commandes passées</a>
        </div>
    </div>

    <div id="panier-contenu" style="display: none;">
        <div class="panier-items">
        </div>

        <div class="panier-resume">
            <h2>Résumé de la commande</h2>
            <div class="panier-totaux">
                <div class="ligne-total">
                    <span>Sous-total :</span>
                    <span id="sous-total">0.00 €</span>
                </div>
                <div class="ligne-total">
                    <span>Frais de livraison :</span>
                    <span id="frais-livraison">4.99 €</span>
                </div>
                <div class="ligne-total total-final">
                    <span>Total :</span>
                    <span id="total-panier">0.00 €</span>
                </div>
            </div>
            <button id="passer-commande" class="passer-commande-btn"><span>Passer la commande</span></button>
            <button id="vider-panier" class="vider-panier-btn">Vider le panier</button>
            <a href="<?= BASE_URL ?>?route=commande/historique" class="voir-commandes-link">Voir mes commandes passées</a>
        </div>
    </div>
</main>