package com.voltazor.ring.model

/**
 * Created by voltazor on 03/10/17.
 */
class Preview(private val images: List<Image?>) {

    val url: String?
        get() = images.firstOrNull()?.source?.url

    data class Image(val source: Source?)

    data class Source(val url: String, val width: Int, val height: Int)

}