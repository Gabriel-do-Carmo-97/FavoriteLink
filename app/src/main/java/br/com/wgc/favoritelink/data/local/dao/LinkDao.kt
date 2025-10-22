package br.com.wgc.favoritelink.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.wgc.favoritelink.data.local.entity.LinkEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID


@Dao
interface LinkDao {

    @Query("SELECT * FROM links ORDER BY name ASC")
    fun getAllLinks(): Flow<List<LinkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLink(link: LinkEntity)

    @Update
    suspend fun updateLink(link: LinkEntity)

    @Query("DELETE FROM links WHERE id = :id")
    suspend fun deleteLinkById(id: UUID)
}