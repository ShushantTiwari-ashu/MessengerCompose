package com.shushant.chatties.ui.screens

import android.net.Uri
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.gson.Gson
import com.shushant.chatties.ui.screens.chat.ChatScreen
import com.shushant.chatties.ui.theme.MessengerComposeTheme

@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun HomePageScreen(
    navController: NavController,
    viewModel: MainViewModel, logout: (Boolean) -> Unit
) {
    val selectedTab by viewModel.selectedTab
    Surface(color = MaterialTheme.colors.background) {
        Scaffold(
            bottomBar = {
                Column {
                    Spacer(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(Color.LightGray)
                    )
                    BottomNavigationBar(navController, viewModel)
                }
            }
        ) { _ ->
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier.fillMaxSize()
            ) {
                Crossfade(selectedTab) { destination ->
                    when (destination) {
                        NavigationItem.Chat -> MessengerComposeTheme(color = Color.White) {
                            ChatScreen(
                                openProfile = {
                                    viewModel.selectTab(NavigationItem.Discover)
                                },
                            ) {
                                val json = Uri.encode(Gson().toJson(it))
                                navController.navigate("chat_detail" + "/${json}")
                            }
                        }

                        NavigationItem.Videos -> MessengerComposeTheme(color = Color.Black) {
                            VideosScreen()
                        }

                        NavigationItem.Friends -> MessengerComposeTheme(color = Color.White) {
                            FriendsScreen(
                                openProfile = {
                                    viewModel.selectTab(NavigationItem.Discover)
                                },
                            ) {
                                val json = Uri.encode(Gson().toJson(it))
                                navController.navigate("chat_detail" + "/${json}")
                            }
                        }
                        NavigationItem.Discover -> MessengerComposeTheme(color = Color.White) {
                            ProfileScreen(logout)
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, viewModel: MainViewModel) {
    val selectedTab by viewModel.selectedTab

    val items = listOf(
        NavigationItem.Chat,
        NavigationItem.Videos,
        NavigationItem.Friends,
        NavigationItem.Discover
    )
    BottomNavigation(
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        modifier = Modifier
            .background(
                Brush.horizontalGradient(
                    colors = listOf(
                        Color(0x1AFFFFFF),
                        Color(0x1AA4AAB2),
                        Color(0x1A000000),
                    )
                )
            )
            .navigationBarsHeight(56.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = if (item.title == "Videos") Modifier.size(item.size) else Modifier.size(
                            60.dp
                        )
                    )
                },
                label = null,
                selectedContentColor = Color.Black,
                unselectedContentColor = Color(0XFFA4AAB2),
                alwaysShowLabel = false,
                selected = item == selectedTab,
                onClick = { viewModel.selectTab(item) },
            )
        }
    }
}