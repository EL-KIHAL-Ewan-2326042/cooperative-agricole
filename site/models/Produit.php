<?php
require_once 'services/ApiService.php';
class Produit {
    private int $id;
    private string $name;
    private string $type;
    private float $price;
    private int $quantity;
    private string $image;
    private ?array $unite = null;

    public function __construct(array $data) {
        $this->id = $data['id'] ?? 0;
        $this->name = $data['nom'] ?? '';
        $this->type = $data['type'] ?? '';
        $this->price = $data['prix'] ?? 0.0;
        $this->quantity = $data['quantite'] ?? 0;
        $this->image = $data['image'] ?? '';
        $this->unite = $data['unite'] ?? null;
    }

    public function hydrate(array $data): void {
        foreach ($data as $key => $value) {
            $setter = 'set' . ucfirst($key);
            if (method_exists($this, $setter)) {
                $this->$setter($value);
            }
        }
    }

    public function getId(): int {
        return $this->id;
    }

    public function getName(): string {
        return $this->name;
    }

    public function getType(): string {
        return $this->type;
    }

    public function getPrice(): float {
        return $this->price;
    }
    public function getUnite(): ?array {
        return $this->unite;
    }

    public function setUnite(?array $unite): void {
        $this->unite = $unite;
    }

    public function getQuantity(): int {
        return $this->quantity;
    }

    public function getImage(): string {
        return $this->image;
    }

    public function getUnitId(): int {
        return $this->unitId;
    }

    public function setUnitId(int $unitId): void {
        $this->unitId = $unitId;
    }
    public function setId(int $id): void {
        $this->id = $id;
    }

    public function setName(string $name): void {
        $this->name = $name;
    }

    public function setType(string $type): void {
        $this->type = $type;
    }

    public function setPrice(float $price): void {
        $this->price = $price;
    }

    public function setQuantity(int $quantity): void {
        $this->quantity = $quantity;
    }

    public function setImage(string $image): void {
        $this->image = $image;
    }



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