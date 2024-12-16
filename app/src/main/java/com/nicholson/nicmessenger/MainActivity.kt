package com.nicholson.nicmessenger

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nicholson.nicmessenger.domaine.service.Authentification
import com.nicholson.nicmessenger.domaine.service.EnvoyerMessage
import com.nicholson.nicmessenger.domaine.service.ManipulerStatut
import com.nicholson.nicmessenger.domaine.service.ObtenirConversations
import com.nicholson.nicmessenger.domaine.service.ObtenirMessages
import com.nicholson.nicmessenger.domaine.service.ObtenirStatus
import com.nicholson.nicmessenger.donnees.http.SourceDeDonnéesConversationHttp
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

        val sourceDeDonnéesStomp = SourceDeDonnéesStomp( getString( R.string.url_wss ) )
        ObtenirMessages.sourceDeDonnéesStomp = sourceDeDonnéesStomp
        EnvoyerMessage.sourceDeDonnéesStomp = sourceDeDonnéesStomp
        ManipulerStatut.sourceDeDonnéesStomp = sourceDeDonnéesStomp
        ObtenirStatus.sourceDeDonnéesStomp = sourceDeDonnéesStomp

        Authentification.sourceDeDonnées =
            SourceDeDonnéesUtilisateurHttp( getString( R.string.url_api ) )

        val sourceDeDonnéesConversation =
            SourceDeDonnéesConversationHttp( getString( R.string.url_api ) )
        ObtenirConversations.sourceDeDonnées = sourceDeDonnéesConversation

        ObtenirMessages.sourceDeDonnéesConversation = sourceDeDonnéesConversation
    }

    override fun onDestroy() {
        super.onDestroy()
        StompClientInstance.obtenirInstance( getString( R.string.url_wss ) ).disconnect()
    }
}