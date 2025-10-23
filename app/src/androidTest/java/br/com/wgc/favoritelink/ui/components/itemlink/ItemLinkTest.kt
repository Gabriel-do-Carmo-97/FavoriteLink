package br.com.wgc.favoritelink.ui.components.itemlink

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.longClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ItemLinkTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    private val onFavoriteClick: () -> Unit = mockk {
        every { this@mockk.invoke() } just runs
    }
    private val onLongClick: () -> Unit = mockk {
        every { this@mockk.invoke() } just runs
    }

    @Test
    fun whenItemIsRenderedNameAndUrlShouldBeDisplayed() {
        val model = ItemLinkModel(name = "Google", url = "https://google.com", isFavorite = false)

        composeTestRule.setContent {
            ItemLink(
                itemLinkModel = model,
                onFavoriteClick = {},
                onLongClick = {}
            )
        }

        composeTestRule.onNodeWithText("Google").assertIsDisplayed()
        composeTestRule.onNodeWithText("https://google.com").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Adicionar aos favoritos").assertIsDisplayed()
    }

    @Test
    fun whenIsFavoriteIsFalse_thenEmptyFavoriteIconIsDisplayed() {
        val model = ItemLinkModel(name = "Test", url = "url", isFavorite = false)
        composeTestRule.setContent {
            ItemLink(itemLinkModel = model, onFavoriteClick = {}, onLongClick = {})
        }

        composeTestRule.onNodeWithContentDescription("Adicionar aos favoritos").assertIsDisplayed()
    }

    @Test
    fun whenIsFavoriteIsTrue_thenFilledFavoriteIconIsDisplayed() {
        // Arrange
        val model = ItemLinkModel(name = "Test", url = "url", isFavorite = true)
        composeTestRule.setContent {
            ItemLink(itemLinkModel = model, onFavoriteClick = {}, onLongClick = {})
        }

        composeTestRule.onNodeWithContentDescription("Remover dos favoritos").assertIsDisplayed()
    }

    @Test
    fun whenFavoriteIconIsClicked_thenOnFavoriteClickIsCalled() {
        val model = ItemLinkModel(name = "Test", url = "url", isFavorite = false)
        composeTestRule.setContent {
            ItemLink(
                itemLinkModel = model,
                onFavoriteClick = onFavoriteClick,
                onLongClick = onLongClick
            )
        }

        composeTestRule.onNodeWithContentDescription("Adicionar aos favoritos").performClick()

        verify(exactly = 1) { onFavoriteClick.invoke() }
        verify(exactly = 0) { onLongClick.invoke() }
    }

    @Test
    fun whenItemIsLongClicked_thenOnLongClickIsCalled() {
        val model = ItemLinkModel(name = "Test", url = "url", isFavorite = false)
        composeTestRule.setContent {
            ItemLink(
                itemLinkModel = model,
                onFavoriteClick = onFavoriteClick,
                onLongClick = onLongClick // Use o mock
            )
        }

        composeTestRule.onNodeWithText("Test").performTouchInput {
            longClick()
        }

        verify(exactly = 1) { onLongClick.invoke() }
        verify(exactly = 0) { onFavoriteClick.invoke() }
    }
}