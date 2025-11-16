package com.example.aroundegypt.domain.repo

import com.example.aroundegypt.data.api.Result
import com.example.aroundegypt.domain.models.LikeResponse
import com.example.aroundegypt.domain.models.SingleExperienceItem
import kotlinx.coroutines.flow.Flow


interface AERepository {

    suspend fun getExperiences(
        filters: Boolean? = null  ): Flow<Result<List<SingleExperienceItem>?>>

    suspend fun getRecentExperiences(): Flow<Result<List<SingleExperienceItem>?>>

    suspend fun getSearchedExperiences(
        filterWord: String? = null ): Flow<Result<List<SingleExperienceItem>?>>

    suspend fun getExperienceDetails(
        id: String ): Flow<Result<SingleExperienceItem?>>

    suspend fun setLikeExperience(
        id: String ): Flow<Result<LikeResponse>>
}