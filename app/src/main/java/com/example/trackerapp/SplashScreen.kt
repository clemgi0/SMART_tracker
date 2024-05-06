package com.example.trackerapp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController
import com.example.trackerapp.navigation.AppScreens
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun SplashScreen(navController: NavController) {
    val context: Context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    LaunchedEffect(key1 = true) {
        // Check if the device has an id_device stored
        val idDevice = sharedPreferences.getString("id_device", null)

        // If the device hasn't an id_device, create one
        if (idDevice == null) {
            val url = "http://10.0.2.2:8000/addtracker"

            // The latitude and longitude should be the Wifi location
            val requestBody = "{\"latitude\":1,\"longitude\":2}".toRequestBody("application/json; charset=utf-8".toMediaType())

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val client = OkHttpClient()

            // Save the id_device into the device
            val response = GlobalScope.launch(Dispatchers.IO) {
                try {
                    val response = client.newCall(request).execute()
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        val editor = sharedPreferences.edit()
                        editor.putString("id_device", responseData)
                        //Toast.makeText(context, "id_device: $responseData", Toast.LENGTH_LONG).show()
                        editor.apply()
                        GlobalVariables.idDevice = responseData

                    } else {
                        //Toast.makeText(context, "id_device couldn't be generated", Toast.LENGTH_LONG).show()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            response.join()
        }
        else {
            GlobalVariables.idDevice = idDevice
            Toast.makeText(context, "id_device found. Value: $idDevice", Toast.LENGTH_LONG).show()
        }

        navController.popBackStack()
        navController.navigate(AppScreens.LoginScreen.route)
    }

    Splash()
}

@Composable
fun Splash() {
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
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(350.dp, 350.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo"
        )
        Text(
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
            text = "Loading app..."
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    Splash()
}
