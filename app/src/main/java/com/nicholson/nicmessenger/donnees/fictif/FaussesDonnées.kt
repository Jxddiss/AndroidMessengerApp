package com.nicholson.nicmessenger.donnees.fictif

import com.nicholson.nicmessenger.domaine.modele.Utilisateur

class FaussesDonnées {
    companion object {
        var listeUtilisateur = listOf(
            Utilisateur(
                id = 1,
                nomComplet = "Alice Dupont",
                email = "alice.dupont@example.com",
                statut = "Actif",
                description = "Développeuse passionnée par l'IA.",
                avatar = "https://example.com/avatars/alice.jpg",
                banniere = "https://example.com/bannieres/alice.jpg"
            ),
            Utilisateur(
                id = 2,
                nomComplet = "Bob Martin",
                email = "bob.martin@example.com",
                statut = "Actif",
                description = "Designer graphique avec 10 ans d'expérience.",
                avatar = "https://example.com/avatars/bob.jpg",
                banniere = "https://example.com/bannieres/bob.jpg"
            ),
            Utilisateur(
                id = 3,
                nomComplet = "Charlie Leroy",
                email = "charlie.leroy@example.com",
                statut = "Inactif",
                description = "Consultant en marketing numérique.",
                avatar = "https://example.com/avatars/charlie.jpg",
                banniere = "https://example.com/bannieres/charlie.jpg"
            )
        )
    }
}