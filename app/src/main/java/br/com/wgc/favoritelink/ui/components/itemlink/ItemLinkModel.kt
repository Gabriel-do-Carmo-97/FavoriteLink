package br.com.wgc.favoritelink.ui.components.itemlink

import java.util.UUID

data class ItemLinkModel(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val url: String,
    val isFavorite: Boolean
)
