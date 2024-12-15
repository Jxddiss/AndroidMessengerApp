package com.nicholson.nicmessenger.presentation.navbar

import com.nicholson.nicmessenger.presentation.IModèle
import com.nicholson.nicmessenger.presentation.Modèle
import com.nicholson.nicmessenger.presentation.navbar.ContratVuePrésentateurNavBar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PréesentateurNavBar( val vue : IVueNavBar ) :  IPrésentateurNavBar {
    private lateinit var modèle : IModèle

    override fun traiterDémarage() {
        modèle = Modèle.obtenirInstance()
        modèle.cacherNavUnit = { vue.cacherNav() }
        modèle.montrerNavUnit = { vue.montrerNav() }
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

    override fun traiterRedirigerÀParametre() {
        vue.redirigerÀParametre()
    }
}