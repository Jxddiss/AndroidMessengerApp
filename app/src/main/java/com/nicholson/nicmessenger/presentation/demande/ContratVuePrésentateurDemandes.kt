package com.nicholson.nicmessenger.presentation.demande

import com.nicholson.nicmessenger.presentation.otd.DemandeOTD
import com.nicholson.nicmessenger.presentation.otd.NotificationOTD

interface ContratVuePrésentateurDemandes {
    interface IVueDemandes {
        fun miseEnPlace()
        fun placerDemandes( demandesOTD : List<DemandeOTD>, nomUtilisateurConnecté : String )
        fun redirigerÀLogin()
        fun montrerChargement()
        fun masquerChargement()
    }

    interface IPrésentateurDemandes {
        fun traiterDémarrage()
        fun traiterObtenirDemandes()
        fun traiterDeconnexion()
        fun accepterDemande( position : Int )
        fun refuserDemande( position: Int )
    }
}