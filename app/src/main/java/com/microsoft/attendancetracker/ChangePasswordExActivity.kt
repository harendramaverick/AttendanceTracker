package com.microsoft.attendancetracker

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.microsoft.attendancetracker.database.AppDatabase
import com.microsoft.attendancetracker.database.repository.ChangePasswordRepository
import com.microsoft.attendancetracker.database.factory.ChangePasswordVMlFactory
import com.microsoft.attendancetracker.data.SessionManager
import com.microsoft.attendancetracker.data.ThemeManager
import com.microsoft.attendancetracker.ui.theme.AttendanceTrackerTheme
import com.microsoft.attendancetracker.viewmodel.AuthViewModel

class ChangePasswordExActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeManager = ThemeManager(LocalContext.current)
            val uDarkTheme by themeManager.themeFlow.collectAsState()

            val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "attendance_database"
            ).build()
            val session = SessionManager(this)
            val repository = ChangePasswordRepository(db.userDao(), session)
            val factory = ChangePasswordVMlFactory(repository)
            val viewModel: AuthViewModel = viewModel(factory = factory)

            AttendanceTrackerTheme(useDarkTheme = uDarkTheme) {
                ChangePasswordScreenMainEx(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreenMainEx(viewModel: AuthViewModel) {
    val activity = LocalContext.current as? Activity
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Change Password") },
                navigationIcon = {
                    IconButton(onClick = {
                        activity?.finish()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Toggle Theme"
                        )
                    }
                }
            )
        }
    ) {
        padding ->
        ChangePasswordScreenEx(
            modifier = Modifier.padding(padding), viewModel
        )
    }
}


@Composable
fun ChangePasswordScreenEx(modifier: Modifier = Modifier,  viewModel: AuthViewModel = viewModel()) {   // , onToggleTheme: () -> Unit) {
    var currentPw by remember { mutableStateOf("") }
    var newPw by remember { mutableStateOf("") }
    var confirmPw by remember { mutableStateOf("") }

    var currVisible by remember { mutableStateOf(false) }
    var newVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val activity = LocalContext.current as? Activity

    val passwordMatch = confirmPw.isEmpty() || confirmPw == newPw

    LaunchedEffect(viewModel.isSuccess) {
        if(viewModel.isSuccess == true){
                Toast.makeText(context, viewModel.message, Toast.LENGTH_SHORT)
                    .show()
            activity?.finish()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        Text("Current Password", color = MaterialTheme.colorScheme.onBackground)
        PasswordInput(
            value = currentPw,
            onValueChange = { currentPw = it },
            visible = currVisible,
            onToggleVisibility = { currVisible = !currVisible },
            placeholder = "Enter your current password"
        )

        Spacer(Modifier.height(20.dp))
        Text("New Password", color = MaterialTheme.colorScheme.onBackground)
        PasswordInput(
            value = newPw,
            onValueChange = { newPw = it },
            visible = newVisible,
            onToggleVisibility = { newVisible = !newVisible },
            placeholder = "At least 8 characters"
        )

        Spacer(Modifier.height(20.dp))
        Text("Confirm New Password", color = MaterialTheme.colorScheme.onBackground)
        PasswordInput(
            value = confirmPw,
            onValueChange = { confirmPw = it },
            visible = confirmVisible,
            onToggleVisibility = { confirmVisible = !confirmVisible },
            placeholder = "Re-enter new password",
            isError = !passwordMatch
        )

        if (!passwordMatch) {
            Text(
                "Passwords do not match.",
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 4.dp, top = 4.dp)
            )
        }

        Spacer(Modifier.height(40.dp))

        Button(
            onClick = {
                        when {
                                currentPw.isEmpty() -> {
                                    Toast.makeText(context, "Enter current password", Toast.LENGTH_SHORT).show()
                                }
                                newPw.length < 8 -> {
                                    Toast.makeText(context, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show()
                                }
                                newPw != confirmPw -> {
                                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    viewModel.changePassword(currentPw, newPw)
                                }
                            }
                      },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Update Password", fontSize = 18.sp)
        }
    }
}

@Composable
fun PasswordInput(
    value: String,
    onValueChange: (String) -> Unit,
    visible: Boolean,
    onToggleVisibility: () -> Unit,
    placeholder: String,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        isError = isError,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    if (visible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = null
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth(),
        placeholder = { Text(placeholder) },
        shape = RoundedCornerShape(12.dp)
    )
}


class FakeAuthViewModel : AuthViewModel(null) {
    override fun changePassword(currentPw: String, newPw: String) {
        // Do nothing
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLightChangePasswordMainEx() {
    val fakeViewModel : FakeAuthViewModel = viewModel()
    AttendanceTrackerTheme(useDarkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background)
        {
            ChangePasswordScreenMainEx(fakeViewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDarkChangePasswordMainEx() {
    val fakeViewModel : FakeAuthViewModel = viewModel()
    AttendanceTrackerTheme(useDarkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background)
        {
            ChangePasswordScreenMainEx(fakeViewModel)
        }
    }
}




