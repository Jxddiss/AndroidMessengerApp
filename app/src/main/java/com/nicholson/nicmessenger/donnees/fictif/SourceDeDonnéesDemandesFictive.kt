package com.nicholson.nicmessenger.donnees.fictif

import com.nicholson.nicmessenger.domaine.modele.Demande
import com.nicholson.nicmessenger.donnees.ISourceDeDonnéesDemandes

class SourceDeDonnéesDemandesFictive : ISourceDeDonnéesDemandes {
    override suspend fun obtenirDemandes( idUtilisateur: Long ): List<Demande> {
        return FaussesDonnées.demandes
    }

    override suspend fun accepterDemande( idDemande: Long ) {
        val demande = FaussesDonnées.demandes.first { it.id == idDemande }
        demande.accepter = true
        demande.statut = "Accepté"
    }

    override suspend fun refuserDemande( idDemande: Long ) {
        val demande = FaussesDonnées.demandes.first { it.id == idDemande }
        demande.accepter = false
        demande.statut = "Refusé"
    }
}