<?php
/**
 * Pied de page du site.
 *
 * @var array $jsFiles Liste des fichiers JavaScript à inclure.
 */
?>

<footer>
    <p>© 2025 - Tous droits réservés</p>
</footer>

<?php if (isset($jsFiles) && is_array($jsFiles)): ?>
    <?php foreach ($jsFiles as $jsFile): ?>
        <script src="<?= BASE_URL ?>/public/js/<?= $jsFile ?>"></script>
    <?php endforeach; ?>
<?php endif; ?>
</body>
</html>