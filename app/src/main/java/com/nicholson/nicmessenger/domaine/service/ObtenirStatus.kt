package com.nicholson.nicmessenger.domaine.service

import com.nicholson.nicmessenger.donnees.ISourceDeDonnéesStomp
import com.nicholson.nicmessenger.donnees.fictif.SourceDeDonnéesStompFictive
import kotlinx.coroutines.flow.Flow

class ObtenirStatus {
    companion object {
        var sourceDeDonnéesStomp: ISourceDeDonnéesStomp = SourceDeDonnéesStompFictive()

        suspend fun subscribeStatus( topic : String ) : Flow<String> {
           return sourceDeDonnéesStomp.subscribe( topic, String::class.java )
        }
    }
}