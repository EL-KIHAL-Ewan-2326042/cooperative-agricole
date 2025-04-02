<?php
require_once 'services/PanierService.php';
require_once 'services/ApiService.php';

class CommandeController {
    private PanierService $panierService;
    private ApiService $apiService;

    public function __construct() {
        $this->panierService = new PanierService();
        $this->apiService = new ApiService();
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
        // Récupérer les données du formulaire
        $panierData = json_decode($_POST['panier_data'] ?? '[]', true);

        // Convertir le panier JS vers un format compatible avec l'API
        $produits = $this->panierService->convertirPanierJsVersApi($panierData);

        $commande = [
            'client' => [
                'nom' => $_POST['nom'] ?? '',
                'prenom' => $_POST['prenom'] ?? '',
                'adresse' => $_POST['adresse'] ?? '',
                'code_postal' => $_POST['code_postal'] ?? '',
                'ville' => $_POST['ville'] ?? '',
                'email' => $_POST['email'] ?? '',
                'telephone' => $_POST['telephone'] ?? '',
            ],
            'produits' => $produits,
            'total' => floatval($_POST['total'] ?? 0),
            'date' => date('Y-m-d H:i:s')
        ];

        // Envoyer la commande à l'API
        $response = $this->apiService->saveOrder($commande);

        if ($response && isset($response['success']) && $response['success']) {

            header('Location: ?route=commande/confirmation&id='.$response['order_id']);
            exit;
        } else {
            header('Location: ?route=commande?error=1');
            exit;
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
        $jsFiles = ['commandes.js', 'historique.js'];

        $clientId = isset($_SESSION['user_id']) ? $_SESSION['user_id'] : null;

        $commandes = [];
        if ($clientId) {
            $commandes = $this->apiService->getCommandesByClient($clientId);
        }

        $commandesJson = json_encode($commandes);

        require_once 'views/templates/header.php';
        require_once 'views/commande/historique.php';
        require_once 'views/templates/footer.php';
    }
}