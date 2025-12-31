package com.dicoding.basuwara.ui.components.home

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dicoding.basuwara.ui.components.AnimatedCircularProgressIndicator

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

