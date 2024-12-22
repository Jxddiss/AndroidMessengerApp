package com.nicholson.nicmessenger.presentation.about

interface ContratVuePrésentateurAbout {
    interface IVueAbout {
        fun miseEnPlace()
        fun redirigerÀLogin()
    }

    interface IPrésentateurAbout {
        fun traiterDémarrage()
        fun traiterDeconnexion()
    }
}