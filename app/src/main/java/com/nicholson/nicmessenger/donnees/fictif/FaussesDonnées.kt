package com.nicholson.nicmessenger.donnees.fictif

import com.nicholson.nicmessenger.domaine.modele.Conversation
import com.nicholson.nicmessenger.domaine.modele.Message
import com.nicholson.nicmessenger.domaine.modele.Utilisateur
import java.time.LocalDateTime

class FaussesDonnées {
    companion object {
        val listeUtilisateur = listOf(
            Utilisateur(
                id = 1L,
                nomComplet = "Alice Dupont",
                email = "alice.dupont@example.com",
                statut = "online",
                description = "Développeuse passionnée par l'IA.",
                avatar = "https://robohash.org//alice.jpg",
                banniere = "https://example.com/bannieres/alice.jpg"
            ),
            Utilisateur(
                id = 2L,
                nomComplet = "Bob Martin",
                email = "bob.martin@example.com",
                statut = "online",
                description = "Designer graphique avec 10 ans d'expérience.",
                avatar = "https://robohash.org//bob.jpg",
                banniere = "https://example.com/bannieres/bob.jpg"
            ),
            Utilisateur(
                id = 3L,
                nomComplet = "Charlie Leroy",
                email = "charlie.leroy@example.com",
                statut = "disconnected",
                description = "Consultant en marketing numérique.",
                avatar = "assets/images/buddy2",
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

        val listeMessages = listOf(
            Message(
                id = 1L,
                contenu = "Salut! Comment ça va?",
                date = LocalDateTime.now().minusHours(1),
                nomSender = "Alice Dupont",
                type = "texte", // or "image", "audio", etc.
                style = "normal",
                winkName = ""
            ),
            Message(
                id = 2L,
                contenu = "Ça va bien, et toi?",
                date = LocalDateTime.now().minusMinutes(50),
                nomSender = "Bob Martin",
                type = "texte",
                style = "bold",
                winkName = ""
            ),
            Message(
                id = 3L,
                contenu = "Regarde ce gif 😂",
                date = LocalDateTime.now().minusMinutes(30),
                nomSender = "Alice Dupont",
                type = "image",
                style = "italic",
                winkName = "funnyGif"
            ),
            Message(
                id = 4L,
                contenu = "Je l'adore ! 😂",
                date = LocalDateTime.now().minusMinutes(15),
                nomSender = "Bob Martin",
                type = "texte",
                style = "normal",
                winkName = ""
            )
        )

        val listMessagesJson = listOf(
            "{\"id\": 1, \"contenu\": \"Salut! Comment ça va?\", \"date\": \"2024-12-15T15:40:08.840306\", \"nomSender\": \"Alice Dupont\", \"type\": \"texte\", \"style\": \"normal\", \"winkName\": \"\"}",
            "{\"id\": 2, \"contenu\": \"Ça va bien, et toi?\", \"date\": \"2024-12-15T15:50:08.840327\", \"nomSender\": \"Bob Martin\", \"type\": \"texte\", \"style\": \"bold\", \"winkName\": \"\"}",
            "{\"id\": 3, \"contenu\": \"Regarde ce gif 😂\", \"date\": \"2024-12-15T16:10:08.840334\", \"nomSender\": \"Alice Dupont\", \"type\": \"image\", \"style\": \"italic\", \"winkName\": \"funnyGif\"}",
            "{\"id\": 4, \"contenu\": \"Je l'adore ! 😂\", \"date\": \"2024-12-15T16:25:08.840339\", \"nomSender\": \"Bob Martin\", \"type\": \"texte\", \"style\": \"normal\", \"winkName\": \"\"}"
        )
    }
}