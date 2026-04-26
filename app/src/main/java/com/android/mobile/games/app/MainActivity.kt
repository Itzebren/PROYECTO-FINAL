package com.android.mobile.games.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.android.mobile.games.app.navigation.AppNavigation
import com.android.mobile.games.app.ui.theme.AndroidmobilegamesappTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AndroidmobilegamesappTheme {
                AppNavigation()
            }
        }
    }
}