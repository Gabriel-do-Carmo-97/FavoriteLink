package br.com.wgc.favoritelink.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
class AddLinkDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var onDismissRequest: () -> Unit = mockk {
        every { this@mockk.invoke() } just runs
    }
    private var onConfirm: (name: String, url: String) -> Unit = mockk {
        every { this@mockk.invoke(any(), any()) } just runs
    }

    @Test
    fun whenDialogIsFirstDisplayed_thenAllElementsAreVisible() {
        composeTestRule.setContent {
            AddLinkDialog(onDismissRequest = onDismissRequest, onConfirm = onConfirm)
        }

        composeTestRule.onNodeWithText("Adicionar Novo Link").assertIsDisplayed()
        composeTestRule.onNodeWithText("Nome do site").assertIsDisplayed()
        composeTestRule.onNodeWithText("URL (ex: https://google.com)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Adicionar").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancelar").assertIsDisplayed()
    }

    @Test
    fun whenFieldsAreEmpty_thenConfirmButtonIsDisabled() {
        composeTestRule.setContent {
            AddLinkDialog(onDismissRequest = onDismissRequest, onConfirm = onConfirm)
        }

        composeTestRule.onNodeWithText("Adicionar").assertIsNotEnabled()
    }

    @Test
    fun whenBothFieldsAreFilled_thenConfirmButtonIsEnabled() {
        composeTestRule.setContent {
            AddLinkDialog(onDismissRequest = onDismissRequest, onConfirm = onConfirm)
        }

        composeTestRule.onNodeWithText("Nome do site").performTextInput("Google")
        composeTestRule.onNodeWithText("URL (ex: https://google.com)")
            .performTextInput("https://google.com")
        composeTestRule.onNodeWithText("Adicionar").assertIsEnabled()
    }

    @Test
    fun whenOnlyOneFieldIsFilled_thenConfirmButtonIsDisabled() {
        composeTestRule.setContent {
            AddLinkDialog(onDismissRequest = onDismissRequest, onConfirm = onConfirm)
        }
        composeTestRule.onNodeWithText("Nome do site").performTextInput("Google")
        composeTestRule.onNodeWithText("Adicionar").assertIsNotEnabled()
    }

    @Test
    fun whenConfirmButtonIsClicked_thenOnConfirmIsCalledWithCorrectData() {
        val nameInput = "Google"
        val urlInput = "https://google.com"

        composeTestRule.setContent {
            AddLinkDialog(onDismissRequest = onDismissRequest, onConfirm = onConfirm)
        }
        composeTestRule.onNodeWithText("Nome do site").performTextInput(nameInput)
        composeTestRule.onNodeWithText("URL (ex: https://google.com)").performTextInput(urlInput)
        composeTestRule.onNodeWithText("Adicionar").performClick()

        verify(exactly = 1) { onConfirm(nameInput, urlInput) }
        verify(exactly = 0) { onDismissRequest() }
    }

    @Test
    fun whenDismissButtonIsClicked_thenOnDismissRequestIsCalled() {
        composeTestRule.setContent {
            AddLinkDialog(onDismissRequest = onDismissRequest, onConfirm = onConfirm)
        }
        composeTestRule.onNodeWithText("Cancelar").performClick()
        verify(exactly = 1) { onDismissRequest() }
        verify(exactly = 0) {
            onConfirm(
                any(),
                any()
            )
        }
    }
}
