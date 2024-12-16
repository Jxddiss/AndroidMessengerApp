package com.nicholson.nicmessenger.domaine.service

import com.nicholson.nicmessenger.domaine.modele.Message
import com.nicholson.nicmessenger.donnees.ISourceDeDonnéesConversation
import com.nicholson.nicmessenger.donnees.ISourceDeDonnéesStomp
import com.nicholson.nicmessenger.donnees.fictif.SourceDeDonnéesConversationFictive
import com.nicholson.nicmessenger.donnees.fictif.SourceDeDonnéesStompFictive
import kotlinx.coroutines.flow.Flow

class ObtenirMessages {
    companion object {
        var sourceDeDonnéesStomp : ISourceDeDonnéesStomp = SourceDeDonnéesStompFictive()
        var sourceDeDonnéesConversation : ISourceDeDonnéesConversation =
            SourceDeDonnéesConversationFictive()

        suspend fun obtenirMessagesPrécédents( idConversation : Long ) : List<Message> {
            return sourceDeDonnéesConversation.obtenirMessagesDeConversation( idConversation )
        }

        suspend fun messageListener( topic : String ) : Flow<Message> {
            return sourceDeDonnéesStomp.subscribe( topic, Message::class.java )
        }
    }
}