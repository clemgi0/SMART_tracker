package com.example.trackerapp

import WifiReceiver
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.trackerapp.navigation.AppNavigation
import com.example.trackerapp.ui.theme.TrackerAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {

//    private val locationPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted: Boolean ->
//        if (isGranted) {
//            // Location permission is granted, proceed with your app logic
//            startLocationTracking()
//        } else {
//            // Location permission is denied, handle it accordingly
//            // For example, show a message to the user or disable location-related functionality
//        }
//    }
//
//    private val wifiPermissionLauncher = registerForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { isGranted: Boolean ->
//        if (isGranted) {
//            // Camera permission is granted, proceed with your app logic
//            searchWifi()
//        } else {
//            // Camera permission is denied, handle it accordingly
//            // For example, show a message to the user or disable camera-related functionality
//        }
//    }

//    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackerAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}
//
//        // Request camera permission if not granted
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.CHANGE_WIFI_STATE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            wifiPermissionLauncher.launch(Manifest.permission.CHANGE_WIFI_STATE)
//        } else {
//            // Camera permission is already granted, proceed with your app logic
//            searchWifi()
//        }
//
//        // Request location permission if not granted
//        if (ContextCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
//        } else {
//            // Location permission is already granted, proceed with your app logic
//            startLocationTracking()
//        }



//    }
//
//    @SuppressLint("MissingPermission")
//    private fun startLocationTracking() {
//        Log.d("main","hello location")
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location : Location? -> Log.d("location","localisation : "+location)
//                // Got last known location. In some rare situations this can be null.
//
//                var loc_LATITUDE = location?.getLatitude()
//                var loc_LONGITUDE = location?.getLongitude()
//                Log.d("location", loc_LATITUDE.toString())
//                Log.d("location", loc_LONGITUDE.toString())
//            }
//
//
//    }
//
//    private fun searchWifi() {
//        Log.d("main","hello wifi")
//        val intent = Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)
//        startActivity(intent)
//        var wifiReceiver = WifiReceiver()
//        registerReceiver(wifiReceiver, IntentFilter(WifiManager.RSSI_CHANGED_ACTION))}
//}

