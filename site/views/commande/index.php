<?php
/**
 * Page de finalisation de commande.
 */
?>

<main class="commande-container">
    <h1>Finaliser votre commande</h1>

    <div class="commande-grid">
        <div class="commande-form">
            <h2>Informations de livraison</h2>
            <form id="commande-form" action="?route=commande/valider" method="post">
                <div class="form-group">
                    <label for="nom">Nom</label>
                    <input type="text" id="nom" name="nom" required>
                </div>

                <div class="form-group">
                    <label for="prenom">Prénom</label>
                    <input type="text" id="prenom" name="prenom" required>
                </div>

                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" required>
                </div>

                <div class="form-group">
                    <label for="telephone">Téléphone</label>
                    <input type="tel" id="telephone" name="telephone" required>
                </div>

                <div class="form-group">
                    <label for="adresse">Adresse</label>
                    <input type="text" id="adresse" name="adresse" required>
                </div>

                <div class="form-group">
                    <label for="code_postal">Code postal</label>
                    <input type="text" id="code_postal" name="code_postal" required>
                </div>

                <div class="form-group">
                    <label for="ville">Ville</label>
                    <input type="text" id="ville" name="ville" required>
                </div>

                <h2>Mode de paiement</h2>
                <div class="payment-options">
                    <div class="payment-option">
                        <input type="radio" id="cb" name="paiement" value="cb" checked>
                        <label for="cb">Carte bancaire</label>
                    </div>
                    <div class="payment-option">
                        <input type="radio" id="paypal" name="paiement" value="paypal">
                        <label for="paypal">PayPal</label>
                    </div>
                </div>

                <button type="submit" class="valider-commande-btn"><span>Valider la commande</span></button>
            </form>
        </div>

        <div class="commande-recap">
            <h2>Récapitulatif</h2>
            <div id="commande-items">
                <!-- Généré par JavaScript -->
            </div>
            <div class="commande-totaux">
                <div class="ligne-total">
                    <span>Sous-total :</span>
                    <span id="recap-sous-total">0.00 €</span>
                </div>
                <div class="ligne-total">
                    <span>Frais de livraison :</span>
                    <span id="recap-frais-livraison">4.99 €</span>
                </div>
                <div class="ligne-total total-final">
                    <span>Total :</span>
                    <span id="recap-total">0.00 €</span>
                </div>
            </div>
        </div>
    </div>
</main>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        /**
         * Récupère le panier depuis le stockage local et met à jour le récapitulatif de la commande.
         */
        const panier = JSON.parse(localStorage.getItem('panier')) || [];

        // Redirection si panier vide
        if (panier.length === 0) {
            window.location.href = '?route=panier';
            return;
        }
        const commandeItems = document.getElementById('commande-items');

        let sousTotal = 0;

        panier.forEach(produit => {
            const totalProduit = produit.prix * produit.quantite;
            sousTotal += totalProduit;

            const itemHTML = `
                <div class="commande-item">
                    <img src="${produit.image}" alt="${produit.nom}">
                    <div class="commande-item-details">
                        <h3>${produit.nom}</h3>
                        <p>${produit.quantite} x ${produit.prix} €</p>
                    </div>
                    <span class="commande-item-prix">${totalProduit.toFixed(2)} €</span>
                </div>
            `;

            commandeItems.innerHTML += itemHTML;
        });

        const fraisLivraison = sousTotal > 0 ? 4.99 : 0;
        const total = sousTotal + fraisLivraison;

        document.getElementById('recap-sous-total').textContent = sousTotal.toFixed(2) + ' €';
        document.getElementById('recap-frais-livraison').textContent = fraisLivraison.toFixed(2) + ' €';
        document.getElementById('recap-total').textContent = total.toFixed(2) + ' €';

    });
</script>