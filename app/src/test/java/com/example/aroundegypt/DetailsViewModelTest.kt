package com.example.aroundegypt

import app.cash.turbine.test
import com.example.aroundegypt.domain.models.LikeResponse
import com.example.aroundegypt.domain.models.SingleExperienceItem
import com.example.aroundegypt.domain.usecase.LikeExperiencesUseCase
import com.example.aroundegypt.domain.usecase.SingleExperiencesUseCase
import com.example.aroundegypt.presentation.details.DetailsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import com.example.aroundegypt.data.api.Result
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var singleExperiencesUseCase: SingleExperiencesUseCase
    private lateinit var likeExperiencesUseCase: LikeExperiencesUseCase
    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        singleExperiencesUseCase = mockk()
        likeExperiencesUseCase = mockk()

        viewModel = DetailsViewModel(
            singleExperiencesUseCase,
            likeExperiencesUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ────────── loadData Success ──────────
    @Test
    fun `loadData returns success`() = runTest {
        val fakeItem = SingleExperienceItem(id = "1", title = "Test", likesNo = 3)
        viewModel.id.value = "1"
        coEvery { singleExperiencesUseCase.execute(any()) } returns flowOf(Result.Success(fakeItem))

        viewModel.loadData("1")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.detailsState.test {
            awaitItem() // initial Loading
            val emission = awaitItem()
            assert(emission is DetailsViewModel.DetailsState.Success)
            val success = emission as DetailsViewModel.DetailsState.Success
            assert(success.singleDetails.id == "1")
            assert(success.singleDetails.likesNo == 3)
        }
    }

    // ────────── loadData Error ──────────
    @Test
    fun `loadData returns error`() = runTest {
        val errorMessage = "Network error"
        coEvery { singleExperiencesUseCase.execute(any()) } returns flowOf(Result.Error(errorMessage))

        viewModel.loadData("1")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.detailsState.test {
            awaitItem() // Loading
            val emission = awaitItem()
            assert(emission is DetailsViewModel.DetailsState.Error)
            val errorState = emission as DetailsViewModel.DetailsState.Error
            assert(errorState.msg == errorMessage)
        }
    }

    // ────────── likeExperience updates likes ──────────
    @Test
    fun `likeExperience updates likes`() = runTest {
        val initialItem = SingleExperienceItem(id = "1", title = "Test", likesNo = 10)
        val response = LikeResponse(data = 11)

        coEvery { likeExperiencesUseCase.execute(any()) } returns flowOf(Result.Success(response))

        viewModel.setTestState(DetailsViewModel.DetailsState.Success(initialItem))

        viewModel.likeExperience("1")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.detailsState.test {
            awaitItem() // initial emission
            val state = awaitItem()
            assert(state is DetailsViewModel.DetailsState.Success)
            val success = state as DetailsViewModel.DetailsState.Success
            assert(success.singleDetails.likesNo == 11)
        }
    }
}


