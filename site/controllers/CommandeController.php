<?php
// site/controllers/CommandeController.php
require_once 'services/PanierService.php';

class CommandeController {
    private PanierService $panierService;

    public function __construct() {
        $this->panierService = new PanierService();
    }

    public function index(): void {
        $pageTitle = "Finaliser votre commande - Coopérative Agricole";
        $cssFiles = ['commandes.css', 'style.css'];
        $jsFiles = ['commandes.js'];

        $panier = $this->panierService->getPanierFromLocalStorage();

        require_once 'views/templates/header.php';
        require_once 'views/commande/index.php';
        require_once 'views/templates/footer.php';
    }

    public function valider(): void {
        $success = true;

        if ($success) {
            echo '<script>localStorage.removeItem("panier");</script>';
            header('Location: ?route=commande/confirmation');
        } else {
            header('Location: ?route=commande?error=1');
        }
    }

    public function confirmation(): void {
        $pageTitle = "Commande confirmée - Coopérative Agricole";
        $cssFiles = ['commandes.css', 'style.css'];
        $jsFiles = ['commandes.js'];

        require_once 'views/templates/header.php';
        require_once 'views/commande/confirmation.php';
        require_once 'views/templates/footer.php';
    }

    public function historique(): void {
        $pageTitle = "Historique des commandes - Coopérative Agricole";
        $cssFiles = ['commandes.css', 'historique.css', 'style.css'];
        $jsFiles = ['commandes.js'];

        require_once 'views/templates/header.php';
        require_once 'views/commande/historique.php';
        require_once 'views/templates/footer.php';
    }

}
