package com.microsoft.attendancetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import com.microsoft.attendancetracker.ui.theme.AttendanceTrackerTheme

// ─────────────────────────────────────────────────────────────
// ACTIVITY WITH THEME SWITCH
// ─────────────────────────────────────────────────────────────

class ChangePasswordActivity : ComponentActivity() {

    private var isDarkTheme by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AttendanceTrackerTheme(useDarkTheme = isDarkTheme) {

                ChangePasswordScreen(
                    onToggleTheme = { isDarkTheme = !isDarkTheme }
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// CHANGE PASSWORD UI SCREEN
// ─────────────────────────────────────────────────────────────

@Composable
fun ChangePasswordScreen(
    onToggleTheme: () -> Unit
) {

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showCurrent by remember { mutableStateOf(false) }
    var showNew by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }

    val passwordsMatch = confirmPassword.isEmpty() || newPassword == confirmPassword
    val strength = passwordStrength(newPassword)


    Surface(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Change Password",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(32.dp))

            // New Password
            PasswordTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = "New Password",
                showPassword = showNew,
                onToggle = { showNew = !showNew }
            )

            // Strength Indicator
            if (newPassword.isNotEmpty()) {
                PasswordStrengthIndicator(strength)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Confirm Password
            PasswordTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm New Password",
                showPassword = showConfirm,
                onToggle = { showConfirm = !showConfirm },
                isError = !passwordsMatch
            )

            if (!passwordsMatch) {
                Text(
                    text = "Passwords do not match",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onToggleTheme,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)//,
                //enabled = currentPassword.isNotEmpty() &&
                //        newPassword.length >= 8 &&
                //        passwordsMatch
            ) {
                Text("Update Password")
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// PASSWORD TEXT FIELD COMPOSABLE
// ─────────────────────────────────────────────────────────────

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    showPassword: Boolean,
    onToggle: () -> Unit,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = isError,
        visualTransformation =
            if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector =
                        if (showPassword) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                    contentDescription = "toggle password"
                )
            }
        }
    )
}

// ─────────────────────────────────────────────────────────────
// PASSWORD STRENGTH INDICATOR
// ─────────────────────────────────────────────────────────────

@Composable
fun PasswordStrengthIndicator(strength: PasswordStrength) {
    val color = when (strength) {
        PasswordStrength.Weak -> MaterialTheme.colorScheme.error
        PasswordStrength.Medium -> Color(0xFFFF9800)
        PasswordStrength.Strong -> MaterialTheme.colorScheme.primary
    }

    Column(Modifier.padding(top = 10.dp)) {
        LinearProgressIndicator(
            progress = strength.progress,
            color = color,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
        )
        Text(
            text = strength.label,
            color = color,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

// ─────────────────────────────────────────────────────────────
// PASSWORD STRENGTH LOGIC
// ─────────────────────────────────────────────────────────────

enum class PasswordStrength(val label: String, val progress: Float) {
    Weak("Weak", 0.3f),
    Medium("Medium", 0.6f),
    Strong("Strong", 1f)
}

fun passwordStrength(password: String): PasswordStrength {
    return when {
        password.length >= 10 &&
                password.any { it.isDigit() } &&
                password.any { !it.isLetterOrDigit() } ->
            PasswordStrength.Strong

        password.length >= 8 ->
            PasswordStrength.Medium

        else ->
            PasswordStrength.Weak
    }
}

// ─────────────────────────────────────────────────────────────
// PREVIEWS
// ─────────────────────────────────────────────────────────────

@Preview(showBackground = true)
@Composable
fun PreviewLightChangePassword() {
    AttendanceTrackerTheme(useDarkTheme = false) {
        ChangePasswordScreen(onToggleTheme = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDarkChangePassword() {
    AttendanceTrackerTheme(useDarkTheme = true) {
        ChangePasswordScreen(onToggleTheme = {})
    }
}
