package com.nicholson.nicmessenger.domaine.service

import android.util.Log
import com.nicholson.nicmessenger.donnees.ISourceDeDonnéesStomp
import com.nicholson.nicmessenger.donnees.fictif.SourceDeDonnéesStompFictive

class ManipulerStatut {
    companion object {
        var sourceDeDonnéesStomp: ISourceDeDonnéesStomp = SourceDeDonnéesStompFictive()

        suspend fun envoyerStatut( idUtilisateur : Long, status : CharSequence ) {
            Log.d("Sending status", "Sending....")
            sourceDeDonnéesStomp.sendMessage( "/app/user/status/$idUtilisateur", status )
        }
    }
}