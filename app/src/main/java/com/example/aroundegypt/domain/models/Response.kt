package com.example.aroundegypt.domain.models

import com.squareup.moshi.JsonClass
import com.squareup.moshi.Json

@JsonClass(generateAdapter = true)
data class ExperienceResponse(

	@Json(name="pagination")
	val pagination: Pagination? = null,

	@Json(name="data")
	val data: List<SingleExperienceItem>? = null,

	@Json(name="meta")
	val meta: Meta? = null
)

@JsonClass(generateAdapter = true)
data class OneExperienceResponse(

	@Json(name="pagination")
	val pagination: Pagination? = null,

	@Json(name="data")
	val data: SingleExperienceItem? = null,

	@Json(name="meta")
	val meta: Meta? = null
)

@JsonClass(generateAdapter = true)
data class Saturday(

	@Json(name="time")
	val time: String? = null,

	@Json(name="day")
	val day: String? = null
)

@JsonClass(generateAdapter = true)
data class Wednesday(

	@Json(name="time")
	val time: String? = null,

	@Json(name="day")
	val day: String? = null
)

@JsonClass(generateAdapter = true)
data class Meta(

	@Json(name="code")
	val code: Int? = null,

	@Json(name="errors")
	val errors: List<Any?>? = null
)

@JsonClass(generateAdapter = true)
data class TicketPricesItem(

	@Json(name="price")
	val price: Int? = null,

	@Json(name="type")
	val type: String? = null
)

@JsonClass(generateAdapter = true)
data class ReviewsItem(

	@Json(name="name")
	val name: String? = null,

	@Json(name="rating")
	val rating: Int? = null,

	@Json(name="created_at")
	val createdAt: String? = null,

	@Json(name="comment")
	val comment: String? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="experience")
	val experience: String? = null
)

@JsonClass(generateAdapter = true)
data class Pagination(
	val any: Any? = null
)

@JsonClass(generateAdapter = true)
data class SingleExperienceItem(

	@Json(name="cover_photo")
	val coverPhoto: String? = null,

	@Json(name="has_audio")
	val hasAudio: Boolean? = null,

	@Json(name="city")
	val city: City? = null,

	@Json(name="gmap_location")
	val gmapLocation: GmapLocation? = null,

	@Json(name="famous_figure")
	val famousFigure: String? = null,

	@Json(name="rating")
	val rating: Int? = null,

	@Json(name="description")
	val description: String? = null,

	@Json(name="title")
	val title: String? = null,

	@Json(name="has_video")
	val hasVideo: Int? = null,

	@Json(name="experience_tips")
	val experienceTips: List<Any?>? = null,

	@Json(name="reviews")
	val reviews: List<ReviewsItem?>? = null,

	@Json(name="era")
	val era: Era? = null,

	@Json(name="starting_price")
	val startingPrice: Int? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="translated_opening_hours")
	val translatedOpeningHours: TranslatedOpeningHours? = null,

	@Json(name="is_liked")
	val isLiked: Any? = null,

	@Json(name="tour_html")
	val tourHtml: String? = null,

	@Json(name="period")
	val period: Any? = null,

	@Json(name="address")
	val address: String? = null,

	@Json(name="views_no")
	val viewsNo: Int? = null,

	@Json(name="founded")
	val founded: String? = null,

	@Json(name="likes_no")
	val likesNo: Int? = null,

	@Json(name="reviews_no")
	val reviewsNo: Int? = null,

	@Json(name="recommended")
	val recommended: Int? = null,

	@Json(name="tags")
	val tags: List<TagsItem?>? = null,

	@Json(name="detailed_description")
	val detailedDescription: String? = null,

	@Json(name="ticket_prices")
	val ticketPrices: List<TicketPricesItem?>? = null,

	@Json(name="opening_hours")
	val openingHours: OpeningHours? = null,

	@Json(name="audio_url")
	val audioUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class Period(

	@Json(name="updated_at")
	val updatedAt: String? = null,

	@Json(name="created_at")
	val createdAt: String? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="value")
	val value: String? = null
)

@JsonClass(generateAdapter = true)
data class Sunday(

	@Json(name="time")
	val time: String? = null,

	@Json(name="day")
	val day: String? = null
)

@JsonClass(generateAdapter = true)
data class City(

	@Json(name="top_pick")
	val topPick: Int? = null,

	@Json(name="disable")
	val disable: Any? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="id")
	val id: Int? = null
)

@JsonClass(generateAdapter = true)
data class TagsItem(

	@Json(name="top_pick")
	val topPick: Int? = null,

	@Json(name="disable")
	val disable: Any? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="id")
	val id: Int? = null
)

@JsonClass(generateAdapter = true)
data class Era(

	@Json(name="updated_at")
	val updatedAt: String? = null,

	@Json(name="created_at")
	val createdAt: String? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="value")
	val value: String? = null
)

@JsonClass(generateAdapter = true)
data class Thursday(

	@Json(name="time")
	val time: String? = null,

	@Json(name="day")
	val day: String? = null
)

@JsonClass(generateAdapter = true)
data class TranslatedOpeningHours(

	@Json(name="sunday")
	val sunday: Sunday? = null,

	@Json(name="saturday")
	val saturday: Saturday? = null,

	@Json(name="tuesday")
	val tuesday: Tuesday? = null,

	@Json(name="wednesday")
	val wednesday: Wednesday? = null,

	@Json(name="thursday")
	val thursday: Thursday? = null,

	@Json(name="friday")
	val friday: Friday? = null,

	@Json(name="monday")
	val monday: Monday? = null
)

@JsonClass(generateAdapter = true)
data class Friday(

	@Json(name="time")
	val time: String? = null,

	@Json(name="day")
	val day: String? = null
)

@JsonClass(generateAdapter = true)
data class OpeningHours(

	@Json(name="sunday")
	val sunday: List<String?>? = null,

	@Json(name="saturday")
	val saturday: List<String?>? = null,

	@Json(name="tuesday")
	val tuesday: List<String?>? = null,

	@Json(name="wednesday")
	val wednesday: List<String?>? = null,

	@Json(name="thursday")
	val thursday: List<String?>? = null,

	@Json(name="friday")
	val friday: List<String?>? = null,

	@Json(name="monday")
	val monday: List<String?>? = null
)

@JsonClass(generateAdapter = true)
data class Tuesday(

	@Json(name="time")
	val time: String? = null,

	@Json(name="day")
	val day: String? = null
)

@JsonClass(generateAdapter = true)
data class GmapLocation(

	@Json(name="coordinates")
	val coordinates: List<Any?>? = null,

	@Json(name="type")
	val type: String? = null
)

@JsonClass(generateAdapter = true)
data class Monday(

	@Json(name="time")
	val time: String? = null,

	@Json(name="day")
	val day: String? = null
)
