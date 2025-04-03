<?php
/**
 * Page d'upload d'image pour un produit.
 *
 * @var array $produits Liste des produits disponibles pour l'upload d'image.
 */
?>

<?php if (isset($_SESSION['error'])): ?>
    <div class="alert alert-danger"><?= $_SESSION['error'] ?></div>
    <?php unset($_SESSION['error']); ?>
<?php endif; ?>

<?php if (isset($_SESSION['success'])): ?>
    <div class="alert alert-success"><?= $_SESSION['success'] ?></div>
    <?php unset($_SESSION['success']); ?>
<?php endif; ?>

<main>
    <h2>Upload d'image pour un produit</h2>
    <form action="/?route=upload_image" method="post" enctype="multipart/form-data">
        <div class="form-group">
            <label for="produit_id">Produit :</label>
            <select name="produit_id" id="produit_id" required>
                <?php foreach ($produits as $produit): ?>
                    <option value="<?= $produit->getId() ?>"><?= $produit->getName() ?></option>
                <?php endforeach; ?>
            </select>
        </div>

        <div class="form-group">
            <label for="image">Choisir une image :</label>
            <input type="file" name="image" id="image" accept="image/jpeg, image/png, image/webp" required>
        </div>

        <button type="submit" class="btn btn-primary">Uploader</button>
    </form>
</main>