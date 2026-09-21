package com.unisms.app.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface SmsBowerApiService {

    @GET("stubs/handler_api.php?action=getBalance")
    suspend fun getBalance(
        @Query("api_key") apiKey: String
    ): String

    @GET("stubs/handler_api.php?action=getServicesList")
    suspend fun getServicesList(
        @Query("api_key") apiKey: String
    ): String

    @GET("stubs/handler_api.php?action=getPrices")
    suspend fun getPrices(
        @Query("api_key") apiKey: String,
        @Query("service") service: String? = null,
        @Query("country") country: String? = null
    ): String

    @GET("stubs/handler_api.php?action=getTopCountriesByService")
    suspend fun getTopCountriesByService(
        @Query("api_key") apiKey: String,
        @Query("service") service: String
    ): String

    @GET("stubs/handler_api.php?action=getNumberV2")
    suspend fun getNumberV2(
        @Query("api_key") apiKey: String,
        @Query("service") service: String,
        @Query("country") country: String,
        @Query("ref") ref: String? = null
    ): String

    @GET("stubs/handler_api.php?action=getStatus")
    suspend fun getStatus(
        @Query("api_key") apiKey: String,
        @Query("id") activationId: Long
    ): String

    @GET("stubs/handler_api.php?action=setStatus")
    suspend fun setStatus(
        @Query("api_key") apiKey: String,
        @Query("id") activationId: Long,
        @Query("status") status: Int
    ): String
}
