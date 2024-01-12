package com.nameisjayant

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class BaseApplication : Application() {
    companion object {
        val scope = CoroutineScope(SupervisorJob())
    }
}