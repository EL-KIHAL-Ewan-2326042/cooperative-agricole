function showModal(id) {
    const produit = produits.find(p => p.id === id);
    document.getElementById("modal-title").innerText = produit.nom;
    document.getElementById("modal-image").innerHTML = `<img src="${produit.image}" alt="${produit.nom}">`;
    document.getElementById("modal-description").innerHTML = `
        Type : ${produit.type}<br>
        Prix : ${produit.prix} €/u<br>
        Quantité disponible : ${produit.quantite}
    `;
    document.getElementById("productModal").style.display = "block";
}

function closeModal() {
    document.getElementById("productModal").style.display = "none";
}

function ajouterPanier() {
    alert("Produit ajouté au panier !");
    closeModal();
}