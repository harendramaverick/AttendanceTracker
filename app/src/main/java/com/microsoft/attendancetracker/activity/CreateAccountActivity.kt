package com.microsoft.attendancetracker.activity

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.microsoft.attendancetracker.database.AppDatabase
import com.microsoft.attendancetracker.database.CreateAccountVMFactory
import com.microsoft.attendancetracker.viewmodel.CreateAccountViewModel
import com.microsoft.attendancetracker.database.UserRepository
import com.microsoft.attendancetracker.ui.theme.AttendanceTrackerTheme
import com.microsoft.attendancetracker.viewmodel.ThemeViewModel

class CreateAccountActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create DB
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "attendance_database"
        ).build()

        val repo = UserRepository(db.userDao())
        val vmFactory = CreateAccountVMFactory(repo)

        setContent {
            val createAccountVM: CreateAccountViewModel =
                viewModel(factory = vmFactory)

            CreateAccountMainScreen(createAccountVM)
        }
    }
}


@Composable
fun CreateAccountMainScreen(viewModel: CreateAccountViewModel)
{
    val themeViewModel: ThemeViewModel = viewModel()
    val uDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    AttendanceTrackerTheme(useDarkTheme = uDarkTheme) {
        CreateAccountScreen(
            viewModel,
            onBack = {},
            onLoginClick = {}
        )
    }
}

@Composable
fun CreateAccountScreen(
    viewModel: CreateAccountViewModel,
    onBack: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val success by viewModel.success.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity

    if (success) {
        Toast.makeText(context, "Account Created!", Toast.LENGTH_SHORT).show()
        activity?.finish()
    }

    Surface(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {

            // Back Button
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        onBack()
                        activity?.finish()
                    }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Title
            Text(
                text = "Create Account",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Full Name Field
            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                placeholder = { Text("Enter your full name") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                placeholder = { Text("Enter your email") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Password
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                placeholder = { Text("Enter your password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                    )
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Confirm Password
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                placeholder = { Text("Confirm your password") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    Icon(
                        imageVector = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = null,
                        modifier = Modifier.clickable { confirmPasswordVisible = !confirmPasswordVisible }
                    )
                },
                visualTransformation = if (confirmPasswordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Create Account Button
            Button(
                onClick = {
                                if (password == confirmPassword) {
                                    viewModel.createAccount(fullName, email, password)
                                } else {
                                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                                }
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(text = "Create Account", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Already have an account
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {

                Text("Already have an account? ",
                    modifier = Modifier.clickable{
                    activity?.finish()
                })

                Text(
                    text = "Log In",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        onLoginClick()
                        activity?.finish()
                    }
                )
            }
        }
    }
}

class FakeCreateAccountViewModel : CreateAccountViewModel(null) {
    fun CreateAccountViewModel(fullName: String, email: String, password: String) {
        // No real login — for preview only
    }
}

val accountVM: FakeCreateAccountViewModel = FakeCreateAccountViewModel();

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun PreviewLightReport() {
    AttendanceTrackerTheme(useDarkTheme = false) {
        CreateAccountScreen(accountVM)
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
fun PreviewDarkReport() {

    AttendanceTrackerTheme(useDarkTheme = true) {
        CreateAccountScreen(accountVM)
    }
}
