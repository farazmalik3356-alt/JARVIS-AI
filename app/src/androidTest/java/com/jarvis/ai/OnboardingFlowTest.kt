package com.jarvis.ai

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class OnboardingFlowTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun onboarding_isVisibleOnFirstLaunch() {
        composeRule.onNodeWithText("JARVIS AI").assertIsDisplayed()
        composeRule.onNodeWithText("Continue").assertIsDisplayed()
        composeRule.onNodeWithText("Continue").performClick()
        composeRule.onNodeWithText("Gemini API Key").assertIsDisplayed()
    }
}
