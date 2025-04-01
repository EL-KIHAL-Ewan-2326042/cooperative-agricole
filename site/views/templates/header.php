<?php
$route = $GLOBALS['route'] ?? '';
?>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?= $pageTitle ?? 'Coopérative Agricole' ?></title>
    <?php if (isset($cssFiles) && is_array($cssFiles)): ?>
        <?php foreach ($cssFiles as $cssFile): ?>
            <!-- Ajout d'un commentaire pour identifier le CSS -->
            <!-- Chargement de <?= $cssFile ?> -->
            <link rel="stylesheet" href="<?= BASE_URL ?>/public/css/<?= $cssFile ?>?v=<?= time() ?>"
                  onerror="console.error('Erreur de chargement de <?= $cssFile ?>')">
        <?php endforeach; ?>
    <?php else: ?>
        <link rel="stylesheet" href="<?= BASE_URL ?>/public/css/style.css?v=<?= time() ?>">
    <?php endif; ?>
</head>
<body>
<header>
    <h1>ANARCHY ACRES</h1>
    <nav>
        <a href="<?= BASE_URL ?>?route=home" <?= ($route === 'home') ? 'class="active"' : '' ?>>Accueil</a>
        <a href="<?= BASE_URL ?>?route=panier" <?= ($route === 'panier') ? 'class="active"' : '' ?>>Voir les paniers</a>
        <a class="login-icon" href="<?= BASE_URL ?>?route=profil">Connexion / Inscription</a>
    </nav>
</header>