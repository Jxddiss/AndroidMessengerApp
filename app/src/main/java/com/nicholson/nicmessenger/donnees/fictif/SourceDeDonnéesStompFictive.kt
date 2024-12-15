package com.nicholson.nicmessenger.donnees.fictif

import com.google.gson.Gson
import com.nicholson.nicmessenger.domaine.modele.Message
import com.nicholson.nicmessenger.donnees.ISourceDeDonnéesStomp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SourceDeDonnéesStompFictive : ISourceDeDonnéesStomp {
    private val gson : Gson = Gson()

    override suspend fun disconnect() {
    }

    override suspend fun <T> subscribe( topic: String, type: Class<T> ): Flow<T> = flow {
        when(type) {
            Message::class.java -> {
                for ( messageJson in FaussesDonnées.listMessagesJson ) {
                    delay((500..2000).random().toLong())
                    val message = gson.fromJson(messageJson, type) as T
                    emit(message)
                }
            }
            else -> {}
        }
    }

    override suspend fun <T> sendMessage( destination: String, message: T ) {
    }
}