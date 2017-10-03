package com.voltazor.ring.api.dto

import com.voltazor.ring.model.Listing

/**
 * Created by voltazor on 03/10/17.
 */
data class ListingData(val children: List<Listing>,
                       val after: String?,
                       val before: String?)