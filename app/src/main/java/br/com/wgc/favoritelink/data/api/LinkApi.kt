package br.com.wgc.favoritelink.data.api

import br.com.wgc.favoritelink.data.model.request.CreateAliasRequest
import br.com.wgc.favoritelink.data.model.response.LinkResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LinkApi {
    @Headers("Content-Type: application/json")
    @POST("api/alias")
    suspend fun createAlias(
        @Body request: CreateAliasRequest
    ): LinkResponse
}