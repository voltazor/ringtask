package com.voltazor.ring.api.service

import com.voltazor.ring.api.ApiSettings.TOP
import com.voltazor.ring.api.dto.ListingResponse
import retrofit2.http.GET
import rx.Observable

/**
 * Created by voltazor on 27/09/17.
 */
interface NetworkService {

    @GET(TOP)
    fun requestTop(): Observable<ListingResponse>

}