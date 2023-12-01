@file:OptIn(ExperimentalPagerApi::class, ExperimentalPagerApi::class)

package com.dicoding.basuwara.ui.screen.onboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.capstone.basuwara.ui.onboard.PageUI
import com.dicoding.basuwara.R
import com.dicoding.basuwara.ui.screen.onboard.onboard
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay


@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun Onboarding(
    onGettingStartedClick: () -> Unit,
    onSkipClicked: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = 3)
    var shouldAutoScroll by remember { mutableStateOf(true) }

    // Fungsi untuk menggeser halaman secara otomatis
    LaunchedEffect(pagerState.currentPage) {
        while (shouldAutoScroll) {
            delay(3000) // Ganti nilai ini sesuai dengan waktu yang diinginkan (dalam milidetik)
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % pagerState.pageCount)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White) // Sesuaikan warna latar belakang sesuai kebutuhan
    ) {
        // Tombol Skip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Skip",
                modifier = Modifier
                    .clickable {
                        shouldAutoScroll = false // Memberhentikan perulangan
                        onSkipClicked()
                    }
            )
        }

        // HorizontalPager dan lain-lain
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            PageUI(onboard = onboard[page])
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            activeColor = colorResource(R.color.purple_500)
        )

        AnimatedVisibility(visible = pagerState.currentPage == 2) {
            OutlinedButton(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                onClick = {
                    shouldAutoScroll = false // Memberhentikan perulangan
                    onGettingStartedClick()
                },
                colors = ButtonDefaults.outlinedButtonColors(
                    colorResource(id = R.color.purple_500),
                    contentColor = Color.White
                )
            ) {
                Text(text = stringResource(R.string.get_started))
            }
        }
    }
}



@Composable
fun HorizontalPagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    activeColor: Color = Color.Gray,
    inactiveColor: Color = Color.LightGray
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(pagerState.pageCount) { pageIndex ->
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(
                        color = if (pageIndex == pagerState.currentPage) activeColor else inactiveColor,

                    )
            )
        }
    }
}


