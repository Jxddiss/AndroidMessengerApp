package com.nicholson.nicmessenger.donnees.fictif

import com.nicholson.nicmessenger.domaine.modele.Conversation
import com.nicholson.nicmessenger.domaine.modele.Demande
import com.nicholson.nicmessenger.domaine.modele.Message
import com.nicholson.nicmessenger.domaine.modele.Notification
import com.nicholson.nicmessenger.domaine.modele.Style
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
                banniere = "https://images.unsplash.com/photo-1731518416224-f052548cf639?q=80&w=1634&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
            ),
            Utilisateur(
                id = 3L,
                nomComplet = "Charlie Leroy",
                email = "charlie.leroy@example.com",
                statut = "disconnected",
                description = "Consultant en marketing numérique.",
                avatar = "assets/images/buddy2",
                banniere = "https://images.unsplash.com/photo-1730386303229-0c921f5690cd?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
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
                type = "text",
                style = null,
                winkName = "",
                conversation = null
            ),
            Message(
                id = 2L,
                contenu = "Ça va bien, et toi?",
                date = LocalDateTime.now().minusMinutes(50),
                nomSender = "Bob Martin",
                type = "text",
                style = null,
                winkName = "",
                conversation = null
            ),
            Message(
                id = 3L,
                contenu = "Regarde ce gif 😂",
                date = LocalDateTime.now().minusMinutes(30),
                nomSender = "Alice Dupont",
                type = "text",
                style = null,
                winkName = "funnyGif",
                conversation = null
            ),
            Message(
                id = 4L,
                contenu = "Je l'adore ! 😂",
                date = LocalDateTime.now().minusMinutes(15),
                nomSender = "Bob Martin",
                type = "text",
                style = Style("#47AC2A"),
                winkName = "",
                conversation = null
            )
        )

        val notifications = listOf(
            Notification(
                id = 1L,
                titre = "Bob Martin",
                message = "Vous a envoyé un message",
                image = "https://robohash.org//bob.jpg",
                type = "msn",
                lu = false
            ),
            Notification(
                id = 2L,
                titre = "Bob Martin",
                message = "Vous a envoyé un message",
                image = "https://robohash.org//bob.jpg",
                type = "msn",
                lu = true
            ),
            Notification(
                id = 3L,
                titre = "Bob Martin",
                message = "Vous a envoyé un message",
                image = "https://robohash.org//bob.jpg",
                type = "msn",
                lu = false
            ),
            Notification(
                id = 4L,
                titre = "Charlie Leroy",
                message = "Vous a envoyé un message",
                image = "assets/images/buddy2",
                type = "msn",
                lu = false
            ),
            Notification(
                id = 5L,
                titre = "Charlie Leroy",
                message = "Vous a envoyé un message",
                image = "assets/images/buddy2",
                type = "msn",
                lu = false
            )
        )

        val listMessagesJson = mutableListOf(
            "{\"id\": 1, \"contenu\": \"Salut! Comment ça va?\", \"date\": \"2024-12-15T15:40:08.840306\", \"nomSender\": \"Alice Dupont\", \"type\": \"text\", \"winkName\": \"\"}",
            "{\"id\": 2, \"contenu\": \"Ça va bien, et toi?\", \"date\": \"2024-12-15T15:50:08.840327\", \"nomSender\": \"Bob Martin\", \"type\": \"text\", \"winkName\": \"\"}",
            "{\"id\": 3, \"contenu\": \"Regarde ce gif 😂\", \"date\": \"2024-12-15T16:10:08.840334\", \"nomSender\": \"Alice Dupont\", \"type\": \"text\", \"winkName\": \"funnyGif\"}",
            "{\"id\": 4, \"contenu\": \"Je l'adore ! 😂\", \"date\": \"2024-12-15T16:25:08.840339\", \"nomSender\": \"Bob Martin\", \"type\": \"text\", \"style\": \"\"{\"color\":\"#f95353\",\"fontSize\":\"1.5rem\",\"fontWeight\":\"\",\"fontFamily\":\"Roboto\",\"textShadow\":\"none\"}\"\", \"winkName\": \"\"}"
        )

        val demandes = mutableListOf(
            Demande( 1, listeUtilisateur[2], "en attente", false ),
            Demande( 2, listeUtilisateur[2], "en attente", false ),
            Demande( 3, listeUtilisateur[2], "en attente", false )
        )
    }
}