package br.com.wgc.favoritelink.data.local.mapper

import br.com.wgc.favoritelink.data.local.entity.LinkEntity
import br.com.wgc.favoritelink.data.mapper.toItemLinkModel
import br.com.wgc.favoritelink.data.mapper.toLinkEntity
import br.com.wgc.favoritelink.ui.components.itemlink.ItemLinkModel
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals

class MappersTest {

    @Test
    fun `toItemLinkModel should correctly map LinkEntity to ItemLinkModel`() {
        val linkEntity = LinkEntity(
            id = UUID.randomUUID(),
            name = "Test Name",
            url = "https://test.url",
            isFavorite = true
        )

        val itemLinkModel = linkEntity.toItemLinkModel()

        assertEquals(linkEntity.id, itemLinkModel.id)
        assertEquals(linkEntity.name, itemLinkModel.name)
        assertEquals(linkEntity.url, itemLinkModel.url)
        assertEquals(linkEntity.isFavorite, itemLinkModel.isFavorite)
    }

    @Test
    fun `toLinkEntity should correctly map ItemLinkModel to LinkEntity`() {
        val itemLinkModel = ItemLinkModel(
            id = UUID.randomUUID(),
            name = "UI Model Name",
            url = "https://ui.model.url",
            isFavorite = false
        )

        val linkEntity = itemLinkModel.toLinkEntity()

        assertEquals(itemLinkModel.id, linkEntity.id)
        assertEquals(itemLinkModel.name, linkEntity.name)
        assertEquals(itemLinkModel.url, linkEntity.url)
        assertEquals(itemLinkModel.isFavorite, linkEntity.isFavorite)
    }
}