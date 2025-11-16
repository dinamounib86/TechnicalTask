package com.example.aroundegypt

//import app.cash.turbine.test
import com.example.aroundegypt.domain.models.SingleExperienceItem
import com.example.aroundegypt.domain.usecase.GetExperiencesUseCase
import com.example.aroundegypt.domain.usecase.GetRecentExperiencesUseCase
import com.example.aroundegypt.domain.usecase.LikeExperiencesUseCase
import com.example.aroundegypt.domain.usecase.SearchExperiencesUseCase
import com.example.aroundegypt.presentation.home.AEViewModel
import com.example.aroundegypt.presentation.home.HomeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
//import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.example.aroundegypt.data.api.Result
import com.example.aroundegypt.domain.models.LikeResponse
import kotlinx.coroutines.flow.MutableStateFlow
//import io.mockk.coEvery
//import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue


import app.cash.turbine.test
import com.example.aroundegypt.domain.usecase.SingleExperiencesUseCase
import com.example.aroundegypt.presentation.details.DetailsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class AEViewModelTest {

    private val getExperiencesUseCase = mockk<GetExperiencesUseCase>()
    private val getRecentExperiencesUseCase = mockk<GetRecentExperiencesUseCase>()
    private val searchExperiencesUseCase = mockk<SearchExperiencesUseCase>()
    private val likeExperiencesUseCase = mockk<LikeExperiencesUseCase>()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: AEViewModel


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = AEViewModel(
            getExperiencesUseCase,
            getRecentExperiencesUseCase,
            searchExperiencesUseCase,
            likeExperiencesUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    // ------------------ TEST: loadData() success ------------------
    @Test
    fun `loadData emits Success state`() = runTest {
        val expList = listOf(SingleExperienceItem(id = "1"))
        val recentList = listOf(SingleExperienceItem(id = "2"))

        // Suspend functions must use coEvery
        coEvery { getExperiencesUseCase.execute(true) } returns flowOf(Result.Success(expList))
        coEvery { getRecentExperiencesUseCase.execute() } returns flowOf(Result.Success(recentList))

        viewModel.loadData()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.homeState.test {
            val firstState = awaitItem() // Loading
            val secondState = awaitItem() // Success

            assertTrue(secondState is HomeState.Success)
            val success = secondState as HomeState.Success
            assertEquals(expList, success.horizontalList)
            assertEquals(recentList, success.verticalList)
        }
    }



    // ------------------ TEST: searchExperiences ------------------
    @Test
    fun `searchExperiences emits Success with searchList`() = runTest {
        val searchResult = listOf(SingleExperienceItem(id = "10", title = "Search Item"))
        coEvery { searchExperiencesUseCase.execute("Luxor") } returns flowOf(Result.Success(searchResult))

        viewModel.searchExperiences("Luxor")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.homeState.test {
            val firstState = awaitItem() // likely HomeState.Loading
            val secondState = awaitItem() // HomeState.Success with search list

            assertTrue(secondState is HomeState.Success)
            val success = secondState as HomeState.Success
            assertEquals(searchResult, success.searchList)
        }
    }



    // ------------------ TEST: likeExperience updates lists ------------------
    @Test
    fun `likeExperience updates likes count in all lists`() = runTest {

        val initial = listOf(
            SingleExperienceItem(id = "1", title = "A", likesNo = 0)
        )

        // Set initial internal state
        val field = viewModel.javaClass.getDeclaredField("_experiencesList")
        field.isAccessible = true
        (field.get(viewModel) as MutableStateFlow<List<SingleExperienceItem>>)
            .value = initial

        // Fake API Like response
        coEvery { likeExperiencesUseCase.execute("1") } returns
                flowOf(Result.Success(LikeResponse(data = 99)))

        viewModel.likeExperience("1")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.homeState.test()
        {
            val state = awaitItem()
            assertTrue(state is HomeState.Success)

            val updated = (state as HomeState.Success).horizontalList.first()

            assertEquals(99, updated.likesNo)
        }
    }
}

