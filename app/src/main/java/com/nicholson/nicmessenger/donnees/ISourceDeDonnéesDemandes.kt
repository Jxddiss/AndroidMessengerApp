package com.nicholson.nicmessenger.donnees

import com.nicholson.nicmessenger.domaine.modele.Demande

interface ISourceDeDonnéesDemandes {
    suspend fun obtenirDemandes( idUtilisateur: Long ) : List<Demande>
    suspend fun accepterDemande( idDemande : Long )
    suspend fun refuserDemande( idDemande : Long )
}