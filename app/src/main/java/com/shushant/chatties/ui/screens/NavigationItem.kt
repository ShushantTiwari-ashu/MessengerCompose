package com.shushant.chatties.ui.screens

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.shushant.chatties.R

sealed class NavigationItem(var route: String, var icon: Int, var title: String, var size: Dp = 24.dp) {
    object Home : NavigationItem("home", R.drawable.ic_tab_1, "Home")
    object Chat : NavigationItem("chat", R.drawable.ic_tab_1, "Chat")
    object Friends : NavigationItem("friends", R.drawable.ic_tab_2, "Friends")
    object Videos : NavigationItem("videos", R.drawable.ic_baseline_ondemand_video_24, "Videos")
    object Discover : NavigationItem("discover", R.drawable.ic_tab_3, "Discover")
    object ChatDetail : NavigationItem("chat_detail/{data}", R.drawable.ic_tab_3, "ChatDetail")
}