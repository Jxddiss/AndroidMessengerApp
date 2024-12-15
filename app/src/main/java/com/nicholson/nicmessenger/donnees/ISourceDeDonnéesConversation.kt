package com.nicholson.nicmessenger.donnees

import com.nicholson.nicmessenger.domaine.modele.Conversation

interface ISourceDeDonnéesConversation {
    suspend fun obtenirConversation( idUtilisateur : Long ) : List<Conversation>
}