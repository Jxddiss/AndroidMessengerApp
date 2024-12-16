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

        fun obtenirInstance( urlApiWs : String ) =
            instance ?: synchronized(this) {
                instance ?: builStompClient( urlApiWs ).also { instance = it }
            }

        private fun builStompClient( urlApiWs : String ) : StompClient {
            val stompClient = Stomp.over( Stomp.ConnectionProvider.OKHTTP, urlApiWs )
            currentUrlApiWs = urlApiWs
            return stompClient
        }

        fun addToken( token : String ) {
            headers = listOf( StompHeader("Authorization", "Bearer $token") )
            instance?.disconnect()
            obtenirInstance( currentUrlApiWs ).connect( headers )
        }
    }
}