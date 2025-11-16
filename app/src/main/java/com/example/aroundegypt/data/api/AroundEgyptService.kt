package com.example.aroundegypt.data.api

import com.example.aroundegypt.domain.models.ExperienceResponse
import com.example.aroundegypt.domain.models.LikeResponse
import com.example.aroundegypt.domain.models.OneExperienceResponse
import com.example.aroundegypt.domain.models.SingleExperienceItem
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface AroundEgyptService {

    @GET("/api/v2/experiences")//?filter[recommended]=true
    suspend fun getRecommendExperiences(
        @Query("filter[recommended]")filter: Boolean? = null
    ): ExperienceResponse

    @GET("/api/v2/experiences")
    suspend fun getRecentExperiences(
    ): ExperienceResponse

    @GET("/api/v2/experiences")//filter
    suspend fun searchExperiences(
        @Query("filter[title]")filter: String? = null
    ): ExperienceResponse

    @GET("api/v2/experiences/{id}")
    suspend fun getExperienceDetails(
        @Path("id") experienceId: String,
    ): OneExperienceResponse

    @POST("api/v2/experiences/{id}/like")
    suspend fun setLikeExperience(
        @Path("id") experienceId: String
    ): LikeResponse
}