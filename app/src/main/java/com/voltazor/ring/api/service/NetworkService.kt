package com.voltazor.ring.api.service

import com.voltazor.ring.api.ApiSettings.AFTER
import com.voltazor.ring.api.ApiSettings.COUNT
import com.voltazor.ring.api.ApiSettings.LIMIT
import com.voltazor.ring.api.ApiSettings.TOP
import com.voltazor.ring.api.dto.ListingResponse
import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Created by voltazor on 27/09/17.
 */
interface NetworkService {

    @GET(TOP)
    fun requestTop(@Query(AFTER) after: String?, @Query(COUNT) count: Int, @Query(LIMIT) limit: Int = 10): Observable<ListingResponse>

}