package com.dicoding.basuwara.ui.screen.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.basuwara.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseQuizScreen(
    onQuizSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Card(
            shape = CardDefaults.elevatedShape,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            onClick = { onQuizSelected("jawa") },
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Box(modifier = modifier.fillMaxWidth()) {
                Text(
                    text = "Aksara Jawa",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.aksara_jawa),
                    contentDescription = "",
                    modifier = modifier
                        .align(Alignment.CenterEnd)
                        .padding(16.dp)
                )
            }
        }
        Spacer(modifier = modifier.height(8.dp))
        Card(
            shape = CardDefaults.elevatedShape,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            enabled = false,
            onClick = { onQuizSelected("bali") },
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Box(modifier = modifier.fillMaxWidth()) {
                Text(
                    text = "*Not available for now",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = modifier.align(Alignment.TopStart).padding(16.dp)
                )
                Text(
                    text = "Aksara Bali",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.aksara_bali),
                    contentDescription = "",
                    modifier = modifier
                        .align(Alignment.CenterEnd)
                        .padding(16.dp)
                )
            }
        }
        Spacer(modifier = modifier.height(8.dp))
        Card(
            shape = CardDefaults.elevatedShape,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            enabled = false,
            onClick = { onQuizSelected("sunda") },
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(150.dp)
        ) {
            Box(modifier = modifier.fillMaxWidth()) {
                Text(
                    text = "*Not available for now",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = modifier.align(Alignment.TopStart).padding(16.dp)
                )
                Text(
                    text = "Aksara Sunda",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.aksara_sunda),
                    contentDescription = "",
                    modifier = modifier
                        .align(Alignment.CenterEnd)
                        .padding(16.dp)
                )
            }

        }
    }

}

@Preview
@Composable
fun previewQuizScreen() {
    ChooseQuizScreen(onQuizSelected = {})
}