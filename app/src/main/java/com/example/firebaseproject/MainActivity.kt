package com.example.firebaseproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.firebaseproject.presentation.AppGraph
import com.example.firebaseproject.presentation.login.LoginScreen
import com.example.firebaseproject.ui.theme.FirebaseProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FirebaseProjectTheme {
                val navigationController = rememberNavController()
                AppGraph(navigationController)
            }
        }
    }
}