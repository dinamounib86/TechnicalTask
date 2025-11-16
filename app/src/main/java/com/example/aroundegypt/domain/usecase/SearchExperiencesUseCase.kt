package com.example.aroundegypt.domain.usecase

import com.example.aroundegypt.data.api.Result
import com.example.aroundegypt.domain.models.SingleExperienceItem
import com.example.aroundegypt.domain.repo.AERepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SearchExperiencesUseCase @Inject constructor(
    private val aerRepository: AERepository
) {

    suspend fun execute(filterWord: String? = null ): Flow<Result<List<SingleExperienceItem>?>> = aerRepository.getSearchedExperiences(filterWord)
}