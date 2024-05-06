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
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.trackerapp.location.LocationScreen
import com.example.trackerapp.location.hasLocationPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

@Composable
fun MainScreen(navController : NavController) {
    val context = LocalContext.current
    // ------ API -------

    //val client = OkHttpClient()
    //get(url = "http://10.0.2.2:8000", client=client)

    // ------ WIFI --------

    // Create a launcher for starting the Wi-Fi activity
    val wifiLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // Handle the result if needed
    }
    // State to hold the current wifi signal strength
    var wifiSignalStrength by remember { mutableIntStateOf(0) }

    // State to hold if the wifi signal is low
    var isWifiSignalLow by remember { mutableStateOf(false) }

    var isStatusAlert by remember { mutableStateOf(false) }

    // ------ LOCATION ------

    // Create a permission launcher
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                val hasPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                if (hasPermission) {
                    Toast.makeText(context, "Location permission given", Toast.LENGTH_SHORT).show()
                    } else {
                    Toast.makeText(context, "Location permission denied", Toast.LENGTH_SHORT).show()
                }
            })

    // Launch coroutine to periodically check wifi signal strength
    LaunchedEffect(Unit) {
        if (!hasLocationPermission(context)) {
            requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
        }
        while (true) {
            // Get wifi signal strength
            wifiSignalStrength = getWifiSignalStrength(context)
            // Check if wifi signal is low
            isWifiSignalLow = wifiSignalStrength < LOW_SIGNAL_THRESHOLD
            Log.d("wifi", wifiSignalStrength.toString())
            //Toast.makeText(context, wifiSignalStrength.toString(), Toast.LENGTH_SHORT).show()
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
        if(isWifiSignalLow) {
            if (!isStatusAlert) {
                isStatusAlert = true
                updateStatus(1)
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 10.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                LocationScreen(navController)
            }
        } else {
            if (isStatusAlert) {
                isStatusAlert = false
                updateStatus(0)
            }
        }
        //Button(
            //shape = RoundedCornerShape(0.dp),
            //colors = ButtonDefaults.buttonColors(
                //containerColor = Color.White,
                //contentColor = Color(0xFFD71729)
            //),
            //onClick = {
                //val intent = Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)
                //wifiLauncher.launch(intent) // Launch the Wi-Fi activity
            //},
        //) {
            //Text(text = "Wifi")
        //}

    }
}

// Constants
private const val CHECK_INTERVAL = 5000L // Check wifi signal strength every 5 seconds
private const val LOW_SIGNAL_THRESHOLD = 70 // Define your low signal strength threshold here

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

fun updateStatus(status: Int){
    val url = "http://10.0.2.2:8000/setstatus"

    // The latitude and longitude should be the Wifi location
    val requestBody = "{\"id\":${GlobalVariables.idDevice},\"status\":${status}}".toRequestBody("application/json; charset=utf-8".toMediaType())
    val request = Request.Builder()
        .url(url)
        .post(requestBody)
        .build()

    val client = OkHttpClient()

    // Save the id_device into the device
    GlobalScope.launch(Dispatchers.IO) {
        try {
            client.newCall(request).execute()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
