package br.com.wgc.favoritelink.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(tableName = "links")
data class LinkEntity(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val url: String,
    val isFavorite: Boolean = false
)