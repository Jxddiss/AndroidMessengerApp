package com.nicholson.nicmessenger.presentation.profile

import com.nicholson.nicmessenger.presentation.otd.UtilisateurOTD

interface ContratVuePrésentateurProfile {
    interface IVueProfile {
        fun miseEnPlace()
        fun placerUtilisateur( utilisateurOTD : UtilisateurOTD )
        fun obtenirDescription() : String
        fun obtenirStatus() : String
        fun montrerChargement()
        fun masquerChargement()
        fun montrerErreurRéseau()
        fun montrerDialogSucces()
        fun redirigerÀLogin()
    }

    interface IPrésentateurProfile {
        fun traiterDémarrage()
        fun traiterObtenirUtilisateurConnecté()
        fun mettreÀJourProfile()
        fun traiterDeconnexion()
    }
}