package com.nameisjayant.notecomposeapp.feature.navigation

import android.provider.ContactsContract.Profile
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.nameisjayant.notecomposeapp.data.viewmodel.NoteViewModel
import com.nameisjayant.notecomposeapp.feature.bottombar.BottomBarRoutes
import com.nameisjayant.notecomposeapp.feature.notes.ui.screens.AddEditNoteScreen
import com.nameisjayant.notecomposeapp.feature.notes.ui.screens.EditProfileScreen
import com.nameisjayant.notecomposeapp.feature.notes.ui.screens.NoteScreen
import com.nameisjayant.notecomposeapp.feature.notes.ui.screens.ProfileScreen
import com.nameisjayant.notecomposeapp.feature.register.ui.screens.LoginScreen
import com.nameisjayant.notecomposeapp.feature.register.ui.screens.RegisterScreen
import com.nameisjayant.notecomposeapp.feature.register.ui.screens.ResetPasswordScreen
import com.nameisjayant.notecomposeapp.feature.register.ui.screens.SplashScreen


@Composable
fun AppNavigation(
    navHostController: NavHostController,
    innerPadding: PaddingValues
) {
    val viewModel: NoteViewModel = viewModel()
    NavHost(
        navController = navHostController,
        startDestination = NavigationRoutes.Start.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(NavigationRoutes.Start.route) {
            SplashScreen(navHostController)
        }
        composable(NavigationRoutes.Register.route) {
            RegisterScreen(navHostController)
        }
        composable(NavigationRoutes.Login.route) {
            LoginScreen(navHostController)
        }
        navigation(
            startDestination = BottomBarRoutes.Note.route,
            route = NavigationRoutes.Main.route
        ) {
            composable(BottomBarRoutes.Note.route) {
                NoteScreen(
                    navHostController,
                    viewModel
                )
            }
            composable(BottomBarRoutes.Profile.route) {
                ProfileScreen(navHostController,viewModel)
            }
        }
        composable(NavigationRoutes.AddEditNote.route) {
            AddEditNoteScreen(navHostController = navHostController, viewModel = viewModel)
        }
        composable(NavigationRoutes.EditProfile.route){
            EditProfileScreen(navHostController,viewModel)
        }
        composable(NavigationRoutes.ResetPassword.route){
            ResetPasswordScreen(navHostController = navHostController)
        }
    }
}