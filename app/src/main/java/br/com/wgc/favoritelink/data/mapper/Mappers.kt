package br.com.wgc.favoritelink.data.mapper

import br.com.wgc.favoritelink.data.local.entity.LinkEntity
import br.com.wgc.favoritelink.ui.components.itemlink.ItemLinkModel


fun LinkEntity.toItemLinkModel() = ItemLinkModel(id, name, url, isFavorite)
fun ItemLinkModel.toLinkEntity() = LinkEntity(id, name, url, isFavorite)