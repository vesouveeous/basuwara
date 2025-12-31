package com.dicoding.basuwara

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ChooseQuizScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    fun navigateToChooseQuizScreen() {
        composeTestRule.waitUntil(3000) {
            composeTestRule.onAllNodesWithTag("progress_jawa").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Quiz").performClick()
    }

    @Test
    fun chooseQuizScreen_displaysAllQuizCards() {
        navigateToChooseQuizScreen()
        composeTestRule.onNodeWithText("Aksara Jawa").assertIsDisplayed()
        composeTestRule.onNodeWithText("Aksara Bali").assertIsDisplayed()
        composeTestRule.onNodeWithText("Aksara Sunda").assertIsDisplayed()
    }

    @Test
    fun aksaraJawaCard_isClickable() {
        navigateToChooseQuizScreen()
        composeTestRule.onNodeWithText("Aksara Jawa").performClick()
    }

    @Test
    fun aksaraBaliCard_isDisabled() {
        navigateToChooseQuizScreen()
        composeTestRule.onNodeWithText("Aksara Bali").assertIsNotEnabled()
    }

    @Test
    fun aksaraSundaCard_isDisabled() {
        navigateToChooseQuizScreen()
        composeTestRule.onNodeWithText("Aksara Sunda").assertIsNotEnabled()
    }
}

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class QuizScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    fun navigateToQuizScreen() {
        composeTestRule.waitUntil(3000) {
            composeTestRule.onAllNodesWithTag("progress_jawa").fetchSemanticsNodes().isNotEmpty()
        }
        composeTestRule.onNodeWithText("Quiz").performClick()
        composeTestRule.onNodeWithText("Aksara Jawa").performClick()
    }

    fun answerA() {
        composeTestRule.onAllNodes(isSelectable())[0].performClick()
    }
    fun answerB() {
        composeTestRule.onAllNodes(isSelectable())[1].performClick()
    }
    fun answerC() {
        composeTestRule.onAllNodes(isSelectable())[2].performClick()
    }
    fun answerD() {
        composeTestRule.onAllNodes(isSelectable())[3].performClick()
    }

    @Test
    fun quizQuestion_displaysQuestionAndOptions() {
        navigateToQuizScreen()
        composeTestRule.onNodeWithText("Gorengane renane akeh").assertExists()
        composeTestRule.onAllNodes(isSelectable()).assertCountEquals(4) // assuming 4 options
    }

    @Test
    fun quizNavigation_nextAndPreviousButtonsWork() {
        navigateToQuizScreen()
        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.onNodeWithText("Previous").performClick()
    }

    @Test
    fun quizFinish_displaysResultScreen() {
        navigateToQuizScreen()
        repeat(9) {
            composeTestRule.onAllNodes(isSelectable())[0].performClick()
            composeTestRule.onNodeWithText("Next").performClick()
        }
        composeTestRule.onAllNodes(isSelectable())[0].performClick()
        composeTestRule.onNodeWithText("Finish").performClick()
        composeTestRule.onNodeWithText("Your final score is", substring = true).assertExists()
    }

    @Test
    fun resultBackgroundColor_changesWithScore() {
        navigateToQuizScreen()
        answerA()
        composeTestRule.onNodeWithText("Next").performClick()
        repeat(4) {
            answerB()
            composeTestRule.onNodeWithText("Next").performClick()
        }
        answerD()
        composeTestRule.onNodeWithText("Next").performClick()
        answerC()
        composeTestRule.onNodeWithText("Next").performClick()
        answerA()
        composeTestRule.onNodeWithText("Next").performClick()
        answerA()
        composeTestRule.onNodeWithText("Next").performClick()
        answerD()
        composeTestRule.onNodeWithText("Finish").performClick()
        composeTestRule.onNodeWithText("Keep trying!\nYou can do better next time.").assertExists()
    }
}
