package com.tamzi.grapla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tamzi.gruid.theme.GraplaTheme

/**
 * Main entry point for the Grapla application.
 * Sets up the app with Material 3 theming and edge-to-edge display.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GraplaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Grapla",
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

/**
 * Displays a greeting message.
 *
 * @param name The name to greet.
 * @param modifier Optional modifier for styling and layout customization.
 */
@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GraplaTheme {
        Greeting("Grapla")
    }
}
