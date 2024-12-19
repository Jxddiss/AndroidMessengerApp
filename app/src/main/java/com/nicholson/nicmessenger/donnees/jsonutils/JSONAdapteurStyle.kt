package com.nicholson.nicmessenger.donnees.jsonutils

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.nicholson.nicmessenger.domaine.modele.Style
import java.lang.reflect.Type

class JSONAdapteurStyle  : JsonDeserializer<Style> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext? )
    : Style? {
        val styleJson = json?.asString
        var style : Style? = null
        styleJson?.let {
            style = Gson().fromJson(styleJson, Style::class.java)
        }
        return style
    }
}