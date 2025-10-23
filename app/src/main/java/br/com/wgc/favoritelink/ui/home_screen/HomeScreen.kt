package br.com.wgc.favoritelink.ui.home_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import br.com.wgc.favoritelink.ui.components.AddLinkDialog
import br.com.wgc.favoritelink.ui.components.ExcludeLinkDialog
import br.com.wgc.favoritelink.ui.components.SearchTextField
import br.com.wgc.favoritelink.ui.components.itemlink.ItemLink
import br.com.wgc.favoritelink.ui.components.itemlink.ItemLinkModel
import java.util.UUID

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var linkToDelete by remember { mutableStateOf<UUID?>(null) }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onErrorMessageShown()
        }
    }

    linkToDelete?.let { id ->
        ExcludeLinkDialog(
            onDismissRequest = {
                linkToDelete = null
            },
            onConfirm = {
                viewModel.deleteLink(id)
                linkToDelete = null
            }
        )
    }

    HomeScreen(
        modifier = modifier,
        state = uiState,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        onSearchTextChanged = viewModel::onSearchTextChanged,
        onFavoriteClick = viewModel::onFavoriteClick,
        onDeleteItem = { linkItem->
            linkToDelete = linkItem
        },
        onAddLinkClick = viewModel::onAddLinkClick
    )
}
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeScreenState,
    snackbarHost: @Composable () -> Unit,
    onSearchTextChanged: (String) -> Unit,
    onFavoriteClick: (UUID) -> Unit,
    onDeleteItem: (UUID) -> Unit,
    onAddLinkClick: (name: String, url: String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        AddLinkDialog(
            onDismissRequest = { showDialog = false },
            onConfirm = { name, url ->
                onAddLinkClick(name, url)
                showDialog = false
            }
        )
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = snackbarHost,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar novo link"
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            SearchTextField(
                modifier = Modifier.padding(16.dp),
                value = state.searchText,
                onValueChange = onSearchTextChanged,
                label = "Pesquisar",
                leadingIcon = Icons.Default.Search,
                iconDescription = "Ícone de pesquisa"
            )

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = state.links,
                    key = { it.id }
                ) { linkItem ->
                    ItemLink(
                        itemLinkModel = ItemLinkModel(
                            name = linkItem.name,
                            url = linkItem.url,
                            isFavorite = linkItem.isFavorite,
                        ),
                        onFavoriteClick = { onFavoriteClick(linkItem.id) },
                        onLongClick = { onDeleteItem(linkItem.id) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    val sampleLinks = List(5) { index ->
        ItemLinkModel(
            name = "Google ${index + 1}",
            url = "https://www.google.com",
            isFavorite = index % 2 == 0,
        )
    }
    val previewState = HomeScreenState(links = sampleLinks, searchText = "")

    MaterialTheme {
        HomeScreen(
            state = previewState,
            snackbarHost = {},
            onSearchTextChanged = {},
            onFavoriteClick = {},
            onAddLinkClick = { _, _ -> },
            onDeleteItem = {}
        )
    }
}