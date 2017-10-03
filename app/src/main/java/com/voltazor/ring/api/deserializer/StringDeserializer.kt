package com.voltazor.ring.api.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * Created by voltazor on 27/09/17.
 */
class StringDeserializer: JsonDeserializer<String> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): String? {
        if (json.isJsonObject || json.isJsonArray) {
            return json.toString()
        } else if (json.isJsonNull) {
            return null
        }
        return json.asString
    }

}