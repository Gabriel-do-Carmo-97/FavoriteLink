package br.com.wgc.favoritelink.ui.home_screen

import br.com.wgc.favoritelink.ui.components.itemlink.ItemLinkModel

data class HomeScreenState(
    val links: List<ItemLinkModel> = emptyList(),
    val searchText: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)