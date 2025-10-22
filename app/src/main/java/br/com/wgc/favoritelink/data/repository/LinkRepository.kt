package br.com.wgc.favoritelink.data.repository

import br.com.wgc.favoritelink.data.api.LinkApi
import br.com.wgc.favoritelink.data.local.dao.LinkDao
import br.com.wgc.favoritelink.data.local.entity.LinkEntity
import br.com.wgc.favoritelink.data.model.LinkResponse
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class LinkRepository(
    val linkApi: LinkApi,
    val linkDao: LinkDao
) {
    suspend fun createAlias(url: String): LinkResponse {
        return linkApi.createAlias(url)
    }

    suspend fun getLinkByAlias(id: String): LinkResponse {
        return linkApi.getLinkByAlias(id)
    }


    fun getAllLinks(): Flow<List<LinkEntity>> = linkDao.getAllLinks()

    suspend fun addLink(link: LinkEntity) = linkDao.insertLink(link)


    suspend fun updateLink(link: LinkEntity) = linkDao.updateLink(link)


    suspend fun deleteLink(id: UUID) = linkDao.deleteLinkById(id)

}