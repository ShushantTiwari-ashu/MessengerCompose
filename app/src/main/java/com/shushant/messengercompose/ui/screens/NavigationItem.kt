package com.shushant.messengercompose.ui.screens

import com.shushant.messengercompose.R

sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object Home : NavigationItem("home", R.drawable.ic_tab_1, "Home")
    object Chat : NavigationItem("chat", R.drawable.ic_tab_1, "Chat")
    object Friends : NavigationItem("friends", R.drawable.ic_tab_2, "Friends")
    object Discover : NavigationItem("discover", R.drawable.ic_tab_3, "Discover")
    object ChatDetail : NavigationItem("chat_detail/{data}", R.drawable.ic_tab_3, "ChatDetail")
}