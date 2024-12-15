package com.nicholson.nicmessenger.donnees.decodeur

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DécodeurDate : JsonDeserializer<LocalDateTime> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime {
        val dateTimeString = json?.asString ?: throw JsonParseException("Date string is null or empty")
        return LocalDateTime.parse( dateTimeString, DateTimeFormatter.ISO_LOCAL_DATE_TIME )
    }
}