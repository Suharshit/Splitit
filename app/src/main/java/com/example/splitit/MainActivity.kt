package com.example.splitit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.splitit.ui.AppNavigation
import com.example.splitit.ui.theme.SplitItTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplitItTheme {
                AppNavigation()
            }
        }
    }
}