package com.nicholson.nicmessenger.donnees.http

import okhttp3.OkHttpClient


class ClientHttp private constructor() {
    companion object {
        @Volatile
        private var instance : OkHttpClient? = null

        fun obtenirInstance() =
            instance ?: synchronized(this) {
                instance ?: OkHttpClient.Builder().build().also { instance = it }
            }

        private fun ajouterIntercepteur( token : String, client : OkHttpClient? ) : OkHttpClient? {
            return client?.newBuilder()?.addInterceptor {
                    chain ->
                var requête = chain.request()
                requête = requête.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                chain.proceed( requête )
            }?.build()
        }

        fun ajouterToken( token : String ) {
            synchronized( this ){
                instance = ajouterIntercepteur( token, instance )
                    ?: ajouterIntercepteur( token, OkHttpClient.Builder().build() )
            }
        }

        fun retirerIntercepteurs() {
            obtenirInstance()
        }
    }
}