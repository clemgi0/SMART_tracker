package com.example.trackerapp.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay

private const val LOCATION_UPDATE_DELAY = 5000L

@Composable
fun LocationScreen(modifier: Modifier = Modifier, textStyle: TextStyle = LocalTextStyle.current) {
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
                    getCurrentLocation(context) { lat, long ->
                        locationText = "Your location\nLatitude: $lat, Longitude: $long"
                    }
                } else {
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            })

    LaunchedEffect(hasPermission) {
        while(true) {
            if (hasPermission)
                {
                    // Permission already granted, update the location
                    getCurrentLocation(context) { lat, long ->
                        locationText = "Your location\n" +
                                "Latitude: $lat, Longitude: $long"
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
            delay(LOCATION_UPDATE_DELAY)
            }
    }
        Text(text = locationText,modifier = modifier, style = textStyle)
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

private fun getCurrentLocation(context: Context, callback: (Double, Double) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    callback(location.latitude, location.longitude)
                    Toast.makeText(context, "Location updated", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(context, "Location couldn't be fetched", Toast.LENGTH_SHORT).show()
                exception.printStackTrace()
            }
    } else {
        // If neither permission is granted, consider providing additional guidance or fallback behavior
        Toast.makeText(context, "No location permission granted", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun LocationScreenPreview(){
LocationScreen()
}