<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Votre Panier - Coopérative Agricole</title>
    <link rel="stylesheet" href="./css/paniers.css">
</head>
<body>
<header>
    <h1>ANARCHY ACRES</h1>
    <nav>
        <a href="/site/index.php">Accueil</a>
        <a href="/site/paniers.php" class="active">Voir les paniers</a>
        <a href="/site/commande.php">Commander</a>
        <a href="/site/profil.php">Mon profil</a>
        <a class="login-icon" href="profil.php">Connexion / Inscription</a>
    </nav>
</header>

<main class="panier-container">
    <h1>Votre Panier</h1>

    <div id="panier-vide" class="panier-message">
        <img src="./media/panier-vide.png" alt="Panier vide">
        <p>Votre panier est vide</p>
        <a href="index.php" class="retour-boutique-btn">Retourner à la boutique</a>
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
            <button id="passer-commande" class="passer-commande-btn">Passer la commande</button>
        </div>
    </div>
</main>

<footer>
    <p>© 2025 - Tous droits réservés</p>
</footer>

<script src="./js/paniers.js"></script>
</body>
</html>