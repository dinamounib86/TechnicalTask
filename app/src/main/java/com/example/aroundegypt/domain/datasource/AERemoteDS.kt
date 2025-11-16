package com.example.aroundegypt.domain.datasource

import com.example.aroundegypt.domain.models.ExperienceResponse
import com.example.aroundegypt.domain.models.LikeResponse
import com.example.aroundegypt.domain.models.OneExperienceResponse

//
// Created by Dina Mounib on 14/11/2025.
//
interface  AERemoteDS {

    suspend fun getExperiences(
        filters: Boolean? = null ): com.example.aroundegypt.data.api.Result<ExperienceResponse>

    suspend fun getRecentExperiences(): com.example.aroundegypt.data.api.Result<ExperienceResponse>

    suspend fun getSearchedExperiences(
        filterWord: String? = null ): com.example.aroundegypt.data.api.Result<ExperienceResponse>

    suspend fun getExperienceDetails(
        id: String ): com.example.aroundegypt.data.api.Result<OneExperienceResponse?>

    suspend fun setLikeExperience(
        id: String ): com.example.aroundegypt.data.api.Result<LikeResponse>
}