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
                    <img src="${produit.image}" alt="${produit.nom}">
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

    commandeForm.addEventListener('submit', function(e) {
        e.preventDefault();

        const errorElements = document.querySelectorAll('.error-message');
        errorElements.forEach(el => el.remove());

        const inputs = commandeForm.querySelectorAll('input[required]');
        inputs.forEach(input => input.classList.remove('input-error'));

        let isValid = true;

        inputs.forEach(input => {
            if (!validateInput(input)) {
                isValid = false;
                markAsError(input);
            }
        });

        if (isValid) {
            const submitBtn = commandeForm.querySelector('button[type="submit"]');
            submitBtn.innerHTML = '<span>Traitement en cours...</span>';
            submitBtn.style.opacity = '0.8';
            submitBtn.style.pointerEvents = 'none';

            const formData = new FormData(commandeForm);

            sauvegarderCommande(formData);

            setTimeout(() => {
                window.location.href = '?route=commande/confirmation';
            }, 800);
        } else {
            commandeForm.classList.add('shake');
            setTimeout(() => {
                commandeForm.classList.remove('shake');
            }, 500);
        }
    });

    const inputs = commandeForm.querySelectorAll('input[required]');
    inputs.forEach(input => {
        input.addEventListener('blur', function() {
            validateInput(input);
        });

        input.addEventListener('input', function() {
            if (input.classList.contains('input-error')) {
                validateInput(input);
            }
        });
    });
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

function afficherHistoriqueCommandes() {
    const commandes = JSON.parse(localStorage.getItem('commandes')) || [];

    const commandesListe = document.getElementById('commandes-liste');
    const commandesVide = document.getElementById('commandes-vide');

    if (!commandesListe || !commandesVide) return;

    if (commandes.length === 0) {
        commandesVide.style.display = 'flex';
        commandesListe.style.display = 'none';
        return;
    }

    commandesVide.style.display = 'none';
    commandesListe.innerHTML = '';

    commandes.sort((a, b) => new Date(b.date) - new Date(a.date));

    commandes.forEach(commande => {
        const commandeDate = new Date(commande.date);
        const dateFormatee = commandeDate.toLocaleDateString('fr-FR', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });

        let produitsHTML = '';
        commande.produits.forEach(produit => {
            produitsHTML += `
                <div class="commande-produit">
                    <img src="${produit.image}" alt="${produit.nom}">
                    <div class="commande-produit-details">
                        <h4>${produit.nom}</h4>
                        <p>${produit.quantite} × ${produit.prix.toFixed(2)} €</p>
                    </div>
                    <div class="commande-produit-prix">${(produit.prix * produit.quantite).toFixed(2)} €</div>
                </div>
            `;
        });

        const commandeCard = document.createElement('div');
        commandeCard.className = 'commande-card';
        commandeCard.innerHTML = `
            <div class="commande-header">
                <h2>Commande #${commande.id}</h2>
                <div class="commande-meta">
                    <span class="commande-date">${dateFormatee}</span>
                    <span class="commande-status">${commande.statut || 'Livrée'}</span>
                </div>
            </div>
            
            <div class="commande-info">
                <div class="commande-adresse">
                    <h3>Adresse de livraison</h3>
                    <p>${commande.client.prenom} ${commande.client.nom}</p>
                    <p>${commande.client.adresse}</p>
                    <p>${commande.client.code_postal} ${commande.client.ville}</p>
                    <p>Email: ${commande.client.email}</p>
                    <p>Tél: ${commande.client.telephone}</p>
                </div>
                
                <div class="commande-produits">
                    <h3>Produits</h3>
                    ${produitsHTML}
                </div>
            </div>
            
            <div class="commande-footer">
                <div class="commande-total">
                    Total: ${commande.total.toFixed(2)} €
                </div>
                <div class="commande-actions">
                    <a href="?route=home" class="reorder-btn">Commander à nouveau</a>
                </div>
            </div>
        `;

        commandesListe.appendChild(commandeCard);
    });
}
