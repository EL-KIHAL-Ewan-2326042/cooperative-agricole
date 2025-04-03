<?php
/**
 * Point d'entrée principal de l'application.
 *
 * Ce fichier gère les routes et appelle les contrôleurs appropriés en fonction de la route demandée.
 *
 * @package Application
 */

require_once 'config/config.php';

$route = $_GET['route'] ?? 'home';

$GLOBALS['route'] = $route;

switch ($route) {
    case 'home':
        require_once 'controllers/HomeController.php';
        $controller = new HomeController();
        $controller->index();
        break;
    case 'panier':
        require_once 'controllers/PanierController.php';
        $controller = new PanierController();
        $controller->index();
        break;
    case 'commande':
        require_once 'controllers/CommandeController.php';
        $controller = new CommandeController();
        $controller->index();
        break;
    case 'commande/validation':
        require_once 'controllers/CommandeController.php';
        $controller = new CommandeController();
        $controller->valider();
        break;
    case 'commande/confirmation':
        require_once 'controllers/CommandeController.php';
        $controller = new CommandeController();
        $controller->confirmation();
        break;
    case 'commande/historique':
        require_once 'controllers/CommandeController.php';
        $controller = new CommandeController();
        $controller->historique();
        break;
    case 'image_upload':
        require_once 'controllers/ImageUploadController.php';
        $controller = new ImageUploadController();
        $controller->index();
        break;
    case 'image_upload/upload':
        require_once 'controllers/ImageUploadController.php';
        $controller = new ImageUploadController();
        $controller->upload();
        break;
    default:
        header("HTTP/1.0 404 Not Found");
        echo "Page non trouvée";
        break;
}