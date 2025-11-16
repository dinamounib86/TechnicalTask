package com.example.aroundegypt.data.repo

import com.example.aroundegypt.data.api.Result
import com.example.aroundegypt.domain.datasource.AERemoteDS
import com.example.aroundegypt.domain.models.LikeResponse
import com.example.aroundegypt.domain.models.SingleExperienceItem
import com.example.aroundegypt.domain.repo.AERepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import javax.inject.Inject


class AERepositoryImpl  @Inject constructor(
    private val remoteDS: AERemoteDS
) : AERepository {
    override suspend fun getExperiences(filters: Boolean?): Flow<Result<List<SingleExperienceItem>?>> {
        return flow {
            when (val res = remoteDS.getExperiences(filters)) {
                is Result.Error ->  emit(Result.Error(res.error))
                is Result.Success ->  emit(Result.Success(res.data.data))
            }
        }.flowOn(
            Dispatchers.IO
        )
    }

    override suspend fun getRecentExperiences(): Flow<Result<List<SingleExperienceItem>?>> {
        return flow {
            when (val res = remoteDS.getRecentExperiences()) {
                is Result.Error ->  emit(Result.Error(res.error))
                is Result.Success ->  emit(Result.Success(res.data.data))
            }
        }.flowOn(
            Dispatchers.IO
        )
    }

    override suspend fun getSearchedExperiences(filterWord: String?): Flow<Result<List<SingleExperienceItem>?>> {
        return flow {
            when (val res = remoteDS.getSearchedExperiences(filterWord)) {
                is Result.Error ->  emit(Result.Error(res.error))
                is Result.Success ->  emit(Result.Success(res.data.data))
            }
        }.flowOn(
            Dispatchers.IO
        )
    }

    override suspend fun getExperienceDetails(id: String): Flow<Result<SingleExperienceItem?>> {
        return flow {
            when (val res = remoteDS.getExperienceDetails(id)) {
                is Result.Error ->  emit(Result.Error(res.error))
                is Result.Success ->  emit(Result.Success(res.data?.data))
            }
        }.flowOn(
            Dispatchers.IO
        )
    }

    override suspend fun setLikeExperience(id: String): Flow<Result<LikeResponse>> {
        return flow {
            when (val res = remoteDS.setLikeExperience(id)) {
                is Result.Error ->  emit(Result.Error(res.error))
                is Result.Success ->  emit(Result.Success(res.data))
            }
        }.flowOn(
            Dispatchers.IO
        )
    }
}
