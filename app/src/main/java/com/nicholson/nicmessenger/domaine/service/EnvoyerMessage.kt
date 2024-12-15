package com.nicholson.nicmessenger.domaine.service

import com.nicholson.nicmessenger.domaine.modele.Message
import com.nicholson.nicmessenger.donnees.ISourceDeDonnéesStomp
import com.nicholson.nicmessenger.donnees.fictif.SourceDeDonnéesStompFictive
import java.time.LocalDateTime

class EnvoyerMessage {
    companion object {
        var sourceDeDonnéesStomp : ISourceDeDonnéesStomp = SourceDeDonnéesStompFictive()

        suspend fun envoyerMessage( destination : String, contenu : String, nomSender : String ) {
            val message = Message(
                id = 0L,
                nomSender = nomSender,
                contenu = contenu,
                type = "text",
                date = LocalDateTime.now(),
                style = null,
                winkName = null
            )

            sourceDeDonnéesStomp.sendMessage( destination, message )
        }
    }
}