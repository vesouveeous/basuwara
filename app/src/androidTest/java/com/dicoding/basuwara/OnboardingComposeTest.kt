package com.dicoding.basuwara

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class OnboardingComposeTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun pressLoginButtonOnThirdScreen() {
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule.onAllNodesWithTag("login_button").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("login_button")
            .assertIsDisplayed()
            .performClick()
        composeTestRule.onNodeWithText("Name or Email Address")
            .performTextInput("rere@gmail.com")
        composeTestRule.onNodeWithText("Enter Password")
            .performTextInput("12345678")
        runBlocking {
            delay(2000)
        }
    }
}