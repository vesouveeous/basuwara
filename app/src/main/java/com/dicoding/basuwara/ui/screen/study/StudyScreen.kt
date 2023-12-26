package com.dicoding.basuwara.ui.screen.study

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.dicoding.basuwara.R

@Composable
fun StudyScreen(
    courseType: String
) {
    val scrollState = rememberScrollState()
    if (courseType == "jawa"){
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
        ) {
            Image(
                painter = painterResource(id = R.drawable.jawa_course),
                contentDescription = "materi aksara jawa",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
            )
            Image(
                painter = painterResource(id = R.drawable.jawa_course_2),
                contentDescription = "materi aksara jawa",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth().fillMaxHeight()
            )
        }
    }
}