<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Coopérative Agricole</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<header>
    <h1>Coopérative Agricole</h1>
    <nav>
        <a href="paniers.php">Voir les paniers</a>
        <a href="commande.php">Commander</a>
        <a href="profil.php">Mon profil</a>
        <a class=login-icon href="profil.php">Connexion / Inscription</a>
    </nav>
</header>

<main>
    <h1>Nos Produits</h1>
    <div class="product-grid">
        <?php
        $produits = [
            ["id" => 1, "nom" => "Carottes Bio", "type" => "Légume", "prix" => 2.50, "quantite" => 100,
                "image" => "https://www.jaimefruitsetlegumes.ca/wp-content/uploads/2019/09/1-2-700x700.png"],

            ["id" => 2, "nom" => "Oeufs Fermiers", "type" => "Œuf", "prix" => 3.20, "quantite" => 30,
                "image" => "https://www.framboizeinthekitchen.com/wp-content/uploads/2018/04/oeufs.jpg"],

            ["id" => 3, "nom" => "Fromage de Chèvre", "type" => "Fromage", "prix" => 5.00, "quantite" => 50,
                "image" => "https://www.france-mineraux.fr/wp-content/uploads/2023/11/aliment-fromage-de-chevre.jpg"],
        ];

        foreach ($produits as $produit) {
            echo "
                <div class='product-card' onclick='showModal({$produit['id']})'>
                    <div class='product-img'>
                        <img src='{$produit['image']}' alt='{$produit['nom']}'>
                    </div>
                    <h3>{$produit['nom']}</h3>
                    <p>Type : {$produit['type']}</p>
                    <p>Prix : {$produit['prix']} €/u</p>
                </div>
            ";
        }
        ?>
    </div>

    <!-- Modal -->
    <div id="productModal" class="modal">
        <div class="modal-content">
            <span class="close" onclick="closeModal()">&times;</span>
            <div id="modal-image"></div>
            <div class="modal-info">
                <h2 id="modal-title"></h2>
                <p id="modal-description"></p>
                <div class="modal-actions">
                    <label for="unite">Unité :</label>
                    <select id="unite">
                        <option value="g">Gramme</option>
                        <option value="kg">Kilo</option>
                        <option value="u">Unité</option>
                        <option value="dz">Douzaine</option>
                        <option value="L">Litre</option>
                        <option value="pc">Pièce</option>
                    </select>
                    <button onclick="ajouterPanier()">Ajouter au panier</button>
                </div>
            </div>
        </div>
    </div>

    <script>
        const produits = <?php echo json_encode($produits); ?>;
    </script>
    <script src="js/script.js"></script>
</main>

<footer>
    <p>© 2025 - Tous droits réservés</p>
</footer>
</body>
</html>