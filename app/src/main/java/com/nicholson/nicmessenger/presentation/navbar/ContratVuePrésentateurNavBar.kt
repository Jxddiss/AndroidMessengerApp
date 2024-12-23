package com.nicholson.nicmessenger.presentation.navbar

import com.nicholson.nicmessenger.presentation.otd.NotificationOTD

interface ContratVuePrésentateurNavBar {
    interface IVueNavBar{
        fun miseEnPlace()
        fun redirigerÀDemandes()
        fun redirigerÀProfile()
        fun redirigerÀAccueil()
        fun redirigerÀNotification()
        fun redirigerÀAbout()
        fun montrerNav()
        fun cacherNav()
        fun montrerNotification( notificationOTD : NotificationOTD )
        fun montrerIndicateurNotif()
        fun cacherNotification()
        fun mettreBtnHomeGris()
        fun mettreBtnDemandeGris()
        fun mettreBtnProfileGris()
        fun mettreBtnNotificationsGris()
        fun mettreBtnAboutGris()
        fun mettreBtnHomeBlanc()
        fun mettreBtnDemandeBlanc()
        fun mettreBtnProfileBlanc()
        fun mettreBtnNotificationsBlanc()
        fun mettreBtnAboutBlanc()
    }

    interface IPrésentateurNavBar {
        fun traiterDémarage()
        fun traiterRedirigerÀDemandes()
        fun traiterRedirigerÀProfile()
        fun traiterRedirigerÀAccueil()
        fun traiterRedirigerÀNotification()
        fun traiterRedirigerÀAbout()
    }
}