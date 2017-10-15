package com.voltazor.ring.api.deserializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.voltazor.ring.api.dto.ListingData
import com.voltazor.ring.api.dto.ListingResponse
import com.voltazor.ring.extractString
import com.voltazor.ring.model.Post
import java.lang.reflect.Type

/**
 * Created by voltazor on 27/09/17.
 */
class ListingResponseDeserializer : JsonDeserializer<ListingResponse> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ListingResponse? {
        val data = json.asJsonObject["data"].asJsonObject
        val posts: List<Post> = data["children"].asJsonArray.map {
            return@map context.deserialize<Post>(it.asJsonObject["data"], Post::class.java)
        }
        return ListingResponse(ListingData(posts, data["after"].extractString(), data["after"].extractString()))
    }

}