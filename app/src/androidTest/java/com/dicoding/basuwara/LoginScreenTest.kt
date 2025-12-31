package com.dicoding.basuwara

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.dicoding.basuwara.ui.Login
import com.dicoding.basuwara.ui.navigation.Screen
import com.dicoding.basuwara.ui.theme.Material3ComposeTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    private fun navigateToLoginScreen() {
        // Tunggu sampai tombol login muncul di screen ketiga
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithTag("login_button").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithTag("login_button")
            .assertIsDisplayed()
            .performClick()
    }

    @Test
    fun loginScreen_isDisplayed() {
        navigateToLoginScreen()
        composeTestRule.onNodeWithTag("email").assertIsDisplayed()
        composeTestRule.onNodeWithTag("password").assertIsDisplayed()
        composeTestRule.onNodeWithText("Login").assertIsDisplayed()
        composeTestRule.onNodeWithText("Create An Account").assertIsDisplayed()
    }

    @Test
    fun showError_whenEmailAndPasswordEmpty() {
        navigateToLoginScreen()
        composeTestRule.onNodeWithText("Login").performClick()
        composeTestRule.onNodeWithText("Error: Empty Input").assertIsDisplayed()
    }

    @Test
    fun emailValidation_showsErrorOnInvalidEmail() {
        navigateToLoginScreen()
        composeTestRule.onNodeWithText("Name or Email Address").performTextInput("invalidemail")
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Invalid Email").assertIsDisplayed()
    }

    @Test
    fun showError_whenPasswordIsEmpty() {
        navigateToLoginScreen()
        composeTestRule.onNodeWithTag("email").performTextInput("test@example.com")
        composeTestRule.onNodeWithText("Login").performClick()
        composeTestRule.onNodeWithText("Error: Empty Input").assertIsDisplayed()
    }

    @Test
    fun login_success_navigatesToHome() {
        navigateToLoginScreen()
        composeTestRule.onNodeWithTag("email").performTextInput("test@abc.com")
        composeTestRule.onNodeWithTag("password").performTextInput("abc123")
        composeTestRule.onNodeWithText("Login").performClick()
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule
                .onAllNodesWithText("Hello, Test!")
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
        composeTestRule.onNodeWithText("Hello, Test!").assertIsDisplayed()
    }

    @Test
    fun login_fail_accountNotRegistered() {
        navigateToLoginScreen()
        composeTestRule.onNodeWithTag("email").performTextInput("unregistered@example.com")
        composeTestRule.onNodeWithTag("password").performTextInput("password123")
        composeTestRule.onNodeWithText("Login").performClick()
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("The supplied auth credential is incorrect, malformed or has expired.").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("The supplied auth credential is incorrect, malformed or has expired.").assertIsDisplayed()
    }


    @Test
    fun createAccountButton_navigatesToRegister() {
        navigateToLoginScreen()
        composeTestRule.onNodeWithText("Create An Account").assertIsDisplayed()
        composeTestRule.onNodeWithText("Create An Account").performClick()
        composeTestRule.onNodeWithTag("register_name").assertExists()
        composeTestRule.onNodeWithTag("register_phone").assertExists()
        composeTestRule.onNodeWithTag("register_email").assertExists()
        composeTestRule.onNodeWithTag("register_password").assertExists()
        composeTestRule.onNodeWithTag("register_confirm_password").assertExists()
        composeTestRule.onNodeWithTag("register_button").assertExists()
    }
}