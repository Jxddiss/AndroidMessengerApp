package com.nicholson.nicmessenger.donnees.fictif

import com.nicholson.nicmessenger.domaine.modele.Notification
import com.nicholson.nicmessenger.donnees.ISourceDeDonnéesNotification

class SourceDeDonnéesNotificationsFictive : ISourceDeDonnéesNotification {
    override suspend fun obtenirNotificationNonLu( idUtilisateur: Long ): List<Notification> {
        return FaussesDonnées.notifications.filter { !it.lu }
    }
}