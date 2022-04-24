package com.shushant.messengercompose.ui

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shushant.messengercompose.R
import com.shushant.messengercompose.ui.screens.LaunchScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class BlankFragmentTest {

    val hiltRule = HiltAndroidRule(this)

    val rules = ActivityScenarioRule(LaunchScreen::class.java)

    val composeRule = createComposeRule()

    @get:Rule
    val ruleArround: RuleChain = RuleChain.outerRule(hiltRule).around(rules).around(composeRule)

    @Before
    fun setup() {
        hiltRule.inject()
    }

}