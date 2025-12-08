package com.microsoft.attendancetracker

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.microsoft.attendancetracker.ui.theme.AttendanceTrackerTheme
import com.microsoft.attendancetracker.viewmodel.ThemeViewModel

class ForgotPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForgetPasswordDetails()
        }
    }
}

@Composable
fun ForgetPasswordDetails()
{
    val themeViewModel: ThemeViewModel = viewModel()
    val uDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    val activity = LocalContext.current as? Activity
    AttendanceTrackerTheme(useDarkTheme = uDarkTheme) {
        ForgotPasswordScreen(
            onBack = { activity?.finish() }
        )
    }
}

/* ------------------------------------------------------
   FORGOT PASSWORD SCREEN
-------------------------------------------------------*/
@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit
) {
    var email by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(26.dp)
                        .clickable { onBack() }
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            Spacer(Modifier.height(10.dp))

            Text(
                text = "Forgot Password?",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Enter your email and we'll send you a link to get back into your account.",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(Modifier.height(30.dp))

            Text("Email Address", fontSize = 15.sp)

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    Icon(Icons.Outlined.Email, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(Modifier.height(30.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Send Reset Link", fontSize = 17.sp)
            }

            Spacer(Modifier.weight(1f))

            TextButton(
                onClick = { onBack() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Back to Login")
            }
        }
    }
}

/* ------------------------------------------------------
   PREVIEWS
-------------------------------------------------------*/

@Preview(showBackground = true)
@Composable
fun ForgotPasswordLightPreview() {
    AttendanceTrackerTheme(useDarkTheme = false) {
        ForgotPasswordScreen({})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun ForgotPasswordDarkPreview() {
    AttendanceTrackerTheme(useDarkTheme = true) {
        ForgotPasswordScreen({})
    }
}
