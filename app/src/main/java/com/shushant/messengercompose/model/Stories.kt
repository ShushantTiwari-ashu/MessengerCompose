package com.shushant.messengercompose.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.concurrent.Immutable

@Immutable
@Entity()
data class Stories(
    @PrimaryKey(autoGenerate = true)
    val id: Long=0,
    var title: String = "Shushant",
    var image: String = "https://picsum.photos/200/300",
    var page:Int= 1
)
