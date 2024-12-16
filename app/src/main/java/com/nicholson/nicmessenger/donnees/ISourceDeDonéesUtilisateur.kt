package com.nicholson.nicmessenger.donnees

import com.nicholson.nicmessenger.domaine.modele.Utilisateur

interface ISourceDeDonéesUtilisateur {
    suspend fun seConnecter( email : String, motDePasse : String ) : Pair<String, Utilisateur>
    suspend fun demandeMotDePasseOublié( email : String )
}