package com.nicholson.nicmessenger.donnees.websocket

import android.util.Log
import com.google.gson.JsonSyntaxException
import com.nicholson.nicmessenger.donnees.ISourceDeDonnéesStomp
import com.nicholson.nicmessenger.donnees.exceptions.SourceDeDonnéesException
import com.nicholson.nicmessenger.donnees.jsonutils.GsonInstance
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SourceDeDonnéesStomp( val urlApiWs : String )  : ISourceDeDonnéesStomp {

    override suspend fun disconnect() {
        val stompClient = StompClientInstance.obtenirInstance( urlApiWs )
        stompClient.disconnect()
    }

    override suspend fun <T> subscribe( topic: String, type: Class<T> ): Flow<T> = callbackFlow {
        val stompClient = StompClientInstance.obtenirInstance( urlApiWs )
        val subscription = stompClient.topic( topic ).subscribe(
            { messageFrame ->
                Log.d("Message received", "In source ${messageFrame.payload}")
                try {
                    val message =
                        GsonInstance.obtenirInstance()
                            .fromJson( messageFrame.payload, type ) as T

                    trySend( message ).onFailure { exception ->
                        throw
                            SourceDeDonnéesException( "Erreur dans la reception du message : " +
                                    "${exception?.message}" )
                    }
                } catch ( ex : JsonSyntaxException) {
                    throw SourceDeDonnéesException( "Exception Json : ${ex.message}" )
                }
            },
            { error ->
                throw SourceDeDonnéesException("Une erreur est survenue: ${error.message}")
            }
        )

        awaitClose {
            Log.d("Cancelling subscribe", "In source")
            subscription.dispose()
        }
    }

    override suspend fun <T> sendMessage( destination: String, message: T ) {
        val messageToSend = if (message is String) message else GsonInstance.obtenirInstance().toJson(message)
        val stompClient = StompClientInstance.obtenirInstance( urlApiWs )
        val sub = stompClient.send( destination, messageToSend )
            .subscribe(
                { Log.d("Sending message", "Sent data!") },
                { error -> Log.d("Sending message", "Encountered error while sending data!", error) }
            )
        sub.dispose()
    }
}