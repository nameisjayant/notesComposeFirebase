package com.nameisjayant.notecomposeapp.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.navigation.NavHostController


typealias onValueChange = (String) -> Unit

fun navigateToWithPopping(
    navHostController: NavHostController,
    route: String,
    isInclusive: Boolean = true
) {
    navHostController.navigate(route) {
        popUpTo(route) {
            inclusive = isInclusive
        }
    }
}

fun Context.showMsg(msg: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, msg, duration).show()

fun Context.getActivity(): Activity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}