package com.example.trackerapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay

@Composable
fun MainScreen() {
    val context = LocalContext.current

    // Create a launcher for starting the Wi-Fi activity
    val wifiLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // Handle the result if needed
    }

    // State to hold the current wifi signal strength
    var wifiSignalStrength by remember { mutableStateOf(0) }

    // State to hold if the wifi signal is low
    var isWifiSignalLow by remember { mutableStateOf(false) }

    // Launch coroutine to periodically check wifi signal strength
    LaunchedEffect(Unit) {
        while (true) {
            // Get wifi signal strength
            wifiSignalStrength = getWifiSignalStrength(context)
            // Check if wifi signal is low
            isWifiSignalLow = wifiSignalStrength < LOW_SIGNAL_THRESHOLD
            Log.d("wifi", wifiSignalStrength.toString())
            Toast.makeText(context, wifiSignalStrength.toString(), Toast.LENGTH_SHORT).show()
            delay(CHECK_INTERVAL)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFF1C31),
                        Color(0xFFD71729)
                    )
                )
            )
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier.size(150.dp, 150.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo"
        )
        Text(
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            text = "Linked devices"
        )
        Text(
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            text = "Longitude : ${1} | Latitude : ${2}"
        )
        Button(
            shape = RoundedCornerShape(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFFD71729)
            ),
            onClick = {
                val intent = Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)
                wifiLauncher.launch(intent) // Launch the Wi-Fi activity
            },
        ) {
            Text(text = "Wifi")
        }
    }
}

// Constants
private const val CHECK_INTERVAL = 5000L // Check wifi signal strength every 5 seconds
private const val LOW_SIGNAL_THRESHOLD = 20 // Define your low signal strength threshold here

fun getWifiSignalStrength(context: Context): Int {
    // Check if the app has permission to access Wi-Fi state
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
        // Access WifiManager
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        // Get WifiInfo
        val wifiInfo: WifiInfo? = wifiManager.connectionInfo
        // Get the signal strength in dBm
        val signalStrength = wifiInfo?.rssi ?: -1
        // Convert signal strength to percentage
        return calculateSignalLevel(signalStrength, 100)
    } else {
        // App doesn't have permission to access Wi-Fi state
        return -1
    }
}

fun calculateSignalLevel(rssi: Int, numLevels: Int): Int {
    val MIN_RSSI = -100
    val MAX_RSSI = -55
    if (rssi <= MIN_RSSI) {
        return 0
    } else if (rssi >= MAX_RSSI) {
        return numLevels - 1
    } else {
        val inputRange = MAX_RSSI - MIN_RSSI
        val outputRange = numLevels - 1
        return ((rssi - MIN_RSSI) * outputRange) / inputRange
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
