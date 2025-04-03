<?php
/**
 * Page d'historique des commandes.
 *
 * @var string $commandesJson JSON des commandes passées.
 */
$commandesJson = $commandesJson ?? '[]';
?>

<main class="commandes-historique-container">
    <h1>Historique des commandes</h1>

    <div id="commandes-liste">
        <!-- Les commandes passées seront insérées ici par JS -->
    </div>

    <div id="commandes-vide" class="commandes-vide" style="display: none;">
        <img src="<?= BASE_URL ?>/public/media/panier-vide.png" alt="Aucune commande">
        <p>Vous n'avez pas encore passé de commande</p>
        <a href="<?= BASE_URL ?>?route=home" class="retour-boutique-btn">Retourner à la boutique</a>
    </div>
    <script>
        /**
         * Liste des commandes en format JSON.
         */
        const commandes = <?php echo $commandesJson; ?>;
    </script>
</main>