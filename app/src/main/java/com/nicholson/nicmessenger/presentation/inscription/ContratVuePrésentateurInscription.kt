package com.nicholson.nicmessenger.presentation.inscription

interface ContratVuePrésentateurInscription {
    interface IVueInscription {
        fun miseEnPlace()
        fun obtenirEmail() : String
        fun obtenirMotDePasse() : String
        fun obtenirNomComplet() : String
        fun obtenirConfirmerMotDePasse() : String
        fun montrerMotDePasseInvalide()
        fun montrerMotDePasseNonIdentique()
        fun montrerEmailInvalide()
        fun montrerEmailExistant()
        fun montrerNomInvalide()
        fun montrerErreurRéseau()
        fun montrerDialogSuccès()
        fun redirigerÀLogin()
        fun redirigerÀAccueil()
        fun desactiverBoutton()
        fun reactiverBoutton()
    }

    interface IPrésentateurInscription {
        fun traiterDémarage()
        fun traiterInscription()
        fun traiterRedirigerLogin()
    }
}