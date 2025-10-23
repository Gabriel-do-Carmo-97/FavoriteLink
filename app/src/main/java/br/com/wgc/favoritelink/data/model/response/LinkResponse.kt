package br.com.wgc.favoritelink.data.model.response

import com.squareup.moshi.Json

data class LinkResponse(
    @Json(name = "_links") val links: Links,
    val alias: String
)