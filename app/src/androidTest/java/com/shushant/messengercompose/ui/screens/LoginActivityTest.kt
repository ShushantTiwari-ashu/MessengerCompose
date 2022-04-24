package com.shushant.messengercompose.ui.screens

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shushant.messengercompose.persistence.SharedPrefs
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest{

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    val loginViewModel :LoginViewModel = mockk(relaxed = true)

    @Before
    fun setup() {
        hiltRule.inject()
        SharedPrefs.initSharedPrefs(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testViewModel(){
        composeTestRule.activityRule.scenario.onActivity {}
        composeTestRule.onNodeWithText("bhakljhdkja")
    }

}