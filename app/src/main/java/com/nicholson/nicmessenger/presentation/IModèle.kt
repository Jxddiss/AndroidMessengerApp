package com.nicholson.nicmessenger.presentation

import com.nicholson.nicmessenger.domaine.modele.Conversation
import com.nicholson.nicmessenger.domaine.modele.Utilisateur

interface IModèle {
    var estConnecté : Boolean
    var utilisateurConnecté : Utilisateur?
    var montrerNavUnit : (() -> Unit)?
    var cacherNavUnit : (() -> Unit)?
    var conversations : List<Conversation>
    var indiceConversationCourrante : Int
    suspend fun seConnecter( email : String, motDePasse : String )
    suspend fun demandeMotDePasseOublié( email : String )
    suspend fun obtenirMesConversations() : List<Conversation>
    suspend fun obtenirConversationCourrante() : Conversation
    fun cacherNav()
}