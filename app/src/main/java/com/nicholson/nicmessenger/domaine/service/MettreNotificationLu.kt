package com.nicholson.nicmessenger.domaine.service

import com.nicholson.nicmessenger.donnees.ISourceDeDonnéesStomp
import com.nicholson.nicmessenger.donnees.fictif.SourceDeDonnéesStompFictive

class MettreNotificationLu {
    companion object {
        var sourceDeDonnéesStomp: ISourceDeDonnéesStomp = SourceDeDonnéesStompFictive()

        suspend fun envoyerNotificationLu( idNotification : Long ) {
            ObtenirNotificationsNonLus.sourceDeDonnéesStomp
                .sendMessage( "/app/notification/set-lu/$idNotification", idNotification )
        }
    }
}