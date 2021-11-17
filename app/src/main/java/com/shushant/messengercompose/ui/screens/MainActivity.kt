package com.shushant.messengercompose.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shushant.messengercompose.ui.screens.chat.ChatViewModel
import com.shushant.messengercompose.ui.theme.MessengerComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import com.shushant.messengercompose.ui.screens.chat.ChatScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: ChatViewModel by viewModels()
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MessengerComposeTheme {
                MainScreen(viewModel)
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun MainScreen(viewModel: ChatViewModel) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            Column {
                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray)
                )
                BottomNavigationBar(navController)
            }
        }
    ) {
        Navigation(navController = navController, viewModel)
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Chat,
        NavigationItem.Friends,
        NavigationItem.Discover
    )
    BottomNavigation(
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        modifier = Modifier.background(
            Brush.horizontalGradient(
            colors = listOf(
                Color(0x1AFFFFFF),
                Color(0x1AA4AAB2),
                Color(0x1A000000),
                )
        )).height(56.dp)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(60.dp)
                    )
                },
                label = null,
                selectedContentColor = Color.Black,
                unselectedContentColor = Color(0XFFA4AAB2),
                alwaysShowLabel = false,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun Navigation(navController: NavHostController, viewModel: ChatViewModel) {
    NavHost(
        navController,
        startDestination = NavigationItem.Chat.route,
        modifier = Modifier.padding(bottom = 56.dp)
    ) {
        composable(NavigationItem.Chat.route) {
            ChatScreen(viewModel)
        }
        composable(NavigationItem.Friends.route) {
            FriendsScreen(viewModel)
        }
        composable(NavigationItem.Discover.route) {
            DiscoverScreen(viewModel)
        }
    }
}


