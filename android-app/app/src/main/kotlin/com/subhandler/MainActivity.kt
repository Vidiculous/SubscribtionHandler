package com.subhandler

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.subhandler.ui.screens.MainScreen
import com.subhandler.ui.theme.SubHandlerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SubHandlerTheme {
                MainScreen()
            }
        }
    }
}
