package com.nicholson.nicmessenger.domaine.service

import com.nicholson.nicmessenger.donnees.ISourceDeDonnéesDemandes
import com.nicholson.nicmessenger.donnees.fictif.SourceDeDonnéesDemandesFictive

class ManipulerDemandes {
    companion object {
        var sourceDeDonnées: ISourceDeDonnéesDemandes = SourceDeDonnéesDemandesFictive()

        suspend fun accepterDemande( idDemande : Long ) {
            sourceDeDonnées.accepterDemande( idDemande )
        }

        suspend fun refuserDemande( idDemande : Long ) {
            sourceDeDonnées.refuserDemande( idDemande )
        }
    }
}