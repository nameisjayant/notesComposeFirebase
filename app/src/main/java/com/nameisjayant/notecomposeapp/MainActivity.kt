package com.nameisjayant.notecomposeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nameisjayant.notecomposeapp.feature.bottombar.BottomBarRoutes
import com.nameisjayant.notecomposeapp.feature.bottombar.BottomBarScreen
import com.nameisjayant.notecomposeapp.feature.navigation.AppNavigation
import com.nameisjayant.notecomposeapp.ui.theme.NoteComposeAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appState = rememberAppState()
            NoteComposeAppTheme {
                Scaffold(
                    bottomBar = {
                        if (appState.shouldShowBottomBar)
                            BottomAppBar(
                                containerColor = Color.White
                            ) {
                                BottomBarScreen(navHostController = appState.navHostController)
                            }
                    }
                ) { innerPadding ->
                    AppNavigation(navHostController = appState.navHostController, innerPadding)
                }
            }
        }
    }
}


@Composable
fun rememberAppState(
    navHostController: NavHostController = rememberNavController()
) = remember(navHostController) {
    AppState(navHostController)
}

@Stable
class AppState(
    val navHostController: NavHostController
) {
    private val routes = BottomBarRoutes.entries.map { it.route }
    val shouldShowBottomBar: Boolean
        @Composable get() =
            navHostController.currentBackStackEntryAsState().value?.destination?.route in routes
}