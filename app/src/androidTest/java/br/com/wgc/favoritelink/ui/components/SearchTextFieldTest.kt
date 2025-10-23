package br.com.wgc.favoritelink.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
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
class SearchTextFieldTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val onValueChange: (String) -> Unit = mockk {
        every { this@mockk.invoke(any()) } just runs
    }

    @Test
    fun whenComponentIsRendered_thenLabelIsDisplayed() {
        composeTestRule.setContent {
            SearchTextField(
                value = "",
                onValueChange = onValueChange,
                label = "Pesquisar",
            )
        }
        composeTestRule.onNodeWithText("Pesquisar").assertIsDisplayed()
    }

    @Test
    fun whenValueIsProvided_thenTextIsDisplayedInField() {
        composeTestRule.setContent {
            SearchTextField(
                value = "Texto de teste",
                onValueChange = onValueChange,
                label = "Pesquisar",
            )
        }
        composeTestRule.onNodeWithText("Texto de teste").assertIsDisplayed()
    }

    @Test
    fun whenLeadingIconIsProvided_thenIconIsDisplayed() {
        composeTestRule.setContent {
            SearchTextField(
                value = "",
                onValueChange = onValueChange,
                label = "Pesquisar",
                leadingIcon = Icons.Default.Search,
                iconDescription = "Ícone de pesquisa"
            )
        }
        composeTestRule.onNodeWithContentDescription("Ícone de pesquisa").assertIsDisplayed()
    }

    @Test
    fun whenUserTypesInField_thenOnValueChangeIsCalledWithCorrectText() {
        val typedText = "kotlin"
        composeTestRule.setContent {
            SearchTextField(
                value = "",
                onValueChange = onValueChange,
                label = "Pesquisar"
            )
        }
        composeTestRule.onNodeWithText("Pesquisar").performTextInput(typedText)
        verify(exactly = 1) { onValueChange(typedText) }
    }
}
