package br.com.wgc.favoritelink.data.api

import br.com.wgc.favoritelink.data.model.request.CreateAliasRequest
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.UUID

class LinkApiTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var linkApi: LinkApi


    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val okHttpClient = OkHttpClient.Builder().build()

        linkApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(LinkApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `createAlias should make a POST request to the correct endpoint with correct body`() =
        runTest {
            val urlTest = "www.test.com.br"
            val request = CreateAliasRequest(urlTest)


            val mockAliasID = UUID.randomUUID().toString()
            val mockJsonResponse = """
            {
                "alias": "$mockAliasID",
                "_links": {
                    "self": "$urlTest",
                    "short": "${mockWebServer.url("/")}$mockAliasID"
                }
            }
        """.trimIndent()


            mockWebServer.enqueue(MockResponse().setBody(mockJsonResponse).setResponseCode(201))
            val response = linkApi.createAlias(request)
            val recordedRequest = mockWebServer.takeRequest()

            assertEquals("POST", recordedRequest.method)
            assertEquals("/api/alias", recordedRequest.path)
            assertEquals("application/json", recordedRequest.getHeader("Content-Type"))
            assertEquals("""{"url":"$urlTest"}""", recordedRequest.body.readUtf8())

            assertEquals(mockAliasID, response.alias)
            assertEquals(urlTest, response.links.self)
        }
}