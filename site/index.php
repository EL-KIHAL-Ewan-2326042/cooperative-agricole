<?php
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
        $controller->validation();
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
    default:
        header("HTTP/1.0 404 Not Found");
        echo "Page non trouvée";
        break;
}