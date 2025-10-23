package br.com.wgc.favoritelink.data.repository

import br.com.wgc.favoritelink.data.api.LinkApi
import br.com.wgc.favoritelink.data.local.dao.LinkDao
import br.com.wgc.favoritelink.data.local.entity.LinkEntity
import br.com.wgc.favoritelink.data.model.request.CreateAliasRequest
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class LinkRepository @Inject constructor(
    private val linkApi: LinkApi,
    private val linkDao: LinkDao
) {

    fun getAllLinks(): Flow<List<LinkEntity>> = linkDao.getAllLinks()
    suspend fun createAndSaveLink(name: String, originalUrl: CreateAliasRequest) {
        val response = linkApi.createAlias(originalUrl)

        val newLinkEntity = LinkEntity(
            name = name,
            url = response.links.self
        )
        linkDao.insertLink(newLinkEntity)
    }
    suspend fun updateLink(link: LinkEntity) = linkDao.updateLink(link)
    suspend fun deleteLink(id: UUID) = linkDao.deleteLinkById(id)


}