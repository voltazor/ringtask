package com.voltazor.ring.model

import com.google.gson.annotations.SerializedName

/**
 * Created by voltazor on 03/10/17.
 */
data class Listing(val id: String,
                   val title: String,
                   val thumbnail: String,
                   val author: String,
                   val url: String?,
                   @SerializedName("created_utc") val created: Long,
                   val preview: Preview,
                   @SerializedName("num_comments") val comments: Int
)