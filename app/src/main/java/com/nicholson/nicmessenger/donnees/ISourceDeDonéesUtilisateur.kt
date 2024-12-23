package com.nicholson.nicmessenger.donnees

import com.nicholson.nicmessenger.domaine.modele.Utilisateur
import java.io.File

interface ISourceDeDonéesUtilisateur {
    suspend fun seConnecter( email : String, motDePasse : String ) : Pair<String, Utilisateur>
    suspend fun demandeMotDePasseOublié( email : String )
    suspend fun mettreÀJourProfile( utilisateur: Utilisateur, avatarFile : File? ) : Utilisateur
}