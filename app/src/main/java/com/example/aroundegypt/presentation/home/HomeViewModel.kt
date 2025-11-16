package com.example.aroundegypt.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aroundegypt.data.api.Result
import com.example.aroundegypt.domain.models.LikeResponse
import com.example.aroundegypt.domain.models.SingleExperienceItem
import com.example.aroundegypt.domain.usecase.GetExperiencesUseCase
import com.example.aroundegypt.domain.usecase.GetRecentExperiencesUseCase
import com.example.aroundegypt.domain.usecase.LikeExperiencesUseCase
import com.example.aroundegypt.domain.usecase.SearchExperiencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class AEViewModel @Inject constructor(
    private val getExperiencesUseCase: GetExperiencesUseCase,
    private val getRecentExperiencesUseCase: GetRecentExperiencesUseCase,
    private val searchExperiencesUseCase: SearchExperiencesUseCase,
    private val likeExperiencesUseCase: LikeExperiencesUseCase,
) : ViewModel() {

    private val _experiencesList = MutableStateFlow<List<SingleExperienceItem>>(emptyList())

    private val _recentExperiencesList = MutableStateFlow<List<SingleExperienceItem>>(emptyList())
    private val _searchExperiencesList = MutableStateFlow<List<SingleExperienceItem>?>(null)

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState
        .onStart {
//        _homeState.update {
            loadData()
//        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = _homeState.value
        )

    fun loadData() {
        viewModelScope.launch {
            getExperiencesUseCase.execute(true).collect { res ->
                when (res) {
                    is Result.Error ->   _homeState.value = HomeState.Error(res.error.toString())// Timber.e("error %s", res.error.toString())
                    is Result.Success<List<SingleExperienceItem>?> -> {
                        Timber.e("success %s", res.data.toString())
                        _experiencesList.value = res.data?:emptyList()
                    }
                }
            }
            getRecentExperiencesUseCase.execute().collect { res ->
                when (res) {
                    is Result.Error -> _homeState.value = HomeState.Error(res.error.toString())
                    is Result.Success<List<SingleExperienceItem>?> -> {
                        Timber.e("success %s", res.data.toString())
                        _recentExperiencesList.value = res.data?:emptyList()
                    }
                }
            }
            _homeState.value = HomeState.Success(
                horizontalList = _experiencesList.value,
                verticalList = _recentExperiencesList.value,
                searchList = null
            )
            //HomeState.Success(_experiencesList.value, _recentExperiencesList.value)
        }
    }

    fun searchExperiences(filterWord: String? =null) {
        viewModelScope.launch {
            Timber.e("call api ")
            searchExperiencesUseCase.execute(filterWord).collect { res ->
                when (res) {
                    is Result.Error -> Timber.e("error %s", res.error.toString())
                    is Result.Success<List<SingleExperienceItem>?> -> {
                        Timber.e("success %s", res.data.toString())
                        _searchExperiencesList.value = res.data
                    }
                }
            }
            _homeState.value = HomeState.Success(searchList = _searchExperiencesList.value,
                horizontalList = _experiencesList.value,
                verticalList = _recentExperiencesList.value
            )
        }
    }

    fun likeExperience(id: String) {
        viewModelScope.launch {
            likeExperiencesUseCase.execute(id).collect { res ->
                when (res) {
                    is Result.Error -> Timber.e("LikeExperience error %s", res.error.toString())
                    is Result.Success<LikeResponse> -> {
                        Timber.e("LikeExperience %s", res.data.toString())
                        _experiencesList.value = _experiencesList.value?.map {
                            if(it.id == id){
                                it.copy(likesNo = res.data.data)
                            }else it
                        }?:emptyList()
                        _searchExperiencesList.value = _searchExperiencesList.value?.map {
                            if(it.id == id){
                                it.copy(likesNo = res.data.data)
                            }else it
                        }
                        _recentExperiencesList.value = _recentExperiencesList.value?.map {
                            if(it.id == id){
                                it.copy(likesNo =res.data.data)
                            }else it
                        }?:emptyList()
                        _homeState.value = HomeState.Success(
                            horizontalList = _experiencesList.value,
                            verticalList = _recentExperiencesList.value,
                            searchList = _searchExperiencesList.value
                        )
                    }
                }
            }
        }

    }

    fun handleIntent(homeIntent: HomeIntent) {
        when (homeIntent) {
            is HomeIntent.Search -> {
                searchExperiences(homeIntent.searchKeyword)
            }
            is HomeIntent.Like -> {
                likeExperience(homeIntent.id)
            }
            is HomeIntent.Retry -> {
                loadData()
            }
        }

    }
}


sealed class HomeState {
    object Loading : HomeState()
    data class Success(
        val searchList: List<SingleExperienceItem>?=null,
        val verticalList: List<SingleExperienceItem>,
        val horizontalList: List<SingleExperienceItem>,
    ) : HomeState()
    data class Error(val error: String) : HomeState()
//    data class Search(val searchList: List<SingleExperienceItem>) : HomeState()

}
//like and open expe
sealed class HomeIntent {
    data class Search(val searchKeyword: String) : HomeIntent()
    data class Like(val id: String) : HomeIntent()
    object Retry : HomeIntent()

}