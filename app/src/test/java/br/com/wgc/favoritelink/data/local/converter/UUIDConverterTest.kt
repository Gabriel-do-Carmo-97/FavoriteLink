package br.com.wgc.favoritelink.data.local.converter

import org.junit.Assert.*
import org.junit.Test
import java.util.UUID

class UUIDConverterTest {
    private val converter = UUIDConverter()


    @Test
    fun `fromUUID should convert a valid UUID to its String representation`() {
        val uuid = UUID.randomUUID()
        val expectedString = uuid.toString()
        val result = converter.fromUUID(uuid)
        assertEquals(expectedString, result)
    }

    @Test
    fun `fromUUID should return null when given a null UUID`() {
        val uuid: UUID? = null
        val result = converter.fromUUID(uuid)
        assertNull(result)
    }

    @Test
    fun `toUUID should convert a valid String back to its UUID object`() {
        val uuid = UUID.randomUUID()
        val uuidString = uuid.toString()

        val result = converter.toUUID(uuidString)

        assertEquals(uuid, result)
    }

    @Test
    fun `toUUID should return null when given a null String`() {
        val uuidString: String? = null

        val result = converter.toUUID(uuidString)

        assertNull(result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `toUUID should throw IllegalArgumentException for a malformed String`() {
        val malformedString = "isso-nao-e-um-uuid"

        converter.toUUID(malformedString)
    }
}