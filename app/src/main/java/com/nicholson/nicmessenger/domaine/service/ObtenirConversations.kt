package com.nicholson.nicmessenger.domaine.service

import com.nicholson.nicmessenger.domaine.modele.Conversation
import com.nicholson.nicmessenger.donnees.ISourceDeDonnéesConversation
import com.nicholson.nicmessenger.donnees.fictif.SourceDeDonnéesConversationFictive

class ObtenirConversations {
    companion object {
        var sourceDeDonnées : ISourceDeDonnéesConversation = SourceDeDonnéesConversationFictive()

        suspend fun obtenirMesConversations( id : Long ) : List<Conversation> {
            return sourceDeDonnées.obtenirConversation( id )
        }

        suspend fun obtenirConversationParId( id : Long ) : Conversation {
            return sourceDeDonnées.obtenirConversationParId( id )
        }
    }
}