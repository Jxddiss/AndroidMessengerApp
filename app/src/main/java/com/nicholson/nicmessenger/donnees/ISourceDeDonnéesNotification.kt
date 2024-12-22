package com.nicholson.nicmessenger.donnees

import com.nicholson.nicmessenger.domaine.modele.Notification

interface ISourceDeDonnéesNotification {
    suspend fun obtenirNotificationNonLu( idUtilisateur : Long ) : List<Notification>
}