package com.nicholson.nicmessenger.presentation.login

interface ContratVuePrésentateurLogin {
    interface IVueLogin {
        fun miseEnPlace()
        fun obtenirEmail() : String
        fun obtenirMotDePasse() : String
        fun montrerMotDePasseInvalide()
        fun montrerEmailInvalide()
        fun redirigerÀAccueil()
        fun montrerErreurIdentifiants()
        fun montrerErreurRéseau()
        fun obtenirTokenEnregistré() : String?
        fun retirerTokenEnregistré()
        fun enregistrerTokenPréférences( token : String )
        fun cacherEditTextPasswordEtBtnLogin()
        fun changerListenerMotDePasseOubliéVersConfirmerEnvEmail()
        fun renitialiserListenerMotDePasseOublié()
        fun montrerMessageEmailMotDePasseOubliéEnvoyé()
    }

    interface IPrésentateurLogin {
        fun traiterDémarage()
        fun traiterSeConnecter()
        fun traiterMotDePasseOublié()
        fun traiterConfirmerMotDePasseOublié()
        fun traiterAnnulerMotDePasseOublié()
    }
}