package com.example.trackerapp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.trackerapp.navigation.AppScreens

@Composable
fun LoginScreen(navController: NavController) {
    val emailState = remember { mutableStateOf("user@email.com") }
    val passwordState = remember { mutableStateOf("1234") }
    val context = LocalContext.current

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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                modifier = Modifier.size(150.dp, 150.dp),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo"
            )
            Text(color = Color.White, text = "ID_DEVICE: "+ GlobalVariables.idDevice.toString())
            Text(
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp),
                text = "Login"
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                modifier = Modifier.border(BorderStroke(1.dp, Color(0xFFD71729))),
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text(text = "Email", color = Color(0xFFD71729)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                modifier = Modifier.border(BorderStroke(1.dp, Color(0xFFD71729))),
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text(text = "Password", color = Color(0xFFD71729)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFFD71729)
                ),
                onClick = {
                    if (emailState.value == "user@email.com" && passwordState.value == "1234") {
                        showToast(context, "Success")
                        navController.navigate(AppScreens.MainScreen.route)
                    }
                    else {
                        showToast(context, "Wrong credentials")
                    }
                },
            ) {
                Text(text = "Login")
            }
        }
    }
}

private fun showToast(context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    LoginScreen(navController)
}