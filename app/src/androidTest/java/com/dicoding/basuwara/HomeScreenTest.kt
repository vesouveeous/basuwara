package com.dicoding.basuwara

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    fun waitUntilScreenReady() {
        composeTestRule.waitUntil(3000) {
            composeTestRule.onAllNodesWithTag("progress_jawa").fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun userNameAndGreeting_areDisplayed() {
        waitUntilScreenReady()
        composeTestRule.onNodeWithText("Hello,", substring = true).assertExists()
        composeTestRule.onNodeWithText("What would you want to learn today?").assertIsDisplayed()
    }

    @Test
    fun courseCardJawa_isDisplayedAndClickable() {
        waitUntilScreenReady()
        composeTestRule.onNodeWithTag("course_jawa").assertIsDisplayed().performClick()
    }

    @Test
    fun courseCardSunda_isDisplayedAndClickable() {
        waitUntilScreenReady()
        composeTestRule.onNodeWithTag("course_sunda").assertIsDisplayed().performClick()
    }

    @Test
    fun courseCardBali_isDisplayedAndClickable() {
        waitUntilScreenReady()
        composeTestRule.onNodeWithTag("course_bali").assertIsDisplayed().performClick()
    }

    @Test
    fun progressCardJawa_isDisplayedAndClickable() {
        waitUntilScreenReady()
        composeTestRule.onNodeWithTag("progress_jawa").assertIsDisplayed().performClick()
    }

    @Test
    fun progressCardSunda_isDisplayedAndClickable() {
        waitUntilScreenReady()
        composeTestRule.onNodeWithTag("progress_sunda").assertIsDisplayed().performClick()
    }

    @Test
    fun progressCardBali_isDisplayedAndClickable() {
        waitUntilScreenReady()
        composeTestRule.onNodeWithTag("progress_bali").assertIsDisplayed().performClick()
    }

    @Test
    fun navigatedToCameraScreen() {
        waitUntilScreenReady()
        composeTestRule.onNodeWithTag("fab").performClick()
        composeTestRule.onNodeWithText("Search").assertExists()
    }

    @Test
    fun clickBottomBar_staysInHomeScreen() {
        waitUntilScreenReady()
        composeTestRule.onNodeWithText("Home").performClick()
        composeTestRule.onNodeWithText("Hello,", substring = true).assertExists()
    }

    @Test
    fun navigatedToQuizScreen() {
        waitUntilScreenReady()
        composeTestRule.onNodeWithText("Quiz").performClick()
        composeTestRule.onNodeWithText("Aksara Jawa").assertExists()
    }

    // TC9: Cek tampilan teks section "Continue Courses" dan "Learn Courses"
    @Test
    fun sectionTitles_areDisplayed() {
        waitUntilScreenReady()
        composeTestRule.onNodeWithText("Continue Courses").assertIsDisplayed()
        composeTestRule.onNodeWithText("Learn Courses").assertIsDisplayed()
    }

}
