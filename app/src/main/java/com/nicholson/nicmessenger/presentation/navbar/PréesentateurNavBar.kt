package com.nicholson.nicmessenger.presentation.navbar

import com.nicholson.nicmessenger.presentation.navbar.ContratVuePrésentateurNavBar.*
class PréesentateurNavBar( val vue : IVueNavBar ) :  IPrésentateurNavBar {
    override fun traiterDémarage() {
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
}