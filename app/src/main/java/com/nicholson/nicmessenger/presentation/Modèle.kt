package com.nicholson.nicmessenger.presentation

import com.nicholson.nicmessenger.domaine.modele.Conversation
import com.nicholson.nicmessenger.domaine.modele.Message
import com.nicholson.nicmessenger.domaine.modele.Utilisateur
import com.nicholson.nicmessenger.domaine.service.Authentification
import com.nicholson.nicmessenger.domaine.service.EnvoyerMessage
import com.nicholson.nicmessenger.domaine.service.ObtenirConversations
import com.nicholson.nicmessenger.domaine.service.ObtenirMessages
import kotlinx.coroutines.flow.Flow

class Modèle private constructor() : IModèle {
    companion object {
        @Volatile
        private var instance : Modèle? = null

        fun obtenirInstance() =
            instance ?: synchronized(this) {
                instance ?: Modèle().also { instance = it }
            }
    }

    override var estConnecté = false
    override var utilisateurConnecté : Utilisateur? = null
    override var montrerNavUnit : (() -> Unit)? = null
    override var cacherNavUnit : (() -> Unit)? = null
    override var conversations: List<Conversation> = listOf()
    override var indiceConversationCourrante: Int = 0
    override var conversationCourrante : Conversation? = null

    override suspend fun seConnecter( email: String, motDePasse: String ) {
        utilisateurConnecté = Authentification.seConnecter( email, motDePasse )
        estConnecté = true
    }

    override suspend fun demandeMotDePasseOublié( email: String ) {
        Authentification.demandeMotDePasseOublié( email )
    }

    override suspend fun obtenirMesConversations(): List<Conversation> {
        utilisateurConnecté?.let {
            conversations =  ObtenirConversations.obtenirMesConversations( it.id )
        }
        return conversations
    }

    override suspend fun obtenirConversationCourrante(): Conversation {
        if( conversationCourrante != null
            && conversations[indiceConversationCourrante].id == conversationCourrante?.id ) {
            return conversationCourrante as Conversation
        } else {
            return ObtenirConversations.obtenirConversationParId( conversations[indiceConversationCourrante].id )
        }
    }

    override fun cacherNav() {
        cacherNavUnit?.let { it() }
    }

    override suspend fun obtenirMessagesPrécédent(): List<Message> {
        return ObtenirMessages.obtenirMessagesPrécédents( conversationCourrante?.id ?: 0L )
    }

    override suspend fun subscribeMessage( topic: String ): Flow<Message> {
        return ObtenirMessages.messageListener( topic )
    }

    override suspend fun envoyerMessage(destination: String, contenu : String) {
        EnvoyerMessage.envoyerMessage(
            destination = destination,
            contenu = contenu,
            nomSender = utilisateurConnecté?.nomComplet ?: "" )
    }
}