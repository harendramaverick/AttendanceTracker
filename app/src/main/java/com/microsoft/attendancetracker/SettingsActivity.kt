package com.microsoft.attendancetracker

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.microsoft.attendancetracker.component.BottomNavBar
import com.microsoft.attendancetracker.component.Logout
import com.microsoft.attendancetracker.database.AppDatabase
import com.microsoft.attendancetracker.database.factory.LoginVMFactory
import com.microsoft.attendancetracker.database.repository.UserRepository
import com.microsoft.attendancetracker.data.SessionManager
import com.microsoft.attendancetracker.data.ThemeManager
import com.microsoft.attendancetracker.ui.theme.AttendanceTrackerTheme
import com.microsoft.attendancetracker.viewmodel.LoginViewModel

class SettingsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "attendance_database"
        ).build()
        val session = SessionManager(this)
        val repo = UserRepository(db.userDao(), session)
        val vmFactory = LoginVMFactory(repo)
        setContent {
            val loginVM: LoginViewModel = viewModel(factory = vmFactory)
            val email: String? = session.getLoginEmail()
            loginVM.getUserRecord(email)
            val themeManager = ThemeManager(this)
            val uDarkTheme by themeManager.themeFlow.collectAsState()
            SettingScreenView(loginVM, uDarkTheme, onToggleTheme = { newTheme -> themeManager.setThemeType(newTheme)})
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreenView( loginVM: LoginViewModel, uDarkTheme: Boolean, onToggleTheme: (Boolean) -> Unit)
{
    val context = LocalContext.current
    val activity = LocalContext.current as? Activity
    AttendanceTrackerTheme(useDarkTheme = uDarkTheme)
    {
        Scaffold(
             topBar = {
                 TopAppBar(
                     title = { Text("Settings", Modifier.padding(horizontal = 100.dp)) },
                     navigationIcon = {
                         IconButton(onClick = {
                             activity?.finish()
                             val intent = Intent(context, DashboardActivity::class.java)
                             context.startActivity(intent)
                             activity?.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                         }) {
                             Icon(
                                 imageVector = Icons.Default.ArrowBack,
                                 contentDescription = "Toggle Theme"
                             )
                         }
                     }
                 )
             }
            ,bottomBar = { BottomNavBar(4) }
        )
        {
            padding ->
            SettingsScreen(
                darkTheme = uDarkTheme,
                onToggleTheme = onToggleTheme,
                modifier = Modifier.padding(padding),
                loginVM
            )
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    darkTheme: Boolean,
    onToggleTheme: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    loginVM: LoginViewModel
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
           modifier.padding(20.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 30.dp)
            ) {

                item { ProfileCard(loginVM) }

                item { SectionTitle("ACCOUNT") }
                item { SettingRow(Icons.Default.Lock, "Change Password", onChangePassword = {})}
                item { Divider() }
                item { SettingRow(Icons.Default.FileDownload, "Export Data") }

                item { SectionTitle("NOTIFICATIONS") }
                item { ToggleRow("Push Notifications") }
                item { Divider() }
                item { DropdownRow("Email Summaries", "Weekly") }
                item { Divider() }
                item { ToggleRow("Reminder Alerts") }

                item { SectionTitle("APP") }
                item { ThemeRow(darkTheme, onToggleTheme) }
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
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

///////////////////////////////////////////////////////////////////////////
//  COMPONENTS
///////////////////////////////////////////////////////////////////////////

@Composable
fun ProfileCard( loginVM: LoginViewModel) {
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
                Text(if(loginVM._record.collectAsState().value != null) {loginVM._record.collectAsState().value!!.fullName} else {
                    "dummy"
                }, fontWeight = FontWeight.Bold)
                Text(if(loginVM._record.collectAsState().value != null) {loginVM._record.collectAsState().value!!.email} else {
                    "dummy@gmail.com"
                }, color = Color.Gray)
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
fun SettingRow(icon: ImageVector, label: String, onChangePassword: () -> Unit = {}) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(26.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, modifier = Modifier
                                            .weight(1f)
                                            .clickable(
                                                onClick = {
                                                    onChangePassword()
                                                    val intent = Intent(context,
                                                        ChangePasswordExActivity::class.java)
                                                    context.startActivity(intent)
                                                }
                                            ))
        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
    }
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
fun ThemeRow(darkTheme: Boolean,  onToggleTheme: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleTheme(!darkTheme) }
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
    Button(
        onClick ={
            Logout(context)
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
        val loginVM: FakeLoginViewModel = viewModel();
        SettingScreenView(
            loginVM,
            false,
            onToggleTheme = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun PreviewSettingsDark() {
    val loginVM: FakeLoginViewModel = viewModel();
    AttendanceTrackerTheme(useDarkTheme = true) {
        SettingScreenView(
            loginVM,
            true,
            onToggleTheme = {}
        )
    }
}
