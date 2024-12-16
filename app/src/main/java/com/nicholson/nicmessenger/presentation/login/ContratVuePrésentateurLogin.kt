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
        fun obtenirEmailEnregistré() : String?
        fun obtenirMotDePasseEnregistré() : String?
        fun retirerEmailEnregistré()
        fun retirerMotDePasseEnregistré()
        fun enregistrerEmailPréférences( email : String )
        fun enregistrerMotDePassePréférence( motDePasse : String )
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