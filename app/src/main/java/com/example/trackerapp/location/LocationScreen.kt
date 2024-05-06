package com.example.trackerapp.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.trackerapp.GlobalVariables
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


private const val LOCATION_UPDATE_INTERVAL = 5000L

@Composable
fun LocationScreen(navController: NavController) {
    val context = LocalContext.current
    var locationText by remember { mutableStateOf("Debug text for location") }
    var hasPermission by remember {
        mutableStateOf(hasLocationPermission(context))
    }

    // Create a permission launcher
    val requestPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                hasPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                if (hasPermission) {
                    startLocationUpdates(context) { lat, long ->
                        locationText = "Your location\nLatitude: $lat, Longitude: $long"
                    }
                } else {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            })

    LaunchedEffect(hasPermission) {
            if (hasPermission)
                {
                    // Permission already granted, update the location
                    startLocationUpdates(context) { lat, long ->
                        locationText = "Your location\n" +
                                "Latitude: $lat, Longitude: $long"
                        sendPosition(lat, long)
                    }
                } else {
                    // Request location permission
                    requestPermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
    }
        Text(text = locationText, style = TextStyle(color = Color.Black))
}

fun hasLocationPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
            ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}

fun startLocationUpdates(context: Context, callback: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    val locationRequest = LocationRequest.Builder(100, 2500L)
        .setIntervalMillis(LOCATION_UPDATE_INTERVAL)
        .build()

    val locationCallback = object : LocationCallback() {
       override fun onLocationResult(p0: LocationResult) {
            for (location in p0.locations) {
                callback(location.latitude, location.longitude)
            }
        }
    }

    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    } else {
        // If neither permission is granted, consider providing additional guidance or fallback behavior
        Toast.makeText(context, "No location permission granted", Toast.LENGTH_SHORT).show()
    }
}

private fun sendPosition(latitude: Double, longitude: Double) {
    val url = "http://10.0.2.2:8000/addposition"

    // The latitude and longitude should be the Wifi location
    val requestBody = "{\"latitude\":${latitude},\"longitude\":${longitude},\"tracker_id\":${GlobalVariables.idDevice}}".toRequestBody("application/json; charset=utf-8".toMediaType())
    Log.d("requestBody", requestBody.toString())
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

//@Preview(showBackground = true)
//@Composable
//fun LocationScreenPreview(){
//LocationScreen(navController)
//}