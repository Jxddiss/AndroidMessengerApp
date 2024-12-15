package com.nicholson.nicmessenger.donnees

import com.nicholson.nicmessenger.domaine.modele.Message
import kotlinx.coroutines.flow.Flow

interface ISourceDeDonnéesStomp {
    suspend fun disconnect()
    suspend fun <T> subscribe(topic: String, type: Class<T>): Flow<T>
    suspend fun <T> sendMessage(destination: String, message: T)
}