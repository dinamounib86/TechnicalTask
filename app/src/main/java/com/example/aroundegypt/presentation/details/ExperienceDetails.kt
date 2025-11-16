package com.example.aroundegypt.presentation.details

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.aroundegypt.Utils.composables.AppAsyncImage
import com.example.aroundegypt.domain.models.SingleExperienceItem
import com.example.aroundegypt.R
import com.example.aroundegypt.presentation.home.ErrorView


@SuppressLint("ContextCastToActivity")
@Composable
fun ExperienceDetails(id: String, viewModel: DetailsViewModel = hiltViewModel()) {
    viewModel.id.value = id
  val detailState =   viewModel.detailsState.collectAsStateWithLifecycle()

    when (val uiState = detailState.value) {
        is DetailsViewModel.DetailsState.Error -> ErrorView(uiState.msg) { }
        DetailsViewModel.DetailsState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is DetailsViewModel.DetailsState.Success -> ExperienceDetails(uiState.singleDetails){
            viewModel.likeExperience(id)
        }
    }


}

@Composable
fun ExperienceDetails(experience: SingleExperienceItem,onLikeClicked:()->Unit){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {

        ImageSection(experience)

        Spacer(modifier = Modifier.height(12.dp))

        // === Title & Subtitle ===
        TitleAndShareSection(experience,onLikeClicked)


        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider(
            color = Color(0xFFE0E0E0), thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        DescriptionSection(experience.description)

        Spacer(modifier = Modifier.height(40.dp))
    }
}
@Composable
fun TitleAndShareSection(experience: SingleExperienceItem?,onLikeClicked:()->Unit) {
    val likeIcon = remember { mutableStateOf(R.drawable.ic_like )}
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                experience?.title.orEmpty(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                experience?.city?.name.orEmpty(),

                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.grey),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = "",
                tint = Color(0xFFE9762C),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                painter = painterResource(likeIcon.value),
                contentDescription = "",
                tint = Color(0xFFE9762C),
                modifier = Modifier.size(20.dp).clickable{
                    likeIcon.value = R.drawable.ic_like_filled
                    onLikeClicked()
                }
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(experience?.likesNo.toString(), color = Color(0xFFE9762C))
        }
    }
}

@Composable
fun BoxScope.StatsRow(experience: SingleExperienceItem?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_view),
                contentDescription = "",

                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                "${experience?.viewsNo.toString()} views", color = Color.White,
                fontSize = 14.sp, fontWeight = FontWeight.W500
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_gallery),
                contentDescription = "",
                modifier = Modifier.size(20.dp)
            )

        }
    }
}

@Composable
fun DescriptionSection(description: String?) {
    Text(
        "Description",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(horizontal = 16.dp)
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = description.orEmpty(),
        fontSize = 14.sp,
        fontWeight = FontWeight.W500,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        color = Color.Black
    )

}

@Composable
fun ImageSection(experience: SingleExperienceItem?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        AppAsyncImage(
            model = experience?.coverPhoto,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Explore Now Button
        Button(
            onClick = { },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Text(
                "EXPLORE NOW",
                color = Color(0xFFE18758),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        StatsRow(experience)
    }
}

