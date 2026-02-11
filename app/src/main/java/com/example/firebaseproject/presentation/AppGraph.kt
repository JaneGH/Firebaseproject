package com.example.firebaseproject.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.firebaseproject.presentation.images.ImagesScreen
import com.example.firebaseproject.presentation.login.LoginScreen

@Composable
fun AppGraph(
     navController : NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ){
        composable("login"){ LoginScreen(onLoginSuccess = {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }) }
        composable("home"){ ImagesScreen() }

    }

}