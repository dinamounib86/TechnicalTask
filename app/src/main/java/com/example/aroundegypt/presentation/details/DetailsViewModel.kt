package com.example.aroundegypt.presentation.details

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aroundegypt.data.api.Result
import com.example.aroundegypt.domain.models.LikeResponse
import com.example.aroundegypt.domain.models.SingleExperienceItem
import com.example.aroundegypt.domain.usecase.LikeExperiencesUseCase
import com.example.aroundegypt.domain.usecase.SingleExperiencesUseCase
import com.example.aroundegypt.presentation.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val singleExperiencesUseCase: SingleExperiencesUseCase,
    private val likeExperiencesUseCase: LikeExperiencesUseCase,
) : ViewModel() {
    val id = mutableStateOf("")
    private val _detailsState = MutableStateFlow<DetailsState>(DetailsState.Loading)
    val detailsState: StateFlow<DetailsState> = _detailsState
        .onStart {loadData(id.value)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _detailsState.value
        )

    fun loadData(id: String) {
        viewModelScope.launch {
            Timber.e("call api ")
            singleExperiencesUseCase.execute(id).collect { res ->
                when (res) {
                    is Result.Error -> {
                        Timber.e("error %s", res.error.toString())
                        _detailsState.value =  DetailsState.Error(res.error.toString())
                    }

                    is Result.Success<SingleExperienceItem?> -> {
                        Timber.e("success %s", res.data.toString())
                        res.data?.let {
                          _detailsState.value = DetailsState.Success(it)
                        }
                    }
                }
            }
        }
    }

    fun likeExperience(id: String) {
        viewModelScope.launch {
            likeExperiencesUseCase.execute(id).collect { res ->
                when (res) {
                    is Result.Error -> Timber.e("LikeExperience error %s", res.error.toString())
                    is Result.Success<LikeResponse> -> {
                        Timber.e("LikeExperience %s", res.data.toString())

                        val current = _detailsState.value
                        if (current is DetailsState.Success) {
                            val updatedDetails = current.singleDetails.copy(
                                likesNo = res.data.data
                            )

                            _detailsState.value = DetailsState.Success(updatedDetails)
                        }
                    }
                }
            }
        }

    }

    sealed class DetailsState {
        object Loading : DetailsState()
        data class Success(
            val singleDetails: SingleExperienceItem,
        ) : DetailsState()

        data class Error (val msg:String): DetailsState()
    }
}
