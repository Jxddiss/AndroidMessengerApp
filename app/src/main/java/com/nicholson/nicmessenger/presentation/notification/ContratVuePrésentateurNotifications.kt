package com.nicholson.nicmessenger.presentation.notification

import com.nicholson.nicmessenger.presentation.otd.NotificationOTD

interface ContratVuePrésentateurNotifications {

    interface IVueNotifications {
        fun miseEnPlace()
        fun placerNotifications( notificationsOTD : List<NotificationOTD> )
        fun ajouterNotification( notificationOTD : NotificationOTD )
        fun redirigerÀLogin()
        fun montrerChargement()
        fun masquerChargement()
    }

    interface IPrésentateurNotifications {
        fun traiterDémarrage()
        fun traiterObtenirNotification()
        fun traiterDeconnexion()
        fun attendreNotification()
    }
}