package com.nicholson.nicmessenger.presentation.navbar

import android.util.Log
import com.nicholson.nicmessenger.domaine.modele.Notification
import com.nicholson.nicmessenger.donnees.exceptions.SourceDeDonnéesException
import com.nicholson.nicmessenger.presentation.IModèle
import com.nicholson.nicmessenger.presentation.Modèle
import com.nicholson.nicmessenger.presentation.navbar.ContratVuePrésentateurNavBar.*
import com.nicholson.nicmessenger.presentation.otd.NotificationOTD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PrésentateurNavBar(val vue : IVueNavBar,
                         private val iocontext : CoroutineContext = Dispatchers.IO )
    :  IPrésentateurNavBar {

    private lateinit var modèle : IModèle
    private var job : Job? = null

    override fun traiterDémarage() {
        modèle = Modèle.obtenirInstance()
        modèle.cacherNavUnit = { vue.cacherNav() }
        modèle.montrerNavUnit = { vue.montrerNav() }
        modèle.attendreNotificationNav = { attendreNotifications() }
        modèle.cacheIndicateurNotification = {
            vue.cacherNotification()
            modèle.indicateurNotifVisible = false
        }
        modèle.montrerIndicateurNotif = {
            vue.montrerIndicateurNotif()
            modèle.indicateurNotifVisible = true
        }
        if ( !modèle.estConnecté ){
            modèle.cacherNav()
        }
        vue.miseEnPlace()
        vue.mettreBtnHomeSelectionné()
    }

    override fun traiterRedirigerÀDemandes() {
        vue.mettreBtnDemandeSelectionné()
        vue.mettreBtnHomeDéselectionné()
        vue.mettreBtnAboutDéselectionné()
        vue.mettreBtnNotificationsDéselectionné()
        vue.mettreBtnProfileDéselectionné()
        vue.redirigerÀDemandes()
    }

    override fun traiterRedirigerÀProfile() {
        vue.mettreBtnDemandeDéselectionné()
        vue.mettreBtnHomeDéselectionné()
        vue.mettreBtnAboutDéselectionné()
        vue.mettreBtnNotificationsDéselectionné()
        vue.mettreBtnProfileSelectionné()
        vue.redirigerÀProfile()
    }

    override fun traiterRedirigerÀAccueil() {
        vue.mettreBtnDemandeDéselectionné()
        vue.mettreBtnHomeSelectionné()
        vue.mettreBtnAboutDéselectionné()
        vue.mettreBtnNotificationsDéselectionné()
        vue.mettreBtnProfileDéselectionné()
        vue.redirigerÀAccueil()
    }

    override fun traiterRedirigerÀNotification() {
        vue.mettreBtnDemandeDéselectionné()
        vue.mettreBtnHomeDéselectionné()
        vue.mettreBtnAboutDéselectionné()
        vue.mettreBtnNotificationsSelectionné()
        vue.mettreBtnProfileDéselectionné()
        vue.redirigerÀNotification()
    }

    override fun traiterRedirigerÀAbout() {
        vue.mettreBtnDemandeDéselectionné()
        vue.mettreBtnHomeDéselectionné()
        vue.mettreBtnAboutSelectionné()
        vue.mettreBtnNotificationsDéselectionné()
        vue.mettreBtnProfileDéselectionné()
        vue.redirigerÀAbout()
    }

    private fun attendreNotifications() {
        Log.d("Sub notif navbar", "subscribing")
        modèle.attendNotif = true
        job = CoroutineScope( iocontext ).launch {
            modèle.subscribeNotifications()
                .catch { Log.d( "Exception", "message : ${it.message}" ) }
                .collect {
                    if ( it.titre != modèle.nomConversationCourrante ) {
                        montrerNotif( convertirNotificationÀNotificationOTD( it ) )
                    } else {
                        try {
                            modèle.mettreNotificationLu( it.id )
                        } catch ( ex : SourceDeDonnéesException) {
                            Log.d( "Exception", "message : ${ex.message}" )
                        }
                    }
                }
        }
    }

    private fun montrerNotif( notificationOTD : NotificationOTD) {
        if ( !modèle.estSurVueNotifications ) {
            modèle.indicateurNotifVisible = true
            CoroutineScope( Dispatchers.Main ).launch {
                vue.montrerNotification( notificationOTD )
            }
        }
    }

    private fun convertirNotificationÀNotificationOTD( notification : Notification)
            : NotificationOTD =
        NotificationOTD(
            id = notification.id.toInt(),
            titre = notification.titre,
            message = notification.message,
            image = notification.image
        )
}