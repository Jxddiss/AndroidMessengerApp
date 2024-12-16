package com.nicholson.nicmessenger.donnees.websocket

import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompHeader
import ua.naiksoftware.stomp.client.StompClient

class StompClientInstance {
    companion object {
        @Volatile
        private var instance : StompClient? = null
        private var headers : List<StompHeader>? = null

        fun obtenirInstance( urlApiWs : String ) =
            instance ?: synchronized(this) {
                instance ?: builStompClient( urlApiWs ).also { instance = it }
            }

        private fun builStompClient( urlApiWs : String ) : StompClient {
            val stompClient = Stomp.over( Stomp.ConnectionProvider.OKHTTP, urlApiWs )
            return stompClient
        }

        fun addToken( token : String ) {
            headers = listOf( StompHeader("Authorization", "Bearer $token") )
        }

        fun connect(){
            headers?.let {
                instance?.connect( headers )
            }
        }
    }
}