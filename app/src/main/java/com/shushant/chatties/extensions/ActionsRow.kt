package com.shushant.chatties.extensions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ActionsRow(
    actionIconSize: Dp,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onFavorite: () -> Unit,
    image1:ImageVector=Icons.Default.Menu,
    image2:ImageVector=Icons.Default.Notifications,
    image3:ImageVector=Icons.Default.Delete,
    color1:Color=Color(0x1A000000),
    color2:Color=Color(0x1A000000),
    color3:Color=Color(0XFFFE294D),
    tint1:Color=Color.Black,
    tint2:Color=Color.White,
    tint3:Color=Color.White,
) {
    Row(
        Modifier.padding(horizontal = 4.dp).width(150.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.size(actionIconSize).align(Alignment.CenterVertically).background(
                color1, CircleShape).padding(5.dp),
            onClick = {
                onDelete()
            },
            content = {
                Icon(
                    image1,
                    contentDescription = "delete action",tint = tint1
                )
            }
        )
        IconButton(
            modifier = Modifier.size(actionIconSize).align(Alignment.CenterVertically).background(
                color2, CircleShape).padding(5.dp),
            onClick = onEdit,
            content = {
                Icon(
                    image2,
                    contentDescription = "edit action",tint = tint2
                )
            },
        )
        IconButton(
            modifier = Modifier.size(actionIconSize).align(Alignment.CenterVertically).background(
                color3, CircleShape).padding(5.dp),
            onClick = onFavorite,
            content = {
                Icon(
                    image3,
                    contentDescription = "Expandable Arrow",tint = tint3
                )
            }
        )
    }
}