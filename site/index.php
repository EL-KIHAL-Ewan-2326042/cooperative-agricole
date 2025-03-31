<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Coopérative Agricole</title>
    <link rel="stylesheet" href="./css/style.css">
</head>
<body>
<header>
    <h1>ANARCHY ACRES</h1>
    <nav>
        <a href="/site/index.php" class="active">Accueil</a>
        <a href="/site/paniers.php">Voir les paniers</a>
        <a href="/site/commande.php">Commander</a>
        <a href="/site/profil.php">Mon profil</a>
        <a class=login-icon href="profil.php">Connexion / Inscription</a>
    </nav>
</header>

<main>
    <h1>Nos produits</h1>
    <div class="product-grid">
        <?php
        $produits = [
            ["id" => 1, "nom" => "Carottes bio", "type" => "Légume", "prix" => 2.50, "quantite" => 100,
                "image" => "media/carrots.png", "unite_id" => "kg"],

            ["id" => 2, "nom" => "Oeufs fermiers", "type" => "Œuf", "prix" => 3.20, "quantite" => 30,
                "image" => "media/eggs.webp", "unite_id" => "douzaine"],

            ["id" => 3, "nom" => "Fromage de chèvre", "type" => "Fromage", "prix" => 5.00, "quantite" => 50,
                "image" => "media/buche_chevre.png", "unite_id" => "pièce"],

            ["id" => 4, "nom" => "Saucisses", "type" => "Saucisse", "prix" => 1.00, "quantite" => 10,
                "image" => "media/saucisses.png", "unite_id" => "pièce"],
        ];

        foreach ($produits as $produit) {
            echo "
                <div class='product-card' onclick='showModal({$produit['id']})'>
                    <div class='product-img'>
                        <img src='{$produit['image']}' alt='{$produit['nom']}'>
                </div>
                <h3>{$produit['nom']}</h3>
                <p>Type : {$produit['type']}</p>
                <p>Prix : {$produit['prix']} €/{$produit['unite_id']}</p>
                </div>
            ";
        }
        ?>
    </div>

    <div id="productModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeModal()">&times;</span>
            <div id="modal-image"></div>
            <div class="modal-info">
                <h2 id="modal-title"></h2>
                <p id="modal-description"></p>
                <div class="modal-actions">
                    <div class="quantity-selector">
                        <label for="quantity" id="quantity-label">Quantité :</label>
                        <div class="quantity-controls">
                            <button type="button" class="quantity-btn" onclick="updateQuantity(-1)">-</button>
                            <input type="number" id="quantity" min="1" value="1" oninput="updateTotal()">
                            <button type="button" class="quantity-btn" onclick="updateQuantity(1)">+</button>
                        </div>
                    </div>
                    <div class="price-total">
                        <span>Prix total : </span>
                        <span id="total-price"></span>
                    </div>
                </div>
                <button class="ajouter-panier-btn" onclick="ajouterPanier()"><span>Ajouter au panier</span></button>
            </div>
        </div>
    </div>

    <script>
        const produits = <?php echo json_encode($produits); ?>;
    </script>
    <script src="./js/script.js"></script>
</main>

<footer>
    <p>© 2025 - Tous droits réservés</p>
</footer>
<div id="notification" class="buy-notification">
    <div class="notification-text-top">Ajouté au panier</div>
    <div class="notification-glow"></div>
    <div class="notification-content">
        <img id="notification-img" src="" alt="">
        <div class="notification-info">
            <h3 id="notification-title"></h3>
            <p id="notification-desc"></p>
        </div>
    </div>
</div>
<div id="overlay-blur" class="overlay-blur"></div>
</body>
</html>