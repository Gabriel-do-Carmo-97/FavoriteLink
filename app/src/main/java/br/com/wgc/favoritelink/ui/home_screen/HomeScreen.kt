package br.com.wgc.favoritelink.ui.home_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.wgc.favoritelink.ui.components.SearchTextField

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var textSearch by rememberSaveable { mutableStateOf("") }
    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            SearchTextField(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                value = textSearch,
                onValueChange = { textSearch = it },
                label = "Pesquisar",
                leadingIcon = Icons.Default.Search,
            )
            LazyColumn {  }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}