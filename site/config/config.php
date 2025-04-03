<?php
// Détection automatique du chemin de base

/**
 * Détecte et définit le chemin de base de l'application.
 */
$scriptDir = str_replace('\\', '/', dirname($_SERVER['SCRIPT_NAME']));
if (basename($scriptDir) === 'site') {
    // Si nous sommes dans le dossier site
    define('BASE_URL', $scriptDir);
} else {
    // Si nous sommes à la racine du projet
    define('BASE_URL', $scriptDir . '/site');
}

/**
 * Définit le chemin public de l'application.
 */
define('PUBLIC_PATH', BASE_URL . '/public');

// Chemins absolus

/**
 * Définit le chemin racine de l'application.
 */
define('ROOT_PATH', dirname(__DIR__));

/**
 * Définit le chemin des vues de l'application.
 */
define('VIEW_PATH', ROOT_PATH . '/views');

/**
 * Définit le chemin des contrôleurs de l'application.
 */
define('CONTROLLER_PATH', ROOT_PATH . '/controllers');

/**
 * Définit le chemin des modèles de l'application.
 */
define('MODEL_PATH', ROOT_PATH . '/models');

/**
 * Définit le chemin des services de l'application.
 */
define('SERVICE_PATH', ROOT_PATH . '/services');