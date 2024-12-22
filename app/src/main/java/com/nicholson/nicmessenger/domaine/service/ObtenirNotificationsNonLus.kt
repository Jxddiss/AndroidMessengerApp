package com.nicholson.nicmessenger.domaine.service

import com.nicholson.nicmessenger.domaine.modele.Notification
import com.nicholson.nicmessenger.donnees.ISourceDeDonnéesNotification
import com.nicholson.nicmessenger.donnees.ISourceDeDonnéesStomp
import com.nicholson.nicmessenger.donnees.fictif.SourceDeDonnéesNotificationsFictive
import com.nicholson.nicmessenger.donnees.fictif.SourceDeDonnéesStompFictive
import kotlinx.coroutines.flow.Flow

class ObtenirNotificationsNonLus {
    companion object {
        var sourceDeDonnées : ISourceDeDonnéesNotification = SourceDeDonnéesNotificationsFictive()
        var sourceDeDonnéesStomp: ISourceDeDonnéesStomp = SourceDeDonnéesStompFictive()

        suspend fun obtenirNotificationNonLus( idUtilisateur : Long ) : List<Notification> {
            return sourceDeDonnées.obtenirNotificationNonLu( idUtilisateur )
        }

        suspend fun subscribeNotifications( idUtilisateur: Long ) : Flow<Notification> {
            return sourceDeDonnéesStomp
                .subscribe( "/topic/notification/$idUtilisateur", Notification::class.java )
        }
    }
}