package com.nicholson.nicmessenger.donnees.fictif

import com.nicholson.nicmessenger.domaine.modele.Conversation
import com.nicholson.nicmessenger.donnees.ISourceDeDonnéesConversation

class SourceDeDonnéesConversationFictive : ISourceDeDonnéesConversation {
    override suspend fun obtenirConversation(idUtilisateur: Long) : List<Conversation> {
        return FaussesDonnées.listeConversations.filter { conversation ->
            conversation.utilisateurs.firstOrNull { it.id == idUtilisateur } != null
        }
    }

    override suspend fun obtenirConversationParId( id: Long ): Conversation {
        return FaussesDonnées.listeConversations.first {
            it.id == id
        }
    }

}