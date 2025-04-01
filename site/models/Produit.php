<?php
class Produit {
    private int $id;
    private string $nom;
    private string $type;
    private float $prix;
    private int $quantite;
    private string $image;
    private string $unite_id;

    public function __construct(array $data = []) {
        if (!empty($data)) {
            $this->hydrate($data);
        }
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

    public function getNom(): string {
        return $this->nom;
    }

    public function getType(): string {
        return $this->type;
    }

    public function getPrix(): float {
        return $this->prix;
    }

    public function getQuantite(): int {
        return $this->quantite;
    }

    public function getImage(): string {
        return $this->image;
    }

    public function getUniteId(): string {
        return $this->unite_id;
    }

    public function setId(int $id): void {
        $this->id = $id;
    }

    public function setNom(string $nom): void {
        $this->nom = $nom;
    }

    public function setType(string $type): void {
        $this->type = $type;
    }

    public function setPrix(float $prix): void {
        $this->prix = $prix;
    }

    public function setQuantite(int $quantite): void {
        $this->quantite = $quantite;
    }

    public function setImage(string $image): void {
        $this->image = $image;
    }

    public function setUnite_id(string $unite_id): void {
        $this->unite_id = $unite_id;
    }

    public function toArray(): array {
        return [
            'id' => $this->id,
            'nom' => $this->nom,
            'type' => $this->type,
            'prix' => $this->prix,
            'quantite' => $this->quantite,
            'image' => $this->image,
            'unite_id' => $this->unite_id
        ];
    }
}