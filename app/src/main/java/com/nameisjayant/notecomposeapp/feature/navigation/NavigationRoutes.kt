package com.nameisjayant.notecomposeapp.feature.navigation


sealed class NavigationRoutes(val route: String) {

    data object Start : NavigationRoutes("/start")
    data object Login : NavigationRoutes("/login")
    data object Register : NavigationRoutes("/register")

    data object Main : NavigationRoutes("/main")
    data object AddEditNote : NavigationRoutes("/addEditNote")
    data object EditProfile : NavigationRoutes("/edit")
    data object ResetPassword : NavigationRoutes("/resetPassword")
}