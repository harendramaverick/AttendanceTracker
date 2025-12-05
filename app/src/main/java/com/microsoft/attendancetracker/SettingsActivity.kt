package com.microsoft.attendancetracker

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.microsoft.attendancetracker.component.BottomNavBar
import com.microsoft.attendancetracker.ui.theme.AttendanceTrackerTheme
import com.microsoft.attendancetracker.viewmodel.ThemeViewModel

class SettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SettingScreenView()
        }
    }
}


@Composable
fun SettingScreenView()
{
    val themeViewModel: ThemeViewModel = viewModel()
    val uDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    val activity = LocalContext.current as? Activity
    AttendanceTrackerTheme(useDarkTheme = uDarkTheme)
    {
        Scaffold(
            bottomBar = { BottomNavBar(4) }
        )
        {
            padding ->
            SettingsScreen(
                darkTheme = uDarkTheme,
                onThemeToggle = {
                    themeViewModel.toggleTheme()
                    Log.d("SettingsActivity", "Theme toggled: $uDarkTheme")
                },
                onBack = {
                    activity?.finish()
                },
                modifier = Modifier.padding(padding)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    darkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column {

            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 30.dp)
            ) {

                item { ProfileCard() }

                item { SectionTitle("ACCOUNT") }
                item { SettingRow(Icons.Default.Lock, "Change Password") }
                item { Divider() }
                item { SettingRow(Icons.Default.FileDownload, "Export Data") }

                item { SectionTitle("NOTIFICATIONS") }
                item { ToggleRow("Push Notifications") }
                item { Divider() }
                item { DropdownRow("Email Summaries", "Weekly") }
                item { Divider() }
                item { ToggleRow("Reminder Alerts") }

                item { SectionTitle("APP") }
                item { ThemeRow(darkTheme, onThemeToggle) }
                item { Divider() }
                item { DropdownRow("Default View", "Calendar") }

                item { SectionTitle("SUPPORT & LEGAL") }
                item { SettingRow(Icons.Default.Help, "Help & Support") }
                item { Divider() }
                item { SettingRow(Icons.Default.PrivacyTip, "Privacy Policy") }
                item { Divider() }
                item { SettingRow(Icons.Default.Description, "Terms of Service") }

                item { LogoutButton() }

                item {
                    Text(
                        "App Version 1.2.3",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }

                item{ Spacer( modifier = Modifier.height(90.dp ))}
            }
        }
    }
}

///////////////////////////////////////////////////////////////////////////
//  COMPONENTS
///////////////////////////////////////////////////////////////////////////

@Composable
fun ProfileCard() {
    Surface(
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .padding(10.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text("Alex Harrison", fontWeight = FontWeight.Bold)
                Text("alex.harrison@example.com", color = Color.Gray)
            }

            Surface(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    "Edit",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text,
        modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 10.dp),
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun SettingRow(icon: ImageVector, label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(26.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, modifier = Modifier.weight(1f))
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
    }
}

@Composable
fun ToggleRow(label: String) {
    var checked by remember { mutableStateOf(true) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = { checked = it })
    }
}

@Composable
fun DropdownRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, modifier = Modifier.weight(1f))
        Text(value, color = Color.Gray)
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
    }
}

@Composable
fun ThemeRow(darkTheme: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.InvertColors, contentDescription = null)
        Spacer(modifier = Modifier.width(16.dp))
        Text("Appearance", modifier = Modifier.weight(1f))
        Text(if (darkTheme) "Dark" else "Light", color = Color.Gray)
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
    }
}

@Composable
fun LogoutButton() {
    val context = LocalContext.current
    val activity = context as? Activity
    Button(
        onClick = {
            activity?.finish()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFE5E5))
    ) {
        Text("Logout", color = Color.Red)
    }
}


///////////////////////////////////////////////////////////////////////////
//  PREVIEWS
///////////////////////////////////////////////////////////////////////////

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun PreviewSettingsLight() {
    AttendanceTrackerTheme(useDarkTheme = false){
        SettingsScreen(
            darkTheme = false,
            onThemeToggle = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun PreviewSettingsDark() {
    AttendanceTrackerTheme(useDarkTheme = true) {
        SettingsScreen(
            darkTheme = true,
            onThemeToggle = {},
            onBack = {}
        )
    }
}
