package br.com.wgc.favoritelink.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.wgc.favoritelink.data.local.db.AppDatabase
import br.com.wgc.favoritelink.data.local.entity.LinkEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class LinkDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var linkDao: LinkDao

    @Before
    fun setup(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        linkDao = database.linkDao()
    }

    @After
    fun tearDown(){
        database.close()
    }

    @Test
    fun insertLink_and_getAllLinks_shouldReturnTheLink() = runTest {
        val linkEntity = LinkEntity(
            id = UUID.randomUUID(),
            name = "Google",
            url = "https://www.google.com",
            isFavorite = false
        )

        linkDao.insertLink(linkEntity)

        val allLinks = linkDao.getAllLinks().first()

        Assert.assertEquals(1, allLinks.size)
        Assert.assertEquals(linkEntity.name, allLinks[0].name)
    }

    @Test
    fun updateLink_shouldModifyExistingLink() = runTest {
        val id = UUID.randomUUID()
        val originalLink = LinkEntity(id = id, name = "Original", url = "url", isFavorite = false)
        linkDao.insertLink(originalLink)

        val updatedLink = LinkEntity(id = id, name = "Updated", url = "url", isFavorite = true)
        linkDao.updateLink(updatedLink)

        val allLinks = linkDao.getAllLinks().first()
        Assert.assertEquals("Updated", allLinks[0].name)
        Assert.assertTrue(allLinks[0].isFavorite)
    }

    @Test
    fun deleteLinkById_shouldRemoveTheLink() = runTest {
        val linkToDelete = LinkEntity(id = UUID.randomUUID(), name = "Delete Me", url = "url")
        linkDao.insertLink(linkToDelete)

        var allLinks = linkDao.getAllLinks().first()
        Assert.assertFalse(allLinks.isEmpty())

        linkDao.deleteLinkById(linkToDelete.id)

        allLinks = linkDao.getAllLinks().first()
        Assert.assertTrue(allLinks.isEmpty())
    }
}