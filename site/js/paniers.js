document.addEventListener('DOMContentLoaded', function() {
    afficherPanier();
});

function afficherPanier() {
    // Récupérer le panier depuis localStorage
    const panier = JSON.parse(localStorage.getItem('panier')) || [];

    const panierVide = document.getElementById('panier-vide');
    const panierContenu = document.getElementById('panier-contenu');
    const panierItems = document.querySelector('.panier-items');

    // Vider le contenu précédent
    panierItems.innerHTML = '';

    if (panier.length === 0) {
        panierVide.style.display = 'flex';
        panierContenu.style.display = 'none';
        return;
    }

    panierVide.style.display = 'none';
    panierContenu.style.display = 'flex';

    // Afficher chaque produit du panier
    panier.forEach(produit => {
        const itemHTML = `
            <div class="panier-item" data-id="${produit.id}">
                <div class="panier-item-img">
                    <img src="${produit.image}" alt="${produit.nom}">
                </div>
                <div class="panier-item-info">
                    <h3>${produit.nom}</h3>
                    <p class="panier-item-type">Type: ${produit.type}</p>
                    <p class="panier-item-prix">${produit.prix} €/${produit.unite_id}</p>
                </div>
                <div class="panier-item-quantite">
                    <button class="quantite-btn moins" onclick="modifierQuantite(${produit.id}, -1)">-</button>
                    <input type="number" value="${produit.quantite}" min="1" max="${produit.stockDisponible}"
                           onchange="miseAJourQuantite(${produit.id}, this.value)">
                    <button class="quantite-btn plus" onclick="modifierQuantite(${produit.id}, 1)">+</button>
                </div>
                <div class="panier-item-total">
                    <span>${(produit.prix * produit.quantite).toFixed(2)} €</span>
                </div>
                <button class="supprimer-btn" onclick="supprimerDuPanier(${produit.id})">
                    <span>&times;</span>
                </button>
            </div>
        `;
        panierItems.innerHTML += itemHTML;
    });

    // Mettre à jour les totaux
    calculerTotaux();
}

function modifierQuantite(id, delta) {
    const panier = JSON.parse(localStorage.getItem('panier')) || [];
    const produit = panier.find(p => p.id === id);

    if (produit) {
        const nouvelleQuantite = produit.quantite + delta;

        if (nouvelleQuantite >= 1 && nouvelleQuantite <= produit.stockDisponible) {
            produit.quantite = nouvelleQuantite;
            localStorage.setItem('panier', JSON.stringify(panier));
            afficherPanier();
        }
    }
}

function miseAJourQuantite(id, nouvelleValeur) {
    const panier = JSON.parse(localStorage.getItem('panier')) || [];
    const produit = panier.find(p => p.id === id);

    if (produit) {
        let valeur = parseInt(nouvelleValeur);

        if (isNaN(valeur) || valeur < 1) {
            valeur = 1;
        } else if (valeur > produit.stockDisponible) {
            valeur = produit.stockDisponible;
        }

        produit.quantite = valeur;
        localStorage.setItem('panier', JSON.stringify(panier));
        afficherPanier();
    }
}

function supprimerDuPanier(id) {
    let panier = JSON.parse(localStorage.getItem('panier')) || [];
    panier = panier.filter(p => p.id !== id);
    localStorage.setItem('panier', JSON.stringify(panier));
    afficherPanier();
}

function calculerTotaux() {
    const panier = JSON.parse(localStorage.getItem('panier')) || [];

    const sousTotal = panier.reduce((total, produit) => {
        return total + (produit.prix * produit.quantite);
    }, 0);

    const fraisLivraison = sousTotal > 0 ? 4.99 : 0;
    const total = sousTotal + fraisLivraison;

    document.getElementById('sous-total').textContent = sousTotal.toFixed(2) + ' €';
    document.getElementById('frais-livraison').textContent = fraisLivraison.toFixed(2) + ' €';
    document.getElementById('total-panier').textContent = total.toFixed(2) + ' €';
}

document.getElementById('passer-commande').addEventListener('click', function() {
    window.location.href = 'commande.php';
});