package com.nicholson.nicmessenger.presentation.profile

import com.nicholson.nicmessenger.presentation.otd.UtilisateurOTD
import java.io.File

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
        fun ouvrirGalleriePhoto()
        fun obtenirNouvelAvatar() : File?
    }

    interface IPrésentateurProfile {
        fun traiterDémarrage()
        fun traiterObtenirUtilisateurConnecté()
        fun mettreÀJourProfile()
        fun traiterDeconnexion()
        fun traiterOuvrirGallerie()
    }
}