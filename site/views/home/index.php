<?php
?>

<main>
    <h1>Nos produits</h1>
    <div class="product-grid">
        <?php if (isset($produitsArray) && is_array($produitsArray)): ?>
            <?php foreach ($produitsArray as $produit): ?>
                <div class="product-card" onclick="showModal(<?= $produit['id'] ?>)">
                    <div class="product-img">
                        <img src="<?= $produit['image'] ?>" alt="<?= $produit['nom'] ?>">
                    </div>
                    <p><?= $produit['type'] ?> - <?= $produit['prix'] ?> €/<?= $produit['symbole_unite'] ?></p>
                    <h3><?= $produit['nom'] ?></h3>
                </div>
            <?php endforeach; ?>
        <?php else: ?>
            <p>Aucun produit disponible pour le moment.</p>
        <?php endif; ?>
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

    <script>
        const produits = <?= json_encode($produitsArray) ?>;
    </script>
</main>