package com.dicoding.basuwara.ui.screen.home
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.dicoding.basuwara.R
import com.dicoding.basuwara.R.*
import com.dicoding.basuwara.ui.components.AnimatedCircularProgressIndicator

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Column {
        HomeTopBar()
        HomeBody()
    }
}

@Composable
fun HomeTopBar(
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
            painter = painterResource(id = drawable.baseline_notifications_none_24),
            contentDescription = "Notification",
            modifier = Modifier
                .padding(top = 32.dp, end = 24.dp)
                .size(35.dp)
                .align(Alignment.TopEnd)
                .clickable {  }
        )
        Column {
            Image(
                painter = painter,
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
                text = "Hello, xxxxx!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(start = 16.dp)
            )
            Text(
                text = "What would you want to learn today?",
                modifier = modifier.padding(start = 16.dp, bottom = 16.dp)
            )
        }

    }

}

@Composable
fun HomeBody(
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
                onClick = {  }
            )
            ProgressCard(
                title = "Belajar Aksara Sunda",
                currentValue = 15,
                maxValue = 25,
                progressBackgroundColor = Color.LightGray,
                progressIndicatorColor = Color.Gray,
                completedColor = Color.Green,
                onClick = {  }
            )
            ProgressCard(
                title = "Belajar Aksara Bali",
                currentValue = 20,
                maxValue = 25,
                progressBackgroundColor = Color.LightGray,
                progressIndicatorColor = Color.Gray,
                completedColor = Color.Green,
                onClick = {  }
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
                onClick = {  }
            )
            CourseCard(
                title = "Belajar Aksara Sunda",
                id = drawable.aksara_sunda,
                onClick = {  }
            )
            CourseCard(
                title = "Belajar Aksara Bali",
                id = drawable.aksara_bali,
                onClick = {  }
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .padding(8.dp)
            .height(180.dp)
            .width(100.dp)
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(180.dp)
            .height(100.dp)
            .clickable { onClick() }
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

@Preview(showBackground = true, device = Devices.DEFAULT, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen()
    }
}