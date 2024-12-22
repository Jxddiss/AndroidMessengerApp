package com.nicholson.nicmessenger.domaine.service

import com.nicholson.nicmessenger.domaine.modele.Demande
import com.nicholson.nicmessenger.donnees.ISourceDeDonnéesDemandes
import com.nicholson.nicmessenger.donnees.fictif.SourceDeDonnéesDemandesFictive

class ObtenirDemandes {
    companion object {
        var sourceDeDonnées : ISourceDeDonnéesDemandes = SourceDeDonnéesDemandesFictive()

        suspend fun obtenirsDemande( idUtilisateur: Long ) : List<Demande> {
            return sourceDeDonnées.obtenirDemandes( idUtilisateur )
        }
    }
}