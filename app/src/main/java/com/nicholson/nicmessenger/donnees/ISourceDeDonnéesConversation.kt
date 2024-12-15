package com.nicholson.nicmessenger.donnees

import com.nicholson.nicmessenger.domaine.modele.Conversation
import com.nicholson.nicmessenger.domaine.modele.Message

interface ISourceDeDonnéesConversation {
    suspend fun obtenirConversation( idUtilisateur : Long ) : List<Conversation>
    suspend fun obtenirConversationParId( id : Long ) : Conversation
    suspend fun obtenirMessagesDeConversation( id : Long ) : List<Message>
}