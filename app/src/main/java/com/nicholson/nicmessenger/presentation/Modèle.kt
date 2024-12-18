package com.nicholson.nicmessenger.presentation

import com.nicholson.nicmessenger.domaine.modele.Conversation
import com.nicholson.nicmessenger.domaine.modele.Message
import com.nicholson.nicmessenger.domaine.modele.Utilisateur
import com.nicholson.nicmessenger.domaine.service.Authentification
import com.nicholson.nicmessenger.domaine.service.EnvoyerMessage
import com.nicholson.nicmessenger.domaine.service.ManipulerStatut
import com.nicholson.nicmessenger.domaine.service.ObtenirConversations
import com.nicholson.nicmessenger.domaine.service.ObtenirMessages
import com.nicholson.nicmessenger.domaine.service.ObtenirStatus
import com.nicholson.nicmessenger.donnees.http.ClientHttp
import com.nicholson.nicmessenger.donnees.websocket.StompClientInstance
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
    override var token: String? = null
    override var seDéconnecter: (() -> Unit)? = null
    override var currentStatus: String? = null

    override suspend fun seConnecter( email: String, motDePasse: String ){
        val (tokenObtenue, utilisateur) = Authentification.seConnecter( email, motDePasse )
        token = tokenObtenue
        utilisateurConnecté = utilisateur
        estConnecté = true
        currentStatus = "online"
    }

    override suspend fun envoyerStatut() {
        utilisateurConnecté?.let { utilisateur ->
            currentStatus?.let {
                ManipulerStatut.envoyerStatut( utilisateur.id, it )
            }
        }
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
        conversationCourrante = conversations[indiceConversationCourrante]
        return conversationCourrante as Conversation
    }

    override fun cacherNav() {
        cacherNavUnit?.let { it() }
    }

    override fun seDéconnecter() {
        seDéconnecter?.let { it() }
        estConnecté = false
        ClientHttp.retirerIntercepteurs()
    }

    override fun mettreÀJourStatusAmi(status: String, position: Int ) {
        conversations[position].utilisateurs.first {
            it.id != utilisateurConnecté?.id
        }.statut = status
    }

    override suspend fun obtenirMessagesPrécédent(): List<Message> {
        return ObtenirMessages.obtenirMessagesPrécédents( conversationCourrante?.id ?: 0L )
    }

    override suspend fun subscribeMessage( topic: String ): Flow<Message> {
        return ObtenirMessages.messageListener( topic )
    }

    override suspend fun envoyerMessage( destination: String, contenu : String, type : String ) {
        EnvoyerMessage.envoyerMessage(
            destination = destination,
            contenu = contenu,
            nomSender = utilisateurConnecté?.nomComplet ?: "",
            type = type,
            idConv = conversationCourrante?.id ?: 0L )
    }

    override suspend fun subscribeStatus( topic: String ): Flow<String> {
        return ObtenirStatus.subscribeStatus( topic )
    }

}