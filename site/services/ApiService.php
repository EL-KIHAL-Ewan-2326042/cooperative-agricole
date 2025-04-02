<?php

class ApiService {
    private string $apiUrl;
    private bool $debug = true;

    public function __construct() {
        $this->apiUrl = 'http://79.72.25.28:8080/api';
    }

    /**
     * Get all products
     */
    public function getProduits(): array {
        $url = $this->apiUrl . '/produits';
        $response = $this->makeRequest('GET', $url);

        if ($response) {
            return isset($response['data']) ? $response['data'] : $response;
        }

        error_log("Failed to retrieve products from API");
        return [];
    }

    /**
     * Récupère les informations d'unité pour un produit spécifique
     */
    public function getUniteForProduit(int $produitId): ?array {
        $produit = $this->getProduitById($produitId);

        if ($produit && isset($produit['unite'])) {
            return $produit['unite'];
        }

        return null;
    }

    /**
     * Get product by ID
     */
    public function getProduitById(int $id): ?array {
        $url = $this->apiUrl . '/produits/' . $id;
        $response = $this->makeRequest('GET', $url);

        if ($response) {
            return isset($response['data']) ? $response['data'] : $response;
        }

        return null;
    }

    /**
     * Search products by name
     */
    public function searchProduits(string $term): array {
        $url = $this->apiUrl . '/produits/search?nom=' . urlencode($term);
        $response = $this->makeRequest('GET', $url);

        if ($response && isset($response['data'])) {
            return $response['data'];
        }

        return [];
    }

    /**
     * Get product image URL
     */
    public function getImageUrl(int $produitId): string {
        return $this->apiUrl . '/images/produit/' . $produitId;
    }

    /**
     * Check product availability
     */
    public function checkProduitAvailability(array $produits): array {
        $url = $this->apiUrl . '/produits/verifier-disponibilite';
        return $this->makeRequest('POST', $url, ['produits' => $produits]) ?? ['disponible' => false];
    }

    /**
     * Save order
     */
    public function saveOrder(array $order): ?array {
        $url = $this->apiUrl . '/commandes';
        return $this->makeRequest('POST', $url, $order);
    }

    /**
     * Make API request
     */
    private function makeRequest(string $method, string $url, array $data = null) {
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_FOLLOWLOCATION, true);
        curl_setopt($ch, CURLOPT_TIMEOUT, 30);
        curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_1_1);

        if ($method === 'POST') {
            curl_setopt($ch, CURLOPT_POST, 1);
            curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
            curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);
        }

        $response = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        $error = curl_error($ch);

        if ($this->debug) {
            $info = curl_getinfo($ch);
            error_log("cURL Info: " . json_encode($info));
        }

        curl_close($ch);

        if ($error) {
            error_log("cURL Error: " . $error);
            return null;
        }

        $decoded = json_decode($response, true);

        if (json_last_error() !== JSON_ERROR_NONE) {
            error_log("Non-JSON response: " . substr($response, 0, 1000));
            if ($httpCode >= 200 && $httpCode < 300) {
                return ['raw_response' => $response];
            }
            return null;
        }

        if ($httpCode >= 200 && $httpCode < 300) {
            return $decoded;
        }

        error_log("API responded with HTTP code: " . $httpCode . ", response: " . substr($response, 0, 1000));
        return null;
    }
}