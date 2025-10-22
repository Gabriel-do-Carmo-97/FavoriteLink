package br.com.wgc.favoritelink

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import br.com.wgc.favoritelink.ui.home_screen.HomeScreen
import br.com.wgc.favoritelink.ui.theme.FavoriteLinkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FavoriteLinkTheme {
                HomeScreen()
            }
        }
    }
}