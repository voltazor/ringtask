package com.voltazor.ring.api.dto

import com.voltazor.ring.model.Post

/**
 * Created by voltazor on 03/10/17.
 */
data class ListingData(val children: List<Post>,
                       val after: String?,
                       val before: String?)