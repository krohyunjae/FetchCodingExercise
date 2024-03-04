package com.barleytea.fetchcodingexercise.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.barleytea.fetchcodingexercise.app.ui.home.HomeScreen
import com.barleytea.fetchcodingexercise.ui.theme.FetchCodingExerciseTheme
import com.barleytea.fetchcodingexercise.utils.setOrientation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setOrientation()
        setContent {
            FetchCodingExerciseTheme {
               Surface(
                   modifier = Modifier.fillMaxSize()
               ) {
                    HomeScreen()
               }
            }

        }
    }
}