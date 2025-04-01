<?php
?>

<main class="confirmation-container">
    <div class="confirmation-message">
        <h1>Commande confirmée !</h1>
        <p>Votre commande a été enregistrée avec succès. Vous recevrez un email de confirmation prochainement.</p>
        <a href="<?= BASE_URL ?>?route=commande/historique" class="retour-boutique-btn">Voir mes commandes</a>
    </div>
</main>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const confirmationSound = new Audio('/site/public/media/alert.mp3');
        confirmationSound.volume = 0.05;
        confirmationSound.play().catch(e => console.log("Erreur de lecture audio:", e));
    });
</script>