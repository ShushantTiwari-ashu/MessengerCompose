
@file:Suppress("unused")

package com.shushant.messengercompose.initializer

import android.content.Context
import androidx.startup.Initializer
import com.shushant.messengercompose.operator.GlobalResponseOperator
import com.skydoves.sandwich.SandwichInitializer

class SandwichInitializer : Initializer<Unit> {

  override fun create(context: Context) {

    // initialize global sandwich operator
    SandwichInitializer.sandwichOperator = GlobalResponseOperator<Unit>(context)
  }

  override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
