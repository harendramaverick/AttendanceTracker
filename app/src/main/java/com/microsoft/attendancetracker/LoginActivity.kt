package com.microsoft.attendancetracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
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
import androidx.room.Room
import com.microsoft.attendancetracker.database.AppDatabase
import com.microsoft.attendancetracker.database.LoginVMFactory
import com.microsoft.attendancetracker.database.UserRepository
import com.microsoft.attendancetracker.ui.theme.AttendanceTrackerTheme
import com.microsoft.attendancetracker.viewmodel.LoginViewModel
import com.microsoft.attendancetracker.viewmodel.ThemeViewModel

class LoginActivity : ComponentActivity() {

    // IMPORTANT: Activity-level state (not inside Composable)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create DB
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "attendance_database"
        ).build()

        val repo = UserRepository(db.userDao())
        val vmFactory = LoginVMFactory(repo)

        setContent {
            val loginVM: LoginViewModel = viewModel(factory = vmFactory)
            LoginMainScreen(loginVM)
        }
    }
}

@Composable
fun LoginMainScreen (viewModel: LoginViewModel)
{
    val themeViewModel: ThemeViewModel = viewModel()
    val uDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    AttendanceTrackerTheme(useDarkTheme = uDarkTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LoginScreen(viewModel)
        }
    }
}

@Composable
fun LoginScreen(viewModel: LoginViewModel) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginSuccess by viewModel.loginSuccess.collectAsState()
    val loginError by viewModel.loginError.collectAsState()
    var show by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Log.d("LoginScreen", "loginSuccess: $loginSuccess")
    Log.d("LoginScreen", "loginError: $loginError")

    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            val intent = Intent(context, DashboardActivity::class.java)
            context.startActivity(intent)
        }
    }

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

        // New Password
        PasswordTextField(
            value = password,
            onValueChange = { password = it },
            label = "Password",
            showPassword = show,
            onToggle = { show = !show }
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = {}) {
            Text("Forgot Password?", modifier = Modifier.clickable {
                val intent = Intent(context, ForgotPasswordActivity::class.java)
                context.startActivity(intent)
            })
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Error message
        if (loginError.isNotEmpty()) {
            Text(loginError, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                        viewModel.login(email, password)
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
            onClick = {
                        val intent = Intent(context, CreateAccountActivity::class.java)
                        context.startActivity(intent)
                      },
            modifier = Modifier.padding(bottom = 24.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
    }
}

class FakeLoginViewModel : LoginViewModel(null) {
    fun loginUser() {
    }
}

val loginVM: FakeLoginViewModel = FakeLoginViewModel();

@Preview(showBackground = true)
@Composable
fun PreviewLoginLight() {
    AttendanceTrackerTheme(useDarkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background)
        {
            LoginScreen(loginVM)
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
            LoginScreen(loginVM)
        }
    }
}
