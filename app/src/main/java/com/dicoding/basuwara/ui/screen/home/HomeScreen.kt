package com.dicoding.basuwara.ui.screen.home
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.dicoding.basuwara.R.*
import com.dicoding.basuwara.ui.components.AnimatedCircularProgressIndicator

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    dummyClick: () -> Unit,
    goToOnboardingPage: () -> Unit,
    onCourseChosen: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(viewModel.idState) {
        viewModel.getUserId()

        viewModel.idState.collect {id ->
            Log.d("HomeScreen", "User ID: $id")
            if (id == "empty") {
                Log.d("HomeScreen", "Redirecting to Onboarding Page")
                goToOnboardingPage()
            } else if (id.isNotEmpty()) {
            viewModel.getUserInfo(id)
            }
        }
    }

    viewModel.homeState.collectAsState(initial = null).value.let {
        when(it) {
            HomeState(isLoading = true) -> {
                CircularProgressIndicator()
            }
            HomeState(isLoading = false, isSuccess = it?.isSuccess) -> {
                Column {
                    HomeTopBar(
                        name = it.isSuccess!!.name,
                        onNotificationClick = {
                            viewModel.logout()
                            dummyClick()
                        }
                    )
                    HomeBody( onCourseChosen = { onCourseChosen(it) })
                }
            }
        }
    }
}

@Composable
fun HomeTopBar(
    name: String,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val imageUrl = "https://example.com/image.jpg"

    val painter =
        rememberAsyncImagePainter(ImageRequest.Builder
            (LocalContext.current).data(data = imageUrl).apply(block = fun ImageRequest.Builder.() {
            crossfade(true)
            placeholder(drawable.ic_placeholder)
        }).build()
        )
    Box(modifier = modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.primary)){
        Icon(
            painter = painterResource(id = drawable.ic_logout),
            contentDescription = "Notification",
            modifier = Modifier
                .padding(top = 32.dp, end = 24.dp)
                .size(35.dp)
                .align(Alignment.TopEnd)
                .clickable { onNotificationClick() }
        )
        Column {
            Image(
                painter = if (painter.state is AsyncImagePainter.State.Success) painter else painterResource(id = drawable.ic_placeholder),
                contentDescription = "Profile picture",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(100.dp)
                    .padding(16.dp)
            )
            Spacer(modifier = modifier
                .fillMaxWidth()
                .height(8.dp))
            Text(
                text = "Hello, $name!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = modifier.padding(start = 16.dp)
            )
            Text(
                text = "What would you want to learn today?",
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = modifier.padding(start = 16.dp, bottom = 16.dp)
            )
        }

    }

}

@Composable
fun HomeBody(
    onCourseChosen: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Continue Courses",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(start = 16.dp, top = 16.dp)
        )
        Row(
            modifier = modifier
                .padding(8.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            ProgressCard(
                title = "Belajar Aksara jawa",
                currentValue = 10,
                maxValue = 25,
                progressBackgroundColor = Color.LightGray,
                progressIndicatorColor = Color.Gray,
                completedColor = Color.Green,
                isClickable = true,
                onClick = { onCourseChosen("jawa") }
            )
            ProgressCard(
                title = "Belajar Aksara Sunda",
                currentValue = 15,
                maxValue = 25,
                progressBackgroundColor = Color.LightGray,
                progressIndicatorColor = Color.Gray,
                completedColor = Color.Green,
                isClickable = false,
                onClick = { onCourseChosen("sunda") }
            )
            ProgressCard(
                title = "Belajar Aksara Bali",
                currentValue = 20,
                maxValue = 25,
                progressBackgroundColor = Color.LightGray,
                progressIndicatorColor = Color.Gray,
                completedColor = Color.Green,
                isClickable = false,
                onClick = { onCourseChosen("bali") }
            )
        }
        Text(
            text = "Learn Courses",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = modifier.padding(start = 16.dp, top = 16.dp)
        )
        Row(
            modifier = modifier
                .padding(8.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            CourseCard(
                title = "Belajar Aksara Jawa",
                id = drawable.aksara_jawa,
                isClickable = true,
                onClick = { onCourseChosen("jawa") }
            )
            CourseCard(
                title = "Belajar Aksara Sunda",
                id = drawable.aksara_sunda,
                isClickable = false,
                onClick = { onCourseChosen("sunda") }
            )
            CourseCard(
                title = "Belajar Aksara Bali",
                id = drawable.aksara_bali,
                isClickable = false,
                onClick = { onCourseChosen("bali") }
            )
        }
    }
}

@Composable
fun ProgressCard(
    title: String,
    currentValue: Int,
    maxValue: Int,
    progressBackgroundColor: Color,
    progressIndicatorColor: Color,
    completedColor: Color,
    isClickable: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .padding(8.dp)
            .height(180.dp)
            .width(100.dp)
            .clickable {
                if (isClickable) {
                    onClick()
                } else {
                    Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show()
                }
            }
    ) {
        AnimatedCircularProgressIndicator(
            currentValue = currentValue,
            maxValue = maxValue,
            progressBackgroundColor = progressBackgroundColor,
            progressIndicatorColor = progressIndicatorColor,
            completedColor = completedColor,
            modifier = modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = modifier.fillMaxWidth()
        )
        Text(
            text = "$maxValue materi",
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Start,
            modifier = modifier.padding(start = 8.dp, top = 12.dp)
        )
    }
}

@Composable
fun CourseCard(
    title: String,
    @DrawableRes id: Int,
    isClickable: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(
        modifier = modifier
            .width(180.dp)
            .height(100.dp)
            .clickable {
                if (isClickable) {
                    onClick()
                } else {
                    Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show()
                }
            }
            .padding(8.dp)
    ) {
        Row {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = modifier
                    .padding(8.dp)
                    .width(80.dp)
                    .align(Alignment.CenterVertically)
            )
            Image(
                painter = painterResource(id = id),
                contentDescription = "",
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxSize()
            )
        }
    }
}