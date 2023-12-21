package com.dicoding.basuwara.ui.screen.quiz

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.ImagePainter
import com.dicoding.basuwara.R

@Composable
fun QuizResultScreen(
    score: Int,
    onDoneClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(getBackgroundColor(score))
    ) {
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Your final score is\n$score / 100",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displayMedium
                )
                Text(
                    text = getScoreMessage(score),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = { onDoneClick() },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .border(2.dp, color = Color.White)
                        .background(color = MaterialTheme.colorScheme.primary)

                ) {
                    Text(
                        text = "Done",
                    )
                }
            }
        }
    }
}

fun getBackgroundColor(score: Int): Color {
    return when {
        score >= 70 -> Color.Green
        score in 41..69 -> Color.Yellow
        else -> Color.Red
    }
}

fun getScoreMessage(score: Int): String {
    return when {
        score >= 70 -> "Congratulations!\nYou did an excellent job!"
        score in 41..69 -> "Good effort!\nYou're on the right track."
        else -> "Keep trying!\nYou can do better next time."
    }
}
@DrawableRes
fun getImage(score: Int): Int {
    return when {
        score >= 70 -> R.drawable.ic_launcher_background
        score in 41..69 -> R.drawable.ic_launcher_background
        else -> R.drawable.anxiety
    }
}