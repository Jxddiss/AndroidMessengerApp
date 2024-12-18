package com.nicholson.nicmessenger.donnees.websocket

import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.StompHeader

class StompClientInstance {
    companion object {
        @Volatile
        private var instance : StompClient? = null
        var headers : List<StompHeader>? = null
        var currentUrlApiWs : String = ""

        fun obtenirInstance() =
            instance ?: synchronized(this) {
                instance ?: builStompClient( ).also { instance = it }
            }

        private fun builStompClient() : StompClient {
            val stompClient = Stomp.over( Stomp.ConnectionProvider.OKHTTP, currentUrlApiWs )
            return stompClient
        }

        fun addToken( token : String ) {
            headers = listOf( StompHeader("Authorization", "Bearer $token") )
            instance?.disconnect()
            instance = null
            obtenirInstance().connect( headers )
        }
    }
}