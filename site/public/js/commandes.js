document.addEventListener('DOMContentLoaded', function() {
    initialiserCommande();

    setupFormValidation();

    setupPaymentOptions();

    if (window.location.href.includes('confirmation')) {
        afficherConfirmation();
    }

    if (window.location.href.includes('historique')) {
        afficherHistoriqueCommandes();
    }
});

function initialiserCommande() {
    const panier = JSON.parse(localStorage.getItem('panier')) || [];

    if (panier.length === 0 && !window.location.href.includes('confirmation') && !window.location.href.includes('historique')) {
        window.location.href = '?route=panier';
        return;
    }

    if (document.getElementById('commande-items')) {
        const commandeItems = document.getElementById('commande-items');
        commandeItems.innerHTML = '';

        let sousTotal = 0;

        panier.forEach(produit => {
            const totalProduit = produit.prix * produit.quantite;
            sousTotal += totalProduit;

            const itemHTML = `
                <div class="commande-item" data-id="${produit.id}">
                    <img src="${produit.image}" alt="${produit.nom}" loading="lazy">
                    <div class="commande-item-details">
                        <h3>${produit.nom}</h3>
                        <p>${produit.quantite} × ${produit.prix.toFixed(2)} €</p>
                    </div>
                    <span class="commande-item-prix">${totalProduit.toFixed(2)} €</span>
                </div>
            `;

            commandeItems.innerHTML += itemHTML;
        });

        const fraisLivraison = sousTotal > 0 ? 4.99 : 0;
        const total = sousTotal + fraisLivraison;

        if (document.getElementById('recap-sous-total')) {
            document.getElementById('recap-sous-total').textContent = sousTotal.toFixed(2) + ' €';
            document.getElementById('recap-frais-livraison').textContent = fraisLivraison.toFixed(2) + ' €';
            document.getElementById('recap-total').textContent = total.toFixed(2) + ' €';
        }
    }
}

function setupFormValidation() {
    const commandeForm = document.getElementById('commande-form');

    if (!commandeForm) return;

    commandeForm.addEventListener('submit', function(event) {
        event.preventDefault();

        let isValid = true;
        const champsObligatoires = ['nom', 'prenom', 'email', 'telephone', 'adresse', 'code_postal', 'ville'];

        champsObligatoires.forEach(champ => {
            const input = document.getElementById(champ);
            if (!input || !input.value.trim()) {
                isValid = false;
                afficherErreur(input, 'Ce champ est obligatoire');
            } else {
                supprimerErreur(input);
            }
        });

        const emailInput = document.getElementById('email');
        if (emailInput && emailInput.value.trim() && !validateEmail(emailInput.value)) {
            isValid = false;
            afficherErreur(emailInput, 'Veuillez entrer une adresse email valide');
        }

        if (isValid) {
            const formData = new FormData(commandeForm);

            sauvegarderCommande(formData);

            window.location.href = '?route=commande/confirmation';
        }
    });
}

function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

function supprimerErreur(input) {
    const errorElement = input.parentNode.querySelector('.error-message');
    if (errorElement) {
        errorElement.remove();
    }
}

function validateInput(input) {
    const type = input.type;
    const value = input.value.trim();

    const errorElement = input.parentNode.querySelector('.error-message');
    if (errorElement) errorElement.remove();

    let isValid = true;
    let errorMessage = '';

    switch(type) {
        case 'email':
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            isValid = emailRegex.test(value);
            errorMessage = 'Veuillez entrer une adresse email valide';
            break;

        case 'tel':
            const telRegex = /^[0-9]{10}$/;
            isValid = telRegex.test(value);
            errorMessage = 'Veuillez entrer un numéro de téléphone à 10 chiffres';
            break;

        case 'text':
            isValid = value.length > 1;
            errorMessage = 'Ce champ est requis';

            if (input.id === 'code_postal') {
                const codePostalRegex = /^[0-9]{5}$/;
                isValid = codePostalRegex.test(value);
                errorMessage = 'Veuillez entrer un code postal à 5 chiffres';
            }
            break;

        default:
            isValid = value.length > 0;
            errorMessage = 'Ce champ est requis';
    }

    if (!isValid) {
        markAsError(input, errorMessage);
    } else {
        input.classList.remove('input-error');
    }

    return isValid;
}

function markAsError(input, message = 'Ce champ est requis') {
    input.classList.add('input-error');

    const errorElement = document.createElement('div');
    errorElement.className = 'error-message';
    errorElement.textContent = message;

    input.parentNode.appendChild(errorElement);
}

function setupPaymentOptions() {
    const paymentOptions = document.querySelectorAll('.payment-option');

    if (!paymentOptions.length) return;

    paymentOptions[0].classList.add('active');

    paymentOptions.forEach(option => {
        option.addEventListener('click', function() {
            paymentOptions.forEach(opt => {
                opt.classList.remove('active');
                opt.querySelector('input').checked = false;
            });

            this.classList.add('active');
            this.querySelector('input').checked = true;

            this.style.transform = 'scale(1.05)';
            setTimeout(() => {
                this.style.transform = 'scale(1)';
            }, 200);
        });
    });
}

function sauvegarderCommande(formData) {
    const panier = JSON.parse(localStorage.getItem('panier')) || [];

    const commandes = JSON.parse(localStorage.getItem('commandes')) || [];

    const sousTotal = panier.reduce((total, produit) => {
        return total + (produit.prix * produit.quantite);
    }, 0);

    const fraisLivraison = sousTotal > 0 ? 4.99 : 0;
    const total = sousTotal + fraisLivraison;

    const nouvelleCommande = {
        id: Date.now().toString().slice(-6),
        date: new Date().toISOString(),
        client: {
            nom: formData.get('nom'),
            prenom: formData.get('prenom'),
            email: formData.get('email'),
            telephone: formData.get('telephone'),
            adresse: formData.get('adresse'),
            code_postal: formData.get('code_postal'),
            ville: formData.get('ville')
        },
        produits: panier,
        sousTotal: sousTotal,
        fraisLivraison: fraisLivraison,
        total: total,
        paiement: formData.get('paiement') || 'cb',
        statut: 'Livrée'
    };

    localStorage.setItem('derniereCommande', JSON.stringify(nouvelleCommande));

    commandes.push(nouvelleCommande);

    localStorage.setItem('commandes', JSON.stringify(commandes));

    localStorage.removeItem('panier');
}

function afficherConfirmation() {
    const derniereCommande = JSON.parse(localStorage.getItem('derniereCommande'));

    if (!derniereCommande) return;

    const confirmationContainer = document.querySelector('.confirmation-message');

    if (confirmationContainer) {
        const detailsHTML = `
            <p>Commande #${derniereCommande.id}</p>
            <p>Total de la commande: ${derniereCommande.total.toFixed(2)} €</p>
        `;

        const bouton = confirmationContainer.querySelector('.retour-boutique-btn');
        if (bouton) {
            bouton.insertAdjacentHTML('beforebegin', detailsHTML);
        }
    }
}


