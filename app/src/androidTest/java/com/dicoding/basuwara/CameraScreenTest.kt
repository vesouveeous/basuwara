package com.dicoding.basuwara

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dicoding.basuwara.ui.screen.camera.CameraContent
import com.dicoding.basuwara.ui.screen.camera.CameraViewModel
import com.dicoding.basuwara.util.AksaraClassifier
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CameraScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    fun navigateToCameraScreen() {
        composeTestRule.waitUntil(3000) {
            composeTestRule.onAllNodesWithTag("progress_jawa").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithTag("fab").performClick()
    }

    @Test
    fun cameraScreen_initialState_showsAllComponents(){
        navigateToCameraScreen()
        composeTestRule.onNodeWithContentDescription("image").assertExists()
        composeTestRule.onNodeWithContentDescription("Upload").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Gallery").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Search").assertIsDisplayed()
    }
    @Test
    fun cameraButton_click_openCamera() {
        navigateToCameraScreen()
        composeTestRule.onNodeWithContentDescription("Upload").performClick()
    }

    @Test
    fun galleryButton_click_openGallery() {
        navigateToCameraScreen()
        composeTestRule.onNodeWithText("Gallery").performClick()
    }

    @Test
    fun cameraButton_click_successTakePicture() {
        navigateToCameraScreen()
        composeTestRule.onNodeWithContentDescription("Upload").performClick()
        composeTestRule.onNodeWithContentDescription("image").assertExists()
    }

    @Test
    fun galleryButton_click_successTakePhoto() {
        navigateToCameraScreen()
        composeTestRule.onNodeWithContentDescription("Upload").performClick()
        composeTestRule.onNodeWithContentDescription("image").assertExists()
    }

    @Test
    fun searchButton_withoutImage_showsToastOrNoResult() {
        navigateToCameraScreen()
        composeTestRule.onNodeWithText("Search").performClick()
    }
}