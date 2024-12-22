package com.nicholson.nicmessenger.presentation.navbar

import android.util.Log
import com.nicholson.nicmessenger.presentation.IModèle
import com.nicholson.nicmessenger.presentation.Modèle
import com.nicholson.nicmessenger.presentation.navbar.ContratVuePrésentateurNavBar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class PréesentateurNavBar( val vue : IVueNavBar ,
                           private val iocontext : CoroutineContext = Dispatchers.IO )
    :  IPrésentateurNavBar {

    private lateinit var modèle : IModèle
    private var job : Job? = null

    override fun traiterDémarage() {
        modèle = Modèle.obtenirInstance()
        modèle.cacherNavUnit = { vue.cacherNav() }
        modèle.montrerNavUnit = { vue.montrerNav() }
        modèle.attendreNotificationNav = { attendreNotifications() }
        modèle.cacheIndicateurNotification = { vue.cacherNotification() }
        if ( !modèle.estConnecté ){
            modèle.cacherNav()
        }
        vue.miseEnPlace()
    }

    override fun traiterRedirigerÀDemandes() {
        vue.redirigerÀDemandes()
    }

    override fun traiterRedirigerÀProfile() {
        vue.redirigerÀProfile()
    }

    override fun traiterRedirigerÀAccueil() {
        vue.redirigerÀAccueil()
    }

    override fun traiterRedirigerÀNotification() {
        vue.redirigerÀNotification()
    }

    override fun traiterRedirigerÀAbout() {
        vue.redirigerÀAbout()
    }

    private fun attendreNotifications() {
        Log.d("Sub notif navbar", "subscribing")
        modèle.attendNotif = true
        job = CoroutineScope( iocontext ).launch {
            modèle.subscribeNotifications()
                .catch { Log.d( "Exception", "message : ${it.message}" ) }
                .collect {
                    montrerNotif()
                }
        }
    }

    private fun montrerNotif() {
        if ( !modèle.estSurVueNotifications ) {
            CoroutineScope( Dispatchers.Main ).launch {
                vue.montrerNotification()
            }
        }
    }
}