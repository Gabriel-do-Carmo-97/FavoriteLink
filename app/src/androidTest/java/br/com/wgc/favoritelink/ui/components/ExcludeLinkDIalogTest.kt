package br.com.wgc.favoritelink.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
class ExcludeLinkDialogTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var onDismissRequest: () -> Unit = mockk {
        every { this@mockk.invoke() } just runs
    }
    private var onConfirm: () -> Unit = mockk {
        every { this@mockk.invoke() } just runs
    }

    @Test
    fun whenDialogIsDisplayed_thenAllElementsAreVisible() {
        composeTestRule.setContent {
            ExcludeLinkDialog(
                onDismissRequest = onDismissRequest,
                onConfirm = onConfirm
            )
        }

        composeTestRule.onNodeWithContentDescription("Ícone de Excluir Link").assertIsDisplayed()
        composeTestRule.onNodeWithText("Deseja excluir").assertIsDisplayed()
        composeTestRule.onNodeWithText("Esse link sera excluido permanentemente")
            .assertIsDisplayed()
        composeTestRule.onNodeWithText("Excluir").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancelar").assertIsDisplayed()
    }

    @Test
    fun whenConfirmButtonIsClicked_thenOnConfirmIsCalled() {
        composeTestRule.setContent {
            ExcludeLinkDialog(
                onDismissRequest = onDismissRequest,
                onConfirm = onConfirm
            )
        }

        composeTestRule.onNodeWithText("Excluir").performClick()
        verify(exactly = 1) { onConfirm.invoke() }
        verify(exactly = 0) { onDismissRequest.invoke() }
    }

    @Test
    fun whenDismissButtonIsClicked_thenOnDismissRequestIsCalled() {
        composeTestRule.setContent {
            ExcludeLinkDialog(
                onDismissRequest = onDismissRequest,
                onConfirm = onConfirm
            )
        }

        composeTestRule.onNodeWithText("Cancelar").performClick()
        verify(exactly = 1) { onDismissRequest.invoke() }
        verify(exactly = 0) { onConfirm.invoke() }
    }
}
