package br.com.wgc.favoritelink.data.api

import br.com.wgc.favoritelink.data.model.LinkResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface LinkApi {
    @POST("api/alias")
    suspend fun createAlias(@Body url: String): LinkResponse

    @GET("api/alias/{id}")
    suspend fun getLinkByAlias(
        @Path("id") id: String
    ): LinkResponse
}