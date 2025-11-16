package com.example.aroundegypt.domain.models

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class LikeResponse(

	@Json(name="pagination")
	val pagination: Pagination? = null,

	@Json(name="data")
	val data: Int? = null,

	@Json(name="meta")
	val meta: Meta? = null
)


