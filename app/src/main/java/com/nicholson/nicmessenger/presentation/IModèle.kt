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
    var seDéconnecter : (() -> Unit)?
    var conversations : List<Conversation>
    var indiceConversationCourrante : Int
    var conversationCourrante : Conversation?
    var token : String?
    var currentStatus : String?
    fun cacherNav()
    fun seDéconnecter()
    suspend fun seConnecter( email : String, motDePasse : String )
    suspend fun envoyerStatut()
    suspend fun demandeMotDePasseOublié( email : String )
    suspend fun obtenirMesConversations() : List<Conversation>
    suspend fun obtenirConversationCourrante() : Conversation
    suspend fun obtenirMessagesPrécédent() : List<Message>
    suspend fun subscribeMessage( topic : String ) : Flow<Message>
    suspend fun envoyerMessage( destination : String, contenu : String, type : String  )
    suspend fun subscribeStatus( topic : String ) : Flow<String>
}