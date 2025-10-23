package br.com.wgc.favoritelink.ui.home_screen

import app.cash.turbine.test
import br.com.wgc.favoritelink.data.local.entity.LinkEntity
import br.com.wgc.favoritelink.data.model.request.CreateAliasRequest
import br.com.wgc.favoritelink.data.repository.LinkRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.util.UUID


@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private lateinit var testDispatcher: TestDispatcher
    private var linkRepository: LinkRepository = mockk(relaxed = true)
    val fakeLink1 = LinkEntity(id = UUID.randomUUID(), name = "Google", url = "https://google.com")
    val fakeLink2 = LinkEntity(id = UUID.randomUUID(), name = "WGC", url = "https://wgc.com.br")

    @Before
    fun setUp() {
        testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)
        clearAllMocks()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState should emit loading state initially`() = runTest {
        val flowController = MutableStateFlow<List<LinkEntity>?>(null)
        coEvery { linkRepository.getAllLinks() } returns flowController.filterNotNull()

        val viewModel = HomeViewModel(linkRepository)

        viewModel.uiState.test {
            val initialState = awaitItem()
            assertTrue(initialState.isLoading)
            assertTrue(initialState.links.isEmpty())

            flowController.value = listOf(fakeLink1, fakeLink2)

            val loadedState = awaitItem()
            assertFalse(loadedState.isLoading)
            assertEquals(2, loadedState.links.size)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSearchTextChanged should update state with filtered links`() = runTest {
        coEvery { linkRepository.getAllLinks() } returns flowOf(
            listOf(fakeLink1, fakeLink2)
        )
        val viewModel = HomeViewModel(linkRepository)


        viewModel.uiState.test {
            awaitItem()
            viewModel.onSearchTextChanged("google")
            val state = awaitItem()
            assertEquals(1, state.links.size)
            assertEquals("Google", state.links[0].name)
            assertEquals("google", state.searchText)
        }
    }

    @Test
    fun `deleteLink should call repository and not show error on success`() = runTest {
        val linksFlow = MutableStateFlow(listOf(fakeLink1, fakeLink2))

        coEvery { linkRepository.getAllLinks() } returns linksFlow
        coEvery { linkRepository.deleteLink(fakeLink1.id) } just runs

        val viewModel = HomeViewModel(linkRepository)
        linksFlow.value = listOf(fakeLink2)

        viewModel.deleteLink(fakeLink1.id)

        coVerify(exactly = 1) { linkRepository.deleteLink(fakeLink1.id) }

        viewModel.uiState.test {
            val finalState = awaitItem()
            assertEquals(1, finalState.links.size)
            assertEquals("WGC", finalState.links[0].name)
            assertNull(finalState.errorMessage)
        }
    }

    @Test
    fun `deleteLink should show error message on failure`() = runTest {
        coEvery { linkRepository.getAllLinks() } returns flowOf(listOf(fakeLink1, fakeLink2))
        val errorMessage = "DB is Error"
        coEvery { linkRepository.deleteLink(fakeLink1.id) } throws IOException(errorMessage)
        val viewModel = HomeViewModel(linkRepository)

        viewModel.deleteLink(fakeLink1.id)

        coVerify(exactly = 1) { linkRepository.deleteLink(fakeLink1.id) }
        viewModel.uiState.test {
            val errorState = awaitItem()
            assertEquals("A lista não deve mudar em caso de falha", 2, errorState.links.size)
            assertTrue(
                "A mensagem de erro deve estar presente",
                errorState.errorMessage?.contains(errorMessage) ?: false
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onAddLinkClick should call repository and not show error on success`() = runTest {
        coEvery { linkRepository.getAllLinks() } returns flowOf(emptyList())

        coEvery { linkRepository.createAndSaveLink(any(), any()) } just runs

        val viewModel = HomeViewModel(linkRepository)
        val name = "Test"
        val url = "https://test.com"

        viewModel.onAddLinkClick(name, url)

        coVerify(exactly = 1) { linkRepository.createAndSaveLink(name, CreateAliasRequest(url)) }

        viewModel.uiState.test {
            val currentState = awaitItem()
            assertNull("A mensagem de erro deve ser nula em caso de sucesso", currentState.errorMessage)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onFavoriteClick should call repository with updated entity`() = runTest {
        val linkToFavorite = fakeLink1.copy(isFavorite = false)
        coEvery { linkRepository.getAllLinks() } returns flowOf(listOf(linkToFavorite))
        coEvery { linkRepository.updateLink(any()) } just runs
        val viewModel = HomeViewModel(linkRepository)
        val expectedUpdatedLink = linkToFavorite.copy(isFavorite = true)

        viewModel.uiState.test {
            awaitItem()

            viewModel.onFavoriteClick(linkToFavorite.id)
            coVerify(exactly = 1) { linkRepository.updateLink(expectedUpdatedLink) }
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onErrorMessageShown should clear the error message`() = runTest {
        coEvery { linkRepository.getAllLinks() } returns flowOf(listOf(fakeLink1))
        val errorMessage = "Erro de teste"
        coEvery { linkRepository.deleteLink(any()) } throws IOException(errorMessage)
        val viewModel = HomeViewModel(linkRepository)
        viewModel.deleteLink(UUID.randomUUID())
        viewModel.onErrorMessageShown()
        viewModel.uiState.test {
            val finalState = awaitItem() // Turbine é inteligente e pega o último estado aqui
            assertNull("A mensagem de erro deveria ter sido limpa", finalState.errorMessage)
        }
    }
}