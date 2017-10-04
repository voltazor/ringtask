package com.voltazor.ring.api.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.voltazor.ring.api.dto.ListingData
import com.voltazor.ring.api.dto.ListingResponse
import com.voltazor.ring.extractString
import com.voltazor.ring.model.Listing
import java.lang.reflect.Type

/**
 * Created by voltazor on 27/09/17.
 */
class ListingResponseDeserializer : JsonDeserializer<ListingResponse> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ListingResponse? {
        val data = json.asJsonObject["data"].asJsonObject
        val listings : List<Listing> = data["children"].asJsonArray.map {
            return@map context.deserialize<Listing>(it.asJsonObject["data"], Listing::class.java)
        }
        return ListingResponse(ListingData(listings, data["after"].extractString(), data["after"].extractString()))
    }

}