package br.com.wgc.favoritelink.data.repository

import br.com.wgc.favoritelink.data.api.LinkApi
import br.com.wgc.favoritelink.data.local.dao.LinkDao
import br.com.wgc.favoritelink.data.local.entity.LinkEntity
import br.com.wgc.favoritelink.data.model.request.CreateAliasRequest
import br.com.wgc.favoritelink.data.model.response.LinkResponse
import br.com.wgc.favoritelink.data.model.response.Links
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.UUID

class LinkRepositoryTest {
    private var api: LinkApi = mockk()
    private var dao: LinkDao = mockk()

    private lateinit var repository: LinkRepository

    @Before
    fun setUp() {
        repository = LinkRepository(api, dao)
    }

    @Test
    fun `getAllLinks should call dao and return its flow`() = runTest {
        val fakeLinks = listOf(LinkEntity(name = "Test", url = "https://test.com"))
        coEvery { dao.getAllLinks() } returns flowOf(fakeLinks)

        val resultFlow = repository.getAllLinks()
        val resultList = resultFlow.first()

        assertEquals(fakeLinks, resultList)
        verify(exactly = 1) { dao.getAllLinks() }
    }

    @Test
    fun `createAndSaveLink should call api and then insert into dao`() = runTest {
        val name = "Google"
        val originalUrl = "https://google.com"
        val request = CreateAliasRequest(url = originalUrl)
        val fakeResponse = LinkResponse(
            alias = "alias",
            links = Links(self = originalUrl, short = "shortUrl")
        )

        coEvery { api.createAlias(request) } returns fakeResponse
        coEvery { dao.insertLink(any()) } just runs

        repository.createAndSaveLink(name, request)

        coVerify(exactly = 1) { api.createAlias(request) }
        coVerify(exactly = 1) {
            dao.insertLink(
                match { it.name == name && it.url == originalUrl }
            )
        }
    }

    @Test
    fun `updateLink should call dao's updateLink`() = runTest {
        val linkToUpdate = LinkEntity(name = "Update", url = "url")
        coEvery { dao.updateLink(linkToUpdate) } just runs

        repository.updateLink(linkToUpdate)

        coVerify(exactly = 1) { dao.updateLink(linkToUpdate) }
    }

    @Test
    fun `deleteLink should call dao's deleteLinkById`() = runTest {
        val idToDelete = UUID.randomUUID()
        coEvery { dao.deleteLinkById(idToDelete) } just runs

        repository.deleteLink(idToDelete)

        coVerify(exactly = 1) { dao.deleteLinkById(idToDelete) }
    }

}