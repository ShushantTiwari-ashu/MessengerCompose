package com.shushant.messengercompose.ui.screens

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shushant.messengercompose.persistence.SharedPrefs
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LaunchScreenTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        hiltRule.inject()
        SharedPrefs.initSharedPrefs(ApplicationProvider.getApplicationContext())
    }
}

/*    @OptIn(ExperimentalMaterialApi::class)
    @Test
    fun testImageRendered(){
        composeTestRule.setContent {
            Text("hlkhal ahfl hwfla fhl hlahflah halhflahfla hlafhla hl afhlafh ahlf hlaf ahfa lahf la a", overflow = TextOverflow.Ellipsis, maxLines = 1)
        }
composeTestRule.onNodeWithText("hlkhal ahfl hwfla fhl hlahflah halhflahfla hlafhla hl afhlafh ahlf hlaf ahfa lahf la a").checkHasEllipseornot()

    }
}

private fun SemanticsNodeInteraction.checkHasEllipseornot(overflow: TextOverflow = TextOverflow.Ellipsis): SemanticsMatcher {
    return SemanticsMatcher(description = "") { this }

}*/
