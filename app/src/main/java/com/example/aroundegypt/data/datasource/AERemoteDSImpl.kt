package com.example.aroundegypt.data.datasource

import com.example.aroundegypt.data.api.AroundEgyptService
import com.example.aroundegypt.data.api.Result
import com.example.aroundegypt.data.api.safeApiCallHelper
import com.example.aroundegypt.domain.datasource.AERemoteDS
import com.example.aroundegypt.domain.models.ExperienceResponse
import com.example.aroundegypt.domain.models.LikeResponse
import com.example.aroundegypt.domain.models.OneExperienceResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

//
// Created by Dina Mounib on 14/11/2025.
//
class AERemoteDSImpl @Inject constructor(
    private val apiService: AroundEgyptService
): AERemoteDS {
    override suspend fun getExperiences(filters: Boolean?): com.example.aroundegypt.data.api.Result<ExperienceResponse> = safeApiCallHelper(Dispatchers.IO) {
        apiService.getRecommendExperiences(true)
    }

    override suspend fun getRecentExperiences(): Result<ExperienceResponse>  = safeApiCallHelper(Dispatchers.IO) {
        apiService.getRecentExperiences()
    }


    override suspend fun getSearchedExperiences(filterWord: String?): Result<ExperienceResponse>  = safeApiCallHelper(Dispatchers.IO) {
        apiService.searchExperiences(filterWord)
    }


    override suspend fun getExperienceDetails(id: String): Result<OneExperienceResponse?>  = safeApiCallHelper(Dispatchers.IO) {
        apiService.getExperienceDetails(id)
    }


    override suspend fun setLikeExperience(id: String): Result<LikeResponse>  = safeApiCallHelper(Dispatchers.IO) {
        apiService.setLikeExperience(id)
    }

}