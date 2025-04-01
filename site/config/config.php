<?php
// Détection automatique du chemin de base
$scriptDir = str_replace('\\', '/', dirname($_SERVER['SCRIPT_NAME']));
if (basename($scriptDir) === 'site') {
    // Si nous sommes dans le dossier site
    define('BASE_URL', $scriptDir);
} else {
    // Si nous sommes à la racine du projet
    define('BASE_URL', $scriptDir . '/site');
}

define('PUBLIC_PATH', BASE_URL . '/public');

// Chemins absolus
define('ROOT_PATH', dirname(__DIR__));
define('VIEW_PATH', ROOT_PATH . '/views');
define('CONTROLLER_PATH', ROOT_PATH . '/controllers');
define('MODEL_PATH', ROOT_PATH . '/models');
define('SERVICE_PATH', ROOT_PATH . '/services');