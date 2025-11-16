package com.example.aroundegypt.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.aroundegypt.R
import com.example.aroundegypt.Utils.composables.AppAsyncImage
import com.example.aroundegypt.domain.models.City
import com.example.aroundegypt.domain.models.SingleExperienceItem
import com.example.aroundegypt.presentation.details.ExperienceDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeStateful(modifier: Modifier, aeViewModel: AEViewModel = hiltViewModel()) {

    val homeState = aeViewModel.homeState.collectAsStateWithLifecycle()
    val openExperience = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val selectedExpID = remember { mutableStateOf("") }

    when (val uiState = homeState.value) {
        is HomeState.Error -> ErrorView(uiState.error,
            { aeViewModel.handleIntent(HomeIntent.Retry) })

        HomeState.Loading -> LoadingView()


        is HomeState.Success -> {
            SuccessView(
                modifier,
                uiState.verticalList,
                uiState.horizontalList,
                uiState.searchList,
                {aeViewModel.handleIntent(HomeIntent.Retry)},
                { keyWord ->
                    aeViewModel.handleIntent(HomeIntent.Search(searchKeyword = keyWord))
                }, { itemID ->
                    aeViewModel.handleIntent(HomeIntent.Like(id = itemID))
                }) {
                selectedExpID.value = it
                openExperience.value = true
            }
        }
    }

    if (openExperience.value) {
        // Open Experience Details
        ModalBottomSheet(
            onDismissRequest = { openExperience.value = false },
            sheetState = sheetState
        ) {
            ExperienceDetails(selectedExpID.value)
        }

    }

}

@Composable
fun ErrorView(errorMsg: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            imageVector = Icons.Default.Build,
            contentDescription = null,
            tint = Color.Red,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = errorMsg,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onRetry() },
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = "Retry")
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


@Composable
fun SuccessView(
    modifier: Modifier,
    verticalList: List<SingleExperienceItem>?,
    horizontalList: List<SingleExperienceItem>?,
    searchList: List<SingleExperienceItem>?,
    onCancelSearch: () -> Unit,
    onSearchExperiences: (String) -> Unit,
    onLikeExperience: (String) -> Unit,
    openExperience: (String) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .background(Color.White)
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        SearchBar(onCancelSearch,{ filterText ->
            onSearchExperiences(filterText)
        })
        if(searchList!=null)
            verticalScrollExperiences(searchList, likeClick = { itemId ->
                onLikeExperience(itemId)
            }) { itemID ->
                openExperience(itemID)
            }
        else{
        welcomeSection()
        headerSection(R.string.recommended_experiences)
        horizontalScrollExperiences(horizontalList, likeClick = { itemId ->
            onLikeExperience(itemId)
        }) { itemID ->
            openExperience(itemID)
        }
        headerSection(R.string.most_recent)
        verticalScrollExperiences(verticalList, likeClick = { itemId ->
            onLikeExperience(itemId)
        }) { itemID ->
            openExperience(itemID)
        }
        }
    }
}

fun LazyListScope.welcomeSection() {
    item {
        Text(
            stringResource(R.string.welcome),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
        )
    }
    item {
        Text(
            stringResource(R.string.welcome_desc),
            fontSize = 14.sp,
            fontWeight = FontWeight.W500,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}


fun LazyListScope.headerSection(text: Int) {
    item {
        Text(
            stringResource(text),
            fontSize = 22.sp,
            fontWeight = FontWeight.W700,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}
@Preview
@Composable
fun SuccessViewPreview() {
    SuccessView(Modifier,dummyExperienceList, dummyExperienceList, null,{},{}, {}, {})
}

private fun LazyListScope.verticalScrollExperiences(
    recentExperience: List<SingleExperienceItem>?,
    likeClick: (String) -> Unit, openExperience: (String) -> Unit,
) {
    items(items = recentExperience ?: emptyList()) { item ->

        ExperienceCard(
            imageRes = item.coverPhoto.orEmpty(),
            title = item.title.orEmpty(),
            views = item.viewsNo ?: 0,
            likes = item.likesNo ?: 0,
            recommended = true,
            likeClick = {
                // viewModel.likeExperience(item.id.orEmpty())
                likeClick.invoke(item.id.orEmpty())
            }) {
            openExperience.invoke(item.id.orEmpty())
        }

    }
}

private fun LazyListScope.horizontalScrollExperiences(
    recommendedExperience: List<SingleExperienceItem>?,
    likeClick: (String) -> Unit, openExperience: (String) -> Unit,
) {
    item {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            // contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(items = recommendedExperience ?: emptyList()) { item ->
                Box(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .height(200.dp)
                        .background(Color.Red)
                ) {
                    ExperienceCard(
//                        imageRes = R.drawable.ic_la,
                        imageRes = item.coverPhoto.orEmpty(),
                        title = item.title.orEmpty(),
                        views = item.viewsNo ?: 0,
                        likes = item.likesNo ?: 0,
                        recommended = item.recommended == 1,
                        likeClick = {
                            //  viewModel.likeExperience(item.id.orEmpty())
                            likeClick.invoke(item.id.orEmpty())
                        }) {
                        openExperience.invoke(item.id.orEmpty())
                    }
                }

            }
        }
    }
}


fun LazyListScope.SearchBar(
    onCancelSearch: () -> Unit,
    onValueChange: (String) -> Unit,

    ) {
    item {
        val searchText = remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Menu Icon
            Icon(
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = "Menu",
                modifier = Modifier
                    .size(28.dp),
                tint = Color.Black
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Search Field
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .background(
                        color = Color(0xFFF2F2F2),
                        shape = RoundedCornerShape(25)
                    ),
                //.padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {

                TextField(
                    value = searchText.value,
                    onValueChange = {
                        searchText.value = it
                        if (it.isNotEmpty()) {
                            onValueChange(it)
                        }else onCancelSearch()
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W400,

                        ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 56.dp)
                        .scale(scaleY = 0.9F, scaleX = 1F)
                    /*.height(45.dp)*/,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.try_luxor),
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(0.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrectEnabled = true,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search,
                    ),
                    shape = RoundedCornerShape(10.dp),
                    leadingIcon = {
                        Icon(
                            painterResource(id = android.R.drawable.ic_menu_search),
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchText.value.isNotEmpty()) {
                            Image(
                                painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                                contentDescription = null,
                                Modifier.clickable {
                                    onCancelSearch()
                                    searchText.value = ""
                                }
                            )
                        }
                    },
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            // Do your search here

                            onValueChange(searchText.value)

                            // Hide Keyboard
                            keyboardController?.hide()
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF2F2F2),
                        unfocusedContainerColor = Color(0xFFF2F2F2),
                        disabledContainerColor = Color(0xFFF2F2F2),
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color(0xFF6200EE)
                    )
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Filter icon
            Image(
                painter = painterResource(id = R.drawable.ic_filter),
                contentDescription = "Filter",
                modifier = Modifier
                    .size(26.dp)

            )
        }
    }
}



@Composable
fun ExperienceCard(
    imageRes: String,
    title: String,
    views: Int,
    likes: Int,
    recommended: Boolean = false,
    likeClick: () -> Unit = {},
    onClick: () -> Unit = {},
) {

    var likeIcon = remember { mutableStateOf(R.drawable.ic_like )}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(20 / 9f)
                .fillMaxWidth()
        ) {
            AppAsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(10.dp)),
                model = imageRes,
                contentScale = ContentScale.Crop,
            )

            // Recommended label
            if (recommended) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(
                            color = colorResource(R.color.black).copy(alpha = 0.46f),
                            shape = RoundedCornerShape(50.dp)
                        )
                        .padding(horizontal = 8.dp)
                        .align(Alignment.TopStart), verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painterResource(R.drawable.ic_favorite),
                        contentDescription = "Favorite",
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "RECOMMENDED",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Info icon (top end)
            Image(
                painter = painterResource(R.drawable.ic_info),
                contentDescription = "Info",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            )

            // 360 Icon center
            Image(
                painter = painterResource(R.drawable.ic_360),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(37.dp),

                )

            // Views & photo icon (bottom start)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_view),
                    contentDescription = "Views",

                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = views.toString(),
                    color = Color.White,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            // Image icon (bottom end)
            Image(
                painter = painterResource(R.drawable.ic_gallery),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .width(20.dp)
                    .height(16.dp)
                    .padding(8.dp)
            )
        }

        // Title and likes row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.Black
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = likes.toString(),
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
                Image(
                    painter = painterResource(likeIcon.value),
                    contentDescription = "Like",
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable {
                            likeIcon.value = R.drawable.ic_like_filled
                            likeClick()
                        }
                )
            }
        }
    }
}

val dummyExperienceList = listOf(
    SingleExperienceItem(
        coverPhoto = "https://fls-9ff553c9-95cd-4102-b359-74ad35cdc461.367be3a2035528943240074d0096e0cd.r2.cloudflarestorage.com\\/29\\/PmA89sBqFNkjDZUVOOaQ8PyEtlIXi7-metaSzNBVFFsaFU4VHhMVmkxZ253emdVNlczTEJRS3BuM1paWDg0MHIzci5qcGVn-.jpg?X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=c1450316125e7da31ad41a64b276dcc3%2F20251113%2Fauto%2Fs3%2Faws4_request&X-Amz-Date=20251113T221036Z&X-Amz-SignedHeaders=host&X-Amz-Expires=172800&X-Amz-Signature=e5a577fd6c1e3e1703032f9583c3fc0d34b7cf62694baed9dba3710443e948e1",
        hasAudio = true,
        city = City(
            name = "Cairo"
        ),

        famousFigure = "Tutankhamun",
        rating = 5,
        description = "A magical journey through the Egyptian Museum.",
        title = "Egyptian Museum Tour",
        hasVideo = 1,
        experienceTips = listOf("Bring water", "Wear comfortable shoes"),


        startingPrice = 250,
        id = "exp_001",
        isLiked = false,
        tourHtml = "<p>Full guided experience</p>",
        period = "2 hours",
        address = "Tahrir Square, Cairo",
        viewsNo = 1023,
        founded = "1902",
        likesNo = 56,
        reviewsNo = 12,
        recommended = 1,

        audioUrl = "https://example.com/audio/guide1.mp3"
    ),

    // Second dummy item
    SingleExperienceItem(
        coverPhoto = "https://picsum.photos/600/400?2",
        hasAudio = false,
        city = City(
            name = "Luxor"
        ),

        famousFigure = "Ramses II",
        rating = 4,
        description = "Explore the Valley of the Kings.",
        title = "Valley of the Kings Tour",
        hasVideo = 0,
        experienceTips = listOf("Go early in the morning"),
        reviews = emptyList(),

        startingPrice = 350,
        id = "exp_002",
        tourHtml = "<p>Visit tombs of famous Pharaohs</p>",
        period = "3 hours",
        address = "Luxor West Bank",
        viewsNo = 2300,
        founded = "1500 BC",
        likesNo = 87,
        reviewsNo = 33,
        recommended = 1,

        detailedDescription = "Walk inside ancient tombs of Egyptian kings.",

        audioUrl = null
    )
)

