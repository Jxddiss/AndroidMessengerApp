package com.nicholson.nicmessenger.presentation

import com.nicholson.nicmessenger.domaine.modele.Conversation
import com.nicholson.nicmessenger.domaine.modele.Message
import com.nicholson.nicmessenger.domaine.modele.Utilisateur
import kotlinx.coroutines.flow.Flow

interface IModèle {
    var estConnecté : Boolean
    var utilisateurConnecté : Utilisateur?
    var montrerNavUnit : (() -> Unit)?
    var cacherNavUnit : (() -> Unit)?
    var conversations : List<Conversation>
    var indiceConversationCourrante : Int
    var conversationCourrante : Conversation?
    var token : String?
    suspend fun seConnecter( email : String, motDePasse : String )
    suspend fun demandeMotDePasseOublié( email : String )
    suspend fun obtenirMesConversations() : List<Conversation>
    suspend fun obtenirConversationCourrante() : Conversation
    fun cacherNav()
    suspend fun obtenirMessagesPrécédent() : List<Message>
    suspend fun subscribeMessage( topic : String ) : Flow<Message>
    suspend fun envoyerMessage( destination : String, contenu : String )
}