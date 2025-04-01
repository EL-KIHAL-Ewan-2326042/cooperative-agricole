document.addEventListener('DOMContentLoaded', function() {
    afficherHistoriqueCommandes();
});

function afficherHistoriqueCommandes() {
    // Récupérer l'historique des commandes
    const commandes = JSON.parse(localStorage.getItem('commandes')) || [];

    const commandesListe = document.getElementById('commandes-liste');
    const commandesVide = document.getElementById('commandes-vide');

    // Vérifier si l'historique est vide
    if (commandes.length === 0) {
        commandesVide.style.display = 'flex';
        return;
    }

    commandesVide.style.display = 'none';

    // Trier les commandes par date (la plus récente en premier)
    commandes.sort((a, b) => new Date(b.date) - new Date(a.date));

    // Afficher chaque commande
    commandes.forEach(commande => {
        const commandeDate = new Date(commande.date);
        const dateFormatee = commandeDate.toLocaleDateString('fr-FR', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });

        // Générer le HTML pour les produits
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

        // Créer la carte de commande
        const commandeCard = document.createElement('div');
        commandeCard.className = 'commande-card';
        commandeCard.innerHTML = `
            <div class="commande-header">
                <h2>Commande #${commande.id}</h2>
                <div class="commande-meta">
                    <span class="commande-date">${dateFormatee}</span>
                    <span class="commande-status">Livrée</span>
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