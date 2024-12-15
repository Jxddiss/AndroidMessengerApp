package com.nicholson.nicmessenger.donnees.fictif

import com.nicholson.nicmessenger.domaine.modele.Conversation
import com.nicholson.nicmessenger.domaine.modele.Utilisateur

class FaussesDonnées {
    companion object {
        val listeUtilisateur = listOf(
            Utilisateur(
                id = 1L,
                nomComplet = "Alice Dupont",
                email = "alice.dupont@example.com",
                statut = "Actif",
                description = "Développeuse passionnée par l'IA.",
                avatar = "https://robohash.org//alice.jpg",
                banniere = "https://example.com/bannieres/alice.jpg"
            ),
            Utilisateur(
                id = 2L,
                nomComplet = "Bob Martin",
                email = "bob.martin@example.com",
                statut = "Actif",
                description = "Designer graphique avec 10 ans d'expérience.",
                avatar = "https://robohash.org//bob.jpg",
                banniere = "https://example.com/bannieres/bob.jpg"
            ),
            Utilisateur(
                id = 3L,
                nomComplet = "Charlie Leroy",
                email = "charlie.leroy@example.com",
                statut = "Inactif",
                description = "Consultant en marketing numérique.",
                avatar = "https://robohash.org//charlie.jpg",
                banniere = "https://example.com/bannieres/charlie.jpg"
            )
        )


        val listeConversations = listOf(
            Conversation(
                id = 1L,
                utilisateurs = setOf( listeUtilisateur[0], listeUtilisateur[1] )
            ),
            Conversation(
                id = 2L,
                utilisateurs = setOf( listeUtilisateur[0], listeUtilisateur[2] )
            ),
            Conversation(
                id = 3L,
                utilisateurs = setOf( listeUtilisateur[1], listeUtilisateur[2] )
            )
        )
    }
}