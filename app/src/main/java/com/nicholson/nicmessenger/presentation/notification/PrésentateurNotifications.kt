package com.nicholson.nicmessenger.presentation.notification

import android.util.Log
import com.nicholson.nicmessenger.domaine.modele.Notification
import com.nicholson.nicmessenger.donnees.exceptions.AuthentificationException
import com.nicholson.nicmessenger.donnees.exceptions.SourceDeDonnéesException
import com.nicholson.nicmessenger.presentation.IModèle
import com.nicholson.nicmessenger.presentation.Modèle
import com.nicholson.nicmessenger.presentation.notification.ContratVuePrésentateurNotifications.*
import com.nicholson.nicmessenger.presentation.otd.NotificationOTD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PrésentateurNotifications(private val vue : IVueNotifications,
                                private val iocontext : CoroutineContext = Dispatchers.IO )
    : IPrésentateurNotifications {

    private var job : Job? = null
    private lateinit var modèle : IModèle

    override fun traiterDémarrage() {
        modèle = Modèle.obtenirInstance()
        modèle.estSurVueNotifications = true
        vue.miseEnPlace()
        vue.montrerChargement()
    }

    override fun traiterObtenirNotification() {
        if( !modèle.estConnecté ) {
            modèle.cacherNav()
            vue.redirigerÀLogin()
        } else {
            job = CoroutineScope( iocontext ).launch {
                var notifications : List<Notification> = modèle.listeNotifications

                if ( notifications.isEmpty() ) {
                    notifications = try {
                        modèle.obtenirNotificationsNonLu()
                    } catch ( ex : SourceDeDonnéesException ) {
                        Log.d( "Exception", "message : ${ex.message}" )
                        emptyList()
                    } catch ( ex : AuthentificationException ) {
                        CoroutineScope( Dispatchers.Main ).launch {
                            vue.redirigerÀLogin()
                        }
                        emptyList()
                    }
                }

                val notifificationsOTD = notifications.map {
                    convertirNotificationÀNotificationOTD(it)
                }

                CoroutineScope( Dispatchers.Main ).launch {
                    vue.placerNotifications( notifificationsOTD )
                    vue.masquerChargement()
                    modèle.cacheIndicateurNotification?.let { it() }
                }

                try {
                    notifications.forEach {
                        modèle.mettreNotificationLu( it.id )
                    }
                } catch ( ex : SourceDeDonnéesException ) {
                    Log.d( "Exception", "message : ${ex.message}" )
                }
            }
        }
    }

    override fun traiterDeconnexion() {
        modèle.seDéconnecter()
        vue.redirigerÀLogin()
    }

    override fun attendreNotification() {
        job = CoroutineScope( iocontext ).launch {
            modèle.subscribeNotifications()
                .catch { Log.d( "Exception", "message : ${it.message}" ) }
                .collect {
                    CoroutineScope( Dispatchers.Main ).launch {
                        vue.ajouterNotification( convertirNotificationÀNotificationOTD(it) )
                    }
                    if ( modèle.estSurVueNotifications ) {
                        try {
                            modèle.mettreNotificationLu( it.id )
                        } catch ( ex : SourceDeDonnéesException ) {
                            Log.d( "Exception", "message : ${ex.message}" )
                        }
                    }
                }
        }
    }

    private fun convertirNotificationÀNotificationOTD( notification : Notification )
        : NotificationOTD =
            NotificationOTD(
                titre = notification.titre,
                message = notification.message,
                image = notification.image
            )

}