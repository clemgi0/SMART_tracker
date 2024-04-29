package com.example.trackerapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.trackerapp.ui.theme.TrackerAppTheme
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackerAppTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        GreetingWithButton("ENABLE")
    }
}

@Composable
fun GreetingWithButton(name: String) {
    val context = LocalContext.current  // Get the current context to use in Composable

    // Use a Column to position and size the Button
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Adds padding around the Column
        verticalArrangement = Arrangement.Center, // Centers the Button vertically
        horizontalAlignment = Alignment.CenterHorizontally // Centers the Button horizontally
    ) {
        Button(
            onClick = {
                // Display a toast
                Toast.makeText(context, "Button clicked!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .height(IntrinsicSize.Min) // Makes height fit to content
        ) {
            Text(name, fontSize = 22.sp)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingWithButtonPrev() {
    TrackerAppTheme {
        GreetingWithButton("Test button")
    }
}