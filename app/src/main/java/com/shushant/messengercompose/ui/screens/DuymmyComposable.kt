package com.shushant.messengercompose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DummyUi(){
    Column() {
        Row{
            RadioButton(selected = true, onClick = { /*TODO*/ })
            Spacer(modifier = Modifier.width(10.dp))
            LazyColumn(){
                items(mutableListOf("aba","anlkalk")){value->
                    Text(text = value)
                }
            }
        }
        Row {
            Spacer(Modifier.width(30.dp))
            Text(
                text = "SGD",
            )
            Spacer(Modifier.width(2.dp))
            Text(
                "12842108y401"
            )
        }
    }
}


@Preview
@Composable
fun PreviewData(){
    DummyUi()
}