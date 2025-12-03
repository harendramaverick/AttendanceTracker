package com.microsoft.attendancetracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.microsoft.attendancetracker.ui.theme.AttendanceTrackerTheme
import com.microsoft.attendancetracker.viewmodel.ThemeViewModel

class LoginActivity : ComponentActivity() {

    // IMPORTANT: Activity-level state (not inside Composable)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginMainScreen()
        }
    }
}

@Composable
fun LoginMainScreen ()
{
    val themeViewModel: ThemeViewModel = viewModel()
    val uDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    AttendanceTrackerTheme(useDarkTheme = uDarkTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LoginScreen(
                onLoginClicked = {
                    // isDarkTheme = !isDarkTheme
                    themeViewModel.toggleTheme()
                }
            )
        }
    }
}

@Composable
fun LoginScreen(onLoginClicked: () -> Unit) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(120.dp))

        Text(
            text = "Welcome back",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = {}) {
            Text("Forgot Password?")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                        onLoginClicked()
                        val intent = Intent(context, DashboardActivity::class.java)
                        context.startActivity(intent)
                      },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            Text("Log In", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        ClickableText(
            text = AnnotatedString("Don't have an account? Sign Up"),
            onClick = { onLoginClicked() },
            modifier = Modifier.padding(bottom = 24.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginLight() {
    AttendanceTrackerTheme(useDarkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background)
        {
            LoginScreen( onLoginClicked = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginDark() {
    AttendanceTrackerTheme(useDarkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background)
        {
            LoginScreen(onLoginClicked = {})
        }
    }
}
