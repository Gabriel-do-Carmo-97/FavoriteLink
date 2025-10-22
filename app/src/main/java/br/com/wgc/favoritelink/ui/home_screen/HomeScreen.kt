package br.com.wgc.favoritelink.ui.home_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.wgc.favoritelink.ui.components.AddLinkDialog
import br.com.wgc.favoritelink.ui.components.ItemLink
import br.com.wgc.favoritelink.ui.components.SearchTextField

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    var textSearch by rememberSaveable { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(true) }
    if (showDialog) {
        AddLinkDialog(
            onDismissRequest = {
                showDialog = false
            },
            onConfirm = { name, url ->
                showDialog = false
            }
        )
    }
    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Pesquisar"
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            SearchTextField(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                value = textSearch,
                onValueChange = { textSearch = it },
                label = "Pesquisar",
                leadingIcon = Icons.Default.Search,
            )
            LazyColumn(
                modifier = Modifier.padding(all = 16.dp)
            ) {
                item {
                    ItemLink(
                        name = "Google",
                        url = "https://www.google.com.br",
                        isFavorite = false,
                        onFavoriteClick = {}
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}