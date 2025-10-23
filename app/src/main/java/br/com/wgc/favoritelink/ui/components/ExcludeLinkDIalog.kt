package br.com.wgc.favoritelink.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun ExcludeLinkDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismissRequest,
        icon = {
            Icon(
                imageVector = Icons.Default.Cancel,
                contentDescription = "Ícone de Excluir Link"
            )
        },
        title = {
            Text("Deseja excluir")
        },
        text = {
            Text("Esse link sera excluido permanentemente")
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Excluir")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancelar")
            }
        }
    )
}


@Preview
@Composable
private fun ExcludeLinkDialogPreview() {
    MaterialTheme {
        ExcludeLinkDialog(
            onDismissRequest = {},
            onConfirm = { }
        )
    }
}