package com.example.mikelesimonieventfindercs414hw4

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// defining methods for making http search requests
interface TicketMasterService {
    @GET("discovery/v2/events")
    fun searchEvents(
        @Query("apikey") apiKey: String,
        @Query("keyword") keyword: String?,
        @Query("city") city: String?,
        @Query("sort") sort: String = "date,asc"
    ): Call<EventData>
}
