<?php
require_once 'services/ApiService.php';

/**
 * Classe représentant un produit.
 */
class Produit {
    /**
     * @var int L'ID du produit.
     */
    private int $id;

    /**
     * @var string Le nom du produit.
     */
    private string $name;

    /**
     * @var string Le type du produit.
     */
    private string $type;

    /**
     * @var float Le prix du produit.
     */
    private float $price;

    /**
     * @var int La quantité du produit.
     */
    private int $quantity;

    /**
     * @var string L'image du produit.
     */
    private string $image;

    /**
     * @var array|null Les informations sur l'unité du produit.
     */
    private ?array $unite = null;

    /**
     * Constructeur de la classe Produit.
     *
     * @param array $data Les données pour initialiser le produit.
     */
    public function __construct(array $data) {
        $this->id = $data['id'] ?? 0;
        $this->name = $data['nom'] ?? '';

        // Gérer le cas où type est un tableau
        if (isset($data['type']) && is_array($data['type'])) {
            // Utiliser le premier élément ou une autre logique selon vos besoins
            $this->type = isset($data['type']['nom']) ? $data['type']['nom'] : '';
        } else {
            $this->type = $data['type'] ?? '';
        }

        $this->price = $data['prix'] ?? 0.0;
        $this->quantity = $data['quantite'] ?? 0;
        $this->image = $data['image'] ?? '';
        $this->unite = $data['unite'] ?? null;
    }

    /**
     * Hydrate l'objet avec des données.
     *
     * @param array $data Les données pour hydrater l'objet.
     */
    public function hydrate(array $data): void {
        foreach ($data as $key => $value) {
            $setter = 'set' . ucfirst($key);
            if (method_exists($this, $setter)) {
                $this->$setter($value);
            }
        }
    }

    /**
     * Retourne l'ID du produit.
     *
     * @return int L'ID du produit.
     */
    public function getId(): int {
        return $this->id;
    }

    /**
     * Retourne le nom du produit.
     *
     * @return string Le nom du produit.
     */
    public function getName(): string {
        return $this->name;
    }

    /**
     * Retourne le type du produit.
     *
     * @return string Le type du produit.
     */
    public function getType(): string {
        return $this->type;
    }

    /**
     * Définit le type du produit.
     *
     * @param string $type Le type du produit.
     */
    public function setType(string $type): void {
        $this->type = $type;
    }

    /**
     * Retourne le prix du produit.
     *
     * @return float Le prix du produit.
     */
    public function getPrice(): float {
        return $this->price;
    }

    /**
     * Retourne les informations sur l'unité du produit.
     *
     * @return array|null Les informations sur l'unité du produit.
     */
    public function getUnite(): ?array {
        return $this->unite;
    }

    /**
     * Définit les informations sur l'unité du produit.
     *
     * @param array|null $unite Les informations sur l'unité du produit.
     */
    public function setUnite(?array $unite): void {
        $this->unite = $unite;
    }

    /**
     * Retourne la quantité du produit.
     *
     * @return int La quantité du produit.
     */
    public function getQuantity(): int {
        return $this->quantity;
    }

    /**
     * Retourne l'image du produit.
     *
     * @return string L'image du produit.
     */
    public function getImage(): string {
        return $this->image;
    }

    /**
     * Retourne l'ID de l'unité du produit.
     *
     * @return int L'ID de l'unité du produit.
     */
    public function getUnitId(): int {
        return $this->unitId;
    }

    /**
     * Définit l'ID de l'unité du produit.
     *
     * @param int $unitId L'ID de l'unité du produit.
     */
    public function setUnitId(int $unitId): void {
        $this->unitId = $unitId;
    }

    /**
     * Définit l'ID du produit.
     *
     * @param int $id L'ID du produit.
     */
    public function setId(int $id): void {
        $this->id = $id;
    }

    /**
     * Définit le nom du produit.
     *
     * @param string $name Le nom du produit.
     */
    public function setName(string $name): void {
        $this->name = $name;
    }

    /**
     * Définit le prix du produit.
     *
     * @param float $price Le prix du produit.
     */
    public function setPrice(float $price): void {
        $this->price = $price;
    }

    /**
     * Définit la quantité du produit.
     *
     * @param int $quantity La quantité du produit.
     */
    public function setQuantity(int $quantity): void {
        $this->quantity = $quantity;
    }

    /**
     * Définit l'image du produit.
     *
     * @param string $image L'image du produit.
     */
    public function setImage(string $image): void {
        $this->image = $image;
    }

    /**
     * Convertit le produit en tableau.
     *
     * @return array Le produit sous forme de tableau.
     */
    public function toArray(): array {
        static $apiService = null;
        if ($apiService === null) {
            $apiService = new ApiService();
        }

        // Récupérer les infos unité directement pour ce produit
        $uniteInfo = $apiService->getUniteForProduit($this->id);

        return [
            'id' => $this->id,
            'nom' => $this->name,
            'type' => $this->type,
            'prix' => $this->price,
            'quantite' => $this->quantity,
            'image' => $this->image,
            'unite' => $uniteInfo,
            'symbole_unite' => $uniteInfo['symbole'] ?? 'u',
            'nom_unite' => $uniteInfo['nom'] ?? 'unité'
        ];
    }
}