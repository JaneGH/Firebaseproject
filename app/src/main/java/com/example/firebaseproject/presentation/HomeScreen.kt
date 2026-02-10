package com.example.firebaseproject.presentation

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun HomeScreen () {
    Text(
        modifier = Modifier.systemBarsPadding(),
        text = "Home screen"
    )
}