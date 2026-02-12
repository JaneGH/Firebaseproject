package com.example.firebaseproject.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.firebaseproject.domain.model.Client
import com.example.firebaseproject.presentation.client.ClientScreen
import com.example.firebaseproject.presentation.client.ClientViewModel
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
        composable("home"){ ImagesScreen(
            onAddClientClick = {
                navController.navigate("client")
            }
        ) }

        composable("client") {
            val viewModel: ClientViewModel = hiltViewModel()
            ClientScreen(
                client = null,
                isEditMode = true,
                onSaveClick = { fullName, age, address, avatarUri ->

                    viewModel.saveClient(
                        fullName = fullName,
                        age = age,
                        address = address,
                        avatarUri = avatarUri
                    )

                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "client/{clientId}",
            arguments = listOf(
                navArgument("clientId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val clientId = backStackEntry.arguments?.getString("clientId")

            val viewModel: ClientViewModel = viewModel()

            ClientScreen(
                client = null,
                isEditMode = true,
                onSaveClick = { fullName, age, address, avatarUri ->

                    viewModel.saveClient(
                        fullName = fullName,
                        age = age,
                        address = address,
                        avatarUri = avatarUri
                    )

                    navController.popBackStack()
                }
            )
        }

    }

}