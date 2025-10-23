package br.com.wgc.favoritelink.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.wgc.favoritelink.data.local.converter.UUIDConverter
import br.com.wgc.favoritelink.data.local.dao.LinkDao
import br.com.wgc.favoritelink.data.local.entity.LinkEntity

@TypeConverters(UUIDConverter::class)
@Database(entities = [LinkEntity::class], version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun linkDao(): LinkDao
}