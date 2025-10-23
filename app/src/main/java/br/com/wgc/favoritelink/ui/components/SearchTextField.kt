package br.com.wgc.favoritelink.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector? = null,
    iconDescription: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(100),
        leadingIcon = { if (leadingIcon != null) Icon(leadingIcon, iconDescription) },
        label = { Text(text = label) },
        maxLines = 1,
        singleLine = true,
        placeholder = { Text(text = "Pesquisar") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
            focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchFieldPrev() {
    SearchTextField(
        value = "",
        onValueChange = {},
        label = "Pesquisar"
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SearchFieldPrev2() {
    SearchTextField(
        value = "",
        onValueChange = {},
        label = "Pesquisar",
        leadingIcon = Icons.Default.Search
    )
}

