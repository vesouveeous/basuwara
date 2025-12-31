package com.dicoding.basuwara

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltAndroidRule
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RegisterScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    private fun goToRegisterScreen() {
        navigateToLoginScreen()
        composeTestRule.onNodeWithText("Create An Account").performClick()
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
    fun navigateToRegisterScreen() {
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

    @Test
    fun emptyInput() {
        goToRegisterScreen()
        composeTestRule.onNodeWithTag("register_button").performClick()
        composeTestRule.onNodeWithText("Please fill all the fields").assertExists()
    }

    @Test
    fun invalidEmailFormat() {
        goToRegisterScreen()
        composeTestRule.onNodeWithTag("register_name").performTextInput("abc")
        composeTestRule.onNodeWithTag("register_phone").performTextInput("081234567890")
        composeTestRule.onNodeWithTag("register_email").performTextInput("abc")
        composeTestRule.onNodeWithTag("register_password").performTextInput("abc123")
        composeTestRule.onNodeWithTag("register_confirm_password").performTextInput("abc123")
        composeTestRule.onNodeWithTag("register_button").performClick()
        composeTestRule.onNodeWithText("Email is not valid").assertExists()
    }

    @Test
    fun passwordLessThan6Chars() {
        goToRegisterScreen()
        composeTestRule.onNodeWithTag("register_name").performTextInput("abc")
        composeTestRule.onNodeWithTag("register_phone").performTextInput("081234567890")
        composeTestRule.onNodeWithTag("register_email").performTextInput("abc@test.com")
        composeTestRule.onNodeWithTag("register_password").performTextInput("abc")
        composeTestRule.onNodeWithTag("register_confirm_password").performTextInput("abc")
        composeTestRule.onNodeWithTag("register_button").performClick()
        composeTestRule.onNodeWithText("Password must be at least 6 characters").assertExists()
    }

    @Test
    fun passwordAndConfirmation_doNotMatch() {
        goToRegisterScreen()
        composeTestRule.onNodeWithTag("register_name").performTextInput("abc")
        composeTestRule.onNodeWithTag("register_phone").performTextInput("081234567890")
        composeTestRule.onNodeWithTag("register_email").performTextInput("abc@test.com")
        composeTestRule.onNodeWithTag("register_password").performTextInput("abc123")
        composeTestRule.onNodeWithTag("register_confirm_password").performTextInput("abc789")
        composeTestRule.onNodeWithTag("register_button").performClick()
        composeTestRule.onNodeWithText("Password and confirm password do not match").assertExists()
    }

    @Test
    fun registrationSuccess() {
        goToRegisterScreen()
        composeTestRule.onNodeWithTag("register_name").performTextInput("abc")
        composeTestRule.onNodeWithTag("register_phone").performTextInput("081234567890")
        composeTestRule.onNodeWithTag("register_email").performTextInput("abc${System.currentTimeMillis()}@test123.com")
        composeTestRule.onNodeWithTag("register_password").performTextInput("abc123")
        composeTestRule.onNodeWithTag("register_confirm_password").performTextInput("abc123")
        composeTestRule.onNodeWithTag("register_button").performClick()
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithText("Account successfully registered!")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Account successfully registered!").assertExists()
    }

    @Test
    fun emailAlreadyInUse() {
        goToRegisterScreen()
        composeTestRule.onNodeWithTag("register_name").performTextInput("abc")
        composeTestRule.onNodeWithTag("register_phone").performTextInput("081234567890")
        composeTestRule.onNodeWithTag("register_email").performTextInput("abc@test.com")
        composeTestRule.onNodeWithTag("register_password").performTextInput("abc123")
        composeTestRule.onNodeWithTag("register_confirm_password").performTextInput("abc123")
        composeTestRule.onNodeWithTag("register_button").performClick()
        composeTestRule.waitUntil(timeoutMillis = 10_000) {
            composeTestRule.onAllNodesWithText("The email address is already in use by another account.")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("The email address is already in use by another account.").assertExists()
    }

    @Test
    fun showLoadingIndicator_whenRegistering() {
        goToRegisterScreen()
        composeTestRule.onNodeWithTag("register_name").performTextInput("abc")
        composeTestRule.onNodeWithTag("register_phone").performTextInput("081234567890")
        composeTestRule.onNodeWithTag("register_email").performTextInput("abc@test.com")
        composeTestRule.onNodeWithTag("register_password").performTextInput("abc123")
        composeTestRule.onNodeWithTag("register_confirm_password").performTextInput("abc123")
        composeTestRule.onNodeWithTag("register_button").performClick()
        composeTestRule.onNodeWithTag("loading_indicator").assertExists()
    }
}

