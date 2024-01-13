package com.nameisjayant.notecomposeapp.feature.register.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.nameisjayant.notecomposeapp.R
import com.nameisjayant.notecomposeapp.data.viewmodel.NoteViewModel
import com.nameisjayant.notecomposeapp.feature.navigation.NavigationRoutes
import com.nameisjayant.notecomposeapp.utils.ONE
import com.nameisjayant.notecomposeapp.utils.PreferenceStore
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    navHostController: NavHostController,
    viewModel: NoteViewModel = hiltViewModel()
) {
    val index by viewModel.getPref(PreferenceStore.index).collectAsStateWithLifecycle(
        initialValue = "",
    )
    LaunchedEffect(key1 = index) {
        delay(2000)
        when (index) {
            ONE -> navHostController.navigate(NavigationRoutes.Main.route)
            else -> navHostController.navigate(NavigationRoutes.Login.route)
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_1), contentDescription = null,
        )
        Text(
            text = stringResource(id = R.string.notess_app),
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.W500
            )
        )
    }

}