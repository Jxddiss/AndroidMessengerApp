package com.nicholson.nicmessenger

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nicholson.nicmessenger.domaine.service.Authentification
import com.nicholson.nicmessenger.domaine.service.EnvoyerMessage
import com.nicholson.nicmessenger.domaine.service.EnvoyerNotification
import com.nicholson.nicmessenger.domaine.service.ManipulerDemandes
import com.nicholson.nicmessenger.domaine.service.ManipulerStatut
import com.nicholson.nicmessenger.domaine.service.MettreNotificationLu
import com.nicholson.nicmessenger.domaine.service.MettreÀJourProfile
import com.nicholson.nicmessenger.domaine.service.ObtenirConversations
import com.nicholson.nicmessenger.domaine.service.ObtenirDemandes
import com.nicholson.nicmessenger.domaine.service.ObtenirMessages
import com.nicholson.nicmessenger.domaine.service.ObtenirNotificationsNonLus
import com.nicholson.nicmessenger.domaine.service.ObtenirStatus
import com.nicholson.nicmessenger.donnees.http.SourceDeDonnéesConversationHttp
import com.nicholson.nicmessenger.donnees.http.SourceDeDonnéesDemandesHttp
import com.nicholson.nicmessenger.donnees.http.SourceDeDonnéesNotificationHttp
import com.nicholson.nicmessenger.donnees.http.SourceDeDonnéesUtilisateurHttp
import com.nicholson.nicmessenger.donnees.websocket.SourceDeDonnéesStomp
import com.nicholson.nicmessenger.donnees.websocket.StompClientInstance

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        StompClientInstance.currentUrlApiWs = getString( R.string.url_wss )

        val sourceDeDonnéesStomp = SourceDeDonnéesStomp()
        ObtenirMessages.sourceDeDonnéesStomp = sourceDeDonnéesStomp
        EnvoyerMessage.sourceDeDonnéesStomp = sourceDeDonnéesStomp
        ManipulerStatut.sourceDeDonnéesStomp = sourceDeDonnéesStomp
        ObtenirStatus.sourceDeDonnéesStomp = sourceDeDonnéesStomp
        EnvoyerNotification.sourceDeDonnéesStomp = sourceDeDonnéesStomp

        val sourceDeDonnéesUtilisateur = SourceDeDonnéesUtilisateurHttp( getString( R.string.url_api ) )

        Authentification.sourceDeDonnées = sourceDeDonnéesUtilisateur
        MettreÀJourProfile.sourceDeDonnées = sourceDeDonnéesUtilisateur

        val sourceDeDonnéesConversation =
            SourceDeDonnéesConversationHttp( getString( R.string.url_api ) )
        ObtenirConversations.sourceDeDonnées = sourceDeDonnéesConversation

        ObtenirMessages.sourceDeDonnéesConversation = sourceDeDonnéesConversation
        ObtenirNotificationsNonLus.sourceDeDonnées =
            SourceDeDonnéesNotificationHttp( getString( R.string.url_api ) )
        ObtenirNotificationsNonLus.sourceDeDonnéesStomp = sourceDeDonnéesStomp
        MettreNotificationLu.sourceDeDonnéesStomp = sourceDeDonnéesStomp

        val sourceDeDonnéesDemandes = SourceDeDonnéesDemandesHttp( getString( R.string.url_api ) )
        ObtenirDemandes.sourceDeDonnées = sourceDeDonnéesDemandes
        ManipulerDemandes.sourceDeDonnées = sourceDeDonnéesDemandes
        créerCannalNotifications()
    }

    private fun créerCannalNotifications() {
        val name = "Mes notifications"
        val descriptionText = "Cannal pour notifications de NicMessenger"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val soundURI = Uri.parse("android.resource://${packageName}/raw/type")
        val channel = NotificationChannel( "NOTIFICATION_NICMESSENGER", name, importance )
            .apply {
                description = descriptionText
                setSound( soundURI,
                    AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build() )
            }

        val notificationManager = getSystemService( Context.NOTIFICATION_SERVICE ) as NotificationManager
        notificationManager.createNotificationChannel( channel )
    }
}