package com.dicoding.basuwara.ui.components.home

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

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