package com.nicholson.nicmessenger.domaine.service

import com.nicholson.nicmessenger.domaine.modele.Conversation
import com.nicholson.nicmessenger.domaine.modele.Message
import com.nicholson.nicmessenger.donnees.ISourceDeDonnéesStomp
import com.nicholson.nicmessenger.donnees.fictif.SourceDeDonnéesStompFictive
import java.time.LocalDateTime

class EnvoyerMessage {
    companion object {
        var sourceDeDonnéesStomp : ISourceDeDonnéesStomp = SourceDeDonnéesStompFictive()

        suspend fun envoyerMessage( destination : String,
                                    contenu : String,
                                    nomSender : String,
                                    type : String,
                                    idConv : Long ) {

            val message = Message(
                id = 0L,
                nomSender = nomSender,
                contenu = contenu,
                type = type,
                date = LocalDateTime.now(),
                style = null,
                winkName = null,
                conversation = Conversation( idConv, setOf() )
            )

            sourceDeDonnéesStomp.sendMessage( destination, message )
        }
    }
}