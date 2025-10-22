package br.com.wgc.favoritelink.data.repository

import br.com.wgc.favoritelink.data.api.LinkApi
import br.com.wgc.favoritelink.data.model.LinkResponse

class LinkRepository(
    val linkApi: LinkApi
) {
    suspend fun createAlias(url: String): LinkResponse {
        return linkApi.createAlias(url)
    }

    suspend fun getLinkByAlias(id: String): LinkResponse {
        return linkApi.getLinkByAlias(id)
    }
}