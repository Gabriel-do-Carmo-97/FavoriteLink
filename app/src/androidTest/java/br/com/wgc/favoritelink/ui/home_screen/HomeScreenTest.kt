package br.com.wgc.favoritelink.ui.home_screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.longClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.wgc.favoritelink.ui.components.itemlink.ItemLinkModel
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID


@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Mocks para todos os callbacks da tela
    private var onSearchTextChanged: (String) -> Unit= mockk {
        every { this@mockk.invoke(any()) } just runs
    }
    private var onFavoriteClick: (UUID) -> Unit= mockk {
        every { this@mockk.invoke(any()) } just runs
    }
    private var onDeleteItem: (UUID) -> Unit= mockk{
        every { this@mockk.invoke(any()) } just runs
    }
    private var onAddLinkClick: (name: String, url: String) -> Unit= mockk {
        every { this@mockk.invoke(any(), any()) } just runs
    }

    @Test
    fun whenStateHasLinks_thenLinksAreDisplayed() {
        // Arrange
        val link1Id = UUID.randomUUID()
        val links = listOf(
            ItemLinkModel(id = link1Id, name = "Google", url = "google.com",isFavorite = false),
            ItemLinkModel(id = UUID.randomUUID(), name = "Android Dev", url = "developer.android.com", isFavorite = false)
        )
        val state = HomeScreenState(links = links)

        composeTestRule.setContent {
            HomeScreen(
                state = state,
                snackbarHost = {},
                onSearchTextChanged = onSearchTextChanged,
                onFavoriteClick = onFavoriteClick,
                onDeleteItem = onDeleteItem,
                onAddLinkClick = onAddLinkClick
            )
        }

        composeTestRule.onNodeWithText("Google").assertIsDisplayed()
        composeTestRule.onNodeWithText("Android Dev").assertIsDisplayed()
    }

    @Test
    fun whenSearchTextIsProvided_thenItIsDisplayedInSearchField() {
        val state = HomeScreenState(searchText = "test search")

        composeTestRule.setContent {
            HomeScreen(state = state, snackbarHost = {}, onSearchTextChanged = onSearchTextChanged, onFavoriteClick = onFavoriteClick, onDeleteItem = onDeleteItem, onAddLinkClick = onAddLinkClick)
        }
        composeTestRule.onNodeWithText("test search").assertIsDisplayed()
    }

    @Test
    fun whenUserTypesInSearchField_thenOnSearchTextChangedIsCalled() {
        val state = HomeScreenState(searchText = "")
        composeTestRule.setContent {
            HomeScreen(state = state, snackbarHost = {}, onSearchTextChanged = onSearchTextChanged, onFavoriteClick = onFavoriteClick, onDeleteItem = onDeleteItem, onAddLinkClick = onAddLinkClick)
        }
        composeTestRule.onNodeWithText("Pesquisar").performTextInput("kotlin")
        verify(exactly = 1) { onSearchTextChanged("kotlin") }
    }

    /**
     * Teste 4: Simula um clique longo em um item e verifica se o callback de exclusão é chamado.
     */
    @Test
    fun whenItemIsLongClicked_thenOnDeleteItemIsCalled() {
        val linkId = UUID.randomUUID()
        val links = listOf(ItemLinkModel(id = linkId, name = "Item to delete", url = "url", isFavorite = false))
        val state = HomeScreenState(links = links)
        composeTestRule.setContent {
            HomeScreen(state = state, snackbarHost = {}, onSearchTextChanged = onSearchTextChanged, onFavoriteClick = onFavoriteClick, onDeleteItem = onDeleteItem, onAddLinkClick = onAddLinkClick)
        }
        composeTestRule.onNodeWithText("Item to delete").performTouchInput {
            longClick()
        }
        verify(exactly = 1) { onDeleteItem(linkId) }
    }

    @Test
    fun whenFavoriteIconIsClicked_thenOnFavoriteClickIsCalled() {
        val linkId = UUID.randomUUID()
        val links = listOf(ItemLinkModel(id = linkId, name = "Item to favorite", url = "url", isFavorite = false))
        val state = HomeScreenState(links = links)
        composeTestRule.setContent {
            HomeScreen(state = state, snackbarHost = {}, onSearchTextChanged = onSearchTextChanged, onFavoriteClick = onFavoriteClick, onDeleteItem = onDeleteItem, onAddLinkClick = onAddLinkClick)
        }
        composeTestRule.onNodeWithContentDescription("Adicionar aos favoritos").performClick()
        verify(exactly = 1) { onFavoriteClick(linkId) }
    }

    @Test
    fun whenFabIsClicked_thenAddLinkDialogIsDisplayed() {
        val state = HomeScreenState()
        composeTestRule.setContent {
            HomeScreen(state = state, snackbarHost = {}, onSearchTextChanged = onSearchTextChanged, onFavoriteClick = onFavoriteClick, onDeleteItem = onDeleteItem, onAddLinkClick = onAddLinkClick)
        }
        composeTestRule.onNodeWithContentDescription("Adicionar novo link").performClick()
        composeTestRule.onNodeWithText("Adicionar Novo Link").assertIsDisplayed()
    }

    @Test
    fun whenAddDialogIsConfirmed_thenOnAddLinkClickIsCalled() {
        val state = HomeScreenState()
        composeTestRule.setContent {
            HomeScreen(state = state, snackbarHost = {}, onSearchTextChanged = onSearchTextChanged, onFavoriteClick = onFavoriteClick, onDeleteItem = onDeleteItem, onAddLinkClick = onAddLinkClick)
        }
        composeTestRule.onNodeWithContentDescription("Adicionar novo link").performClick()
        composeTestRule.onNodeWithText("Nome do site").performTextInput("Test Name")
        composeTestRule.onNodeWithText("URL (ex: https://google.com)").performTextInput("http://test.url")
        composeTestRule.onNodeWithText("Adicionar").performClick()
        verify(exactly = 1) { onAddLinkClick("Test Name", "http://test.url") }
    }

    @Test
    fun whenAddDialogIsDismissed_thenDialogDisappears() {
        val state = HomeScreenState()
        composeTestRule.setContent {
            HomeScreen(state = state, snackbarHost = {}, onSearchTextChanged = onSearchTextChanged, onFavoriteClick = onFavoriteClick, onDeleteItem = onDeleteItem, onAddLinkClick = onAddLinkClick)
        }
        composeTestRule.onNodeWithContentDescription("Adicionar novo link").performClick()
        composeTestRule.onNodeWithText("Adicionar Novo Link").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancelar").performClick()
        composeTestRule.onNodeWithText("Adicionar Novo Link").assertDoesNotExist()
    }
}