package com.nicholson.nicmessenger.presentation.navbar

interface ContratVuePrésentateurNavBar {
    interface IVueNavBar{
        fun miseEnPlace()
        fun redirigerÀDemandes()
        fun redirigerÀProfile()
        fun redirigerÀAccueil()
        fun redirigerÀNotification()
    }

    interface IPrésentateurNavBar {
        fun traiterDémarage()
        fun traiterRedirigerÀDemandes()
        fun traiterRedirigerÀProfile()
        fun traiterRedirigerÀAccueil()
        fun traiterRedirigerÀNotification()
    }
}