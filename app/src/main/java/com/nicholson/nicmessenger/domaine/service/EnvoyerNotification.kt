package com.nicholson.nicmessenger.domaine.service

import com.nicholson.nicmessenger.domaine.modele.Notification
import com.nicholson.nicmessenger.donnees.ISourceDeDonnéesStomp
import com.nicholson.nicmessenger.donnees.fictif.SourceDeDonnéesStompFictive

class EnvoyerNotification {
    companion object {
        var sourceDeDonnéesStomp : ISourceDeDonnéesStomp = SourceDeDonnéesStompFictive()

        suspend fun envoyerNotification( notification : Notification, receuveurId : Long ) {
            sourceDeDonnéesStomp
                .sendMessage( "/app/notification/$receuveurId", notification )
        }
    }
}