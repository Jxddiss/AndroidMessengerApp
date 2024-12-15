package com.nicholson.nicmessenger.donnees.fictif

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.nicholson.nicmessenger.domaine.modele.Message
import com.nicholson.nicmessenger.domaine.modele.Notification
import com.nicholson.nicmessenger.donnees.ISourceDeDonnéesStomp
import com.nicholson.nicmessenger.donnees.exceptions.SourceDeDonnéesException
import com.nicholson.nicmessenger.donnees.jsonutils.GsonInstance
import com.nicholson.nicmessenger.donnees.jsonutils.JSONAdapteurDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime

class SourceDeDonnéesStompFictive : ISourceDeDonnéesStomp {

    override suspend fun disconnect() {
    }

    override suspend fun <T> subscribe( topic: String, type: Class<T> ): Flow<T> = flow {
        when(type) {
            Message::class.java -> {
                for ( messageJson in FaussesDonnées.listMessagesJson ) {
                    delay((500..2000).random().toLong())
                    try {
                        val message = GsonInstance.obtenirInstance().fromJson( messageJson, type ) as T
                        emit( message )
                    } catch ( ex : JsonSyntaxException ) {
                        throw SourceDeDonnéesException( "Exception Json : ${ex.message}" )
                    }
                }
            }
            Notification::class.java -> {

            }
            String::class.java -> {

            }
            else -> {}
        }
    }

    override suspend fun <T> sendMessage( destination: String, message: T ) {
        when {
            destination.contains( "chat" ) -> FaussesDonnées.listMessagesJson
                .add( GsonInstance.obtenirInstance().toJson( message ) )

            destination.contains( "notification" ) -> {}
            destination.contains( "statut" ) -> {}
        }
    }
}