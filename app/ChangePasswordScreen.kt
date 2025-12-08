// ChangePasswordScreen.kt
package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen() {

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showCurrent by remember { mutableStateOf(false) }
    var showNew by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }

    val passwordsMatch = confirmPassword.isEmpty() || newPassword == confirmPassword
    val strength = passwordStrength(newPassword)

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

        // Current Password
        PasswordTextField(
            value = currentPassword,
            onValueChange = { currentPassword = it },
            label = "Current Password",
            showPassword = showCurrent,
            onToggle = { showCurrent = !showCurrent }
        )

        Spacer(modifier = Modifier.height(20.dp))

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
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            enabled = currentPassword.isNotEmpty() &&
                    newPassword.length >= 8 &&
                    passwordsMatch
        ) {
            Text("Update Password")
        }
    }
}

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
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = "toggle password"
                )
            }
        }
    )
}

@Composable
fun PasswordStrengthIndicator(strength: PasswordStrength) {
    val color = when (strength) {
        PasswordStrength.Weak -> MaterialTheme.colorScheme.error
        PasswordStrength.Medium -> MaterialTheme.colorScheme.tertiary
        PasswordStrength.Strong -> MaterialTheme.colorScheme.primary
    }

    Column(Modifier.padding(top = 10.dp)) {
        LinearProgressIndicator(
            progress = strength.progress,
            color = color,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
        Text(
            text = strength.label,
            color = color,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

enum class PasswordStrength(val label: String, val progress: Float) {
    Weak("Weak", 0.3f),
    Medium("Medium", 0.6f),
    Strong("Strong", 1f)
}

fun passwordStrength(password: String): PasswordStrength {
    return when {
        password.length >= 10 && password.any { it.isDigit() } && password.any { !it.isLetterOrDigit() } ->
            PasswordStrength.Strong

        password.length >= 8 ->
            PasswordStrength.Medium

        else ->
            PasswordStrength.Weak
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLight() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        ChangePasswordScreen()
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        ChangePasswordScreen()
    }
}
