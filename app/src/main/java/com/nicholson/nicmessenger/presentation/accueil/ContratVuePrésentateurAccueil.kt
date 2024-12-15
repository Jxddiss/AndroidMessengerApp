package com.nicholson.nicmessenger.presentation.accueil

interface ContratVuePrésentateurAccueil {
    interface IVueAccueil{
        fun miseEnPlace()
        fun redirigerÀLogin()
    }

    interface IPrésentateurAccueil {
        fun traiterDémarrage()
        fun traiterObtenirConversation()
    }
}