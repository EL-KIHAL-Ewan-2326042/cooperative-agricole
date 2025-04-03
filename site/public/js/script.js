function showModal(id) {
    const produit = produits.find(p => p.id === id);

    document.getElementById("modal-title").innerHTML = produit.nom + '<br>' + '<span class="product-type">Type : ' + produit.type + '</span></h3>';
    document.getElementById("modal-title").setAttribute("data-product-id", produit.id);
    document.getElementById("modal-image").innerHTML = '<img src="' + produit.image + '" alt="' + produit.nom + '" loading="lazy">';
    document.getElementById("modal-description").innerHTML =
        'Prix : ' + produit.prix + ' €/' + produit.nom_unite + '<br>' +
        'Quantité disponible : ' + produit.quantite;

    document.getElementById("quantity-label").innerHTML = 'Quantité (' + produit.symbole_unite + ') :';

    const quantityInput = document.getElementById("quantity");
    quantityInput.value = "1";
    quantityInput.max = produit.quantite;

    updateTotal();

    const modal = document.getElementById("productModal");
    modal.style.display = "block";
    void modal.offsetWidth;
    modal.classList.add("show");
}

function updateQuantity(change) {
    const quantityInput = document.getElementById("quantity");
    const currentValue = Number(quantityInput.value);
    const maxValue = Number(quantityInput.max);
    const newValue = currentValue + change;

    if (newValue >= 1 && newValue <= maxValue) {
        quantityInput.value = newValue;
        updateTotal();
    }
}
function updateTotal() {
    const id = getCurrentProduitId();
    const produit = produits.find(p => p.id === id);

    const quantityInput = document.getElementById("quantity");
    let quantity = Number(quantityInput.value);

    const maxQuantity = Number(quantityInput.max);
    if (quantity > maxQuantity) {
        quantity = maxQuantity;
        quantityInput.value = maxQuantity;
    }

    if (quantity < 1 || isNaN(quantity)) {
        quantity = 1;
        quantityInput.value = 1;
    }

    const totalPrice = (produit.prix * quantity).toFixed(2);
    document.getElementById("total-price").innerText = totalPrice + " €";
}

function getCurrentProduitId() {
    return Number(document.getElementById("modal-title").getAttribute("data-product-id"));
}
function closeModal() {
    const modal = document.getElementById("productModal");
    const modalContent = modal.querySelector(".modal-content");

    void modalContent.offsetWidth;

    modalContent.classList.add("hide");

    setTimeout(() => {
        modal.classList.remove("show");
        modalContent.classList.remove("hide");
        modal.style.display = "none";
    }, 450);
}


function ajouterPanier() {
    const id = getCurrentProduitId();
    const produit = produits.find(p => p.id === id);
    const image = document.getElementById("modal-image").querySelector("img").src;
    const quantity = Number(document.getElementById("quantity").value);

    const prixTotal = (produit.prix * quantity).toFixed(2);

    let message = "";
    if (produit.unite && produit.unite.nom) {
        message = quantity + " " + produit.unite.nom + (quantity > 1 ? 's' : '');
    } else {
        message = quantity + " unité" + (quantity > 1 ? 's' : '');
    }

    let panier = JSON.parse(localStorage.getItem('panier')) || [];

    const produitExistant = panier.find(p => p.id === produit.id);

    if (produitExistant) {
        const nouvelleQuantite = Math.min(produitExistant.quantite + quantity, produit.quantite);
        produitExistant.quantite = nouvelleQuantite;
    } else {
        // Ajouter le nouveau produit
        panier.push({
            id: produit.id,
            nom: produit.nom,
            type: produit.type,
            prix: produit.prix,
            unite_id: produit.unite_id,
            quantite: quantity,
            image: image,
            stockDisponible: produit.quantite,
            unite: produit.unite
        });
    }

    localStorage.setItem('panier', JSON.stringify(panier));

    document.getElementById("notification-img").src = image;
    document.getElementById("notification-title").innerText = produit.nom;
    document.getElementById("notification-desc").innerHTML =
        message + " ajouté" + (quantity > 1 ? 's' : '') + " à votre panier" +
        '<div class="notification-price">' + prixTotal + ' €</div>';

    closeModal();

    const notification = document.getElementById("notification");
    const overlay = document.getElementById("overlay-blur");
    notification.classList.remove("show");
    notification.classList.remove("hide");
    overlay.classList.remove("show");

    const buySound = new Audio('/site/public/media/buy-sound.mp3');
    buySound.volume = 0.5;
    buySound.play().catch(e => console.log("Erreur de lecture audio:", e));

    void notification.offsetWidth;

    setTimeout(() => {
        overlay.classList.add("show");
        notification.classList.add("show");

        setTimeout(() => {
            notification.classList.add("hide");

            setTimeout(() => {
                notification.classList.remove("show");
                notification.classList.remove("hide");
                overlay.classList.remove("show");
            }, 450);
        }, 900);
    }, 100);
}



