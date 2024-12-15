package com.nicholson.nicmessenger.donnees.jsonutils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.time.LocalDateTime


class GsonInstance private constructor() {
    companion object {
        @Volatile
        private var instance : Gson? = null

        fun obtenirInstance() =
            instance ?: synchronized(this) {
                instance ?: buildGson().also { instance = it }
            }

        private fun buildGson() : Gson {
            return GsonBuilder()
                .registerTypeAdapter( LocalDateTime::class.java, JSONAdapteurDate() )
                .create()
        }
    }
}