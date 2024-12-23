package com.nicholson.nicmessenger.domaine.service

import com.nicholson.nicmessenger.domaine.modele.Utilisateur
import com.nicholson.nicmessenger.donnees.ISourceDeDonéesUtilisateur
import com.nicholson.nicmessenger.donnees.fictif.SourceDeDonnéesUtilisateurFictive
import java.io.File

class MettreÀJourProfile {
    companion object {
        var sourceDeDonnées : ISourceDeDonéesUtilisateur = SourceDeDonnéesUtilisateurFictive()

        suspend fun mettreÀJourProfile( utilisateur : Utilisateur, avatarFile : File?, bannièreFile : File? ) : Utilisateur {
            return sourceDeDonnées.mettreÀJourProfile( utilisateur, avatarFile, bannièreFile )
        }
    }
}