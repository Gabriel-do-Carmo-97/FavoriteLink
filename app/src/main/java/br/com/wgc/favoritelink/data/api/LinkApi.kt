package br.com.wgc.favoritelink.data.api

import br.com.wgc.favoritelink.data.model.response.LinkResponse
import br.com.wgc.favoritelink.data.model.request.CreateAliasRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface LinkApi {
    @Headers("Content-Type: application/json")
    @POST("api/alias")
    suspend fun createAlias(
        @Body request: CreateAliasRequest
    ): LinkResponse

    @GET("api/alias/{id}")
    suspend fun getLinkByID(
        @Path("id") id: String
    ): LinkResponse
}