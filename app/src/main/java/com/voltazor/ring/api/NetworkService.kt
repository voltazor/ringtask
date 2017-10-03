package com.voltazor.ring.api

import com.voltazor.ring.api.ApiSettings.TOP
import com.voltazor.ring.api.dto.ListingResponse
import retrofit2.http.POST
import rx.Observable

/**
 * Created by voltazor on 27/09/17.
 */
interface NetworkService {

    @POST(TOP)
    fun requestTop(): Observable<ListingResponse>

}