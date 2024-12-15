package com.nicholson.nicmessenger.presentation.navbar

interface ContratVuePrésentateurNavBar {
    interface IVueNavBar{
        fun miseEnPlace()
        fun redirigerÀDemandes()
        fun redirigerÀProfile()
        fun redirigerÀAccueil()
        fun redirigerÀNotification()
        fun redirigerÀParametre()
        fun montrerNav()
        fun cacherNav()
    }

    interface IPrésentateurNavBar {
        fun traiterDémarage()
        fun traiterRedirigerÀDemandes()
        fun traiterRedirigerÀProfile()
        fun traiterRedirigerÀAccueil()
        fun traiterRedirigerÀNotification()
        fun traiterRedirigerÀParametre()
    }
}