package com.microsoft.attendancetracker

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.microsoft.attendancetracker.component.BottomNavBar
import com.microsoft.attendancetracker.ui.theme.AttendanceTrackerTheme
import com.microsoft.attendancetracker.viewmodel.ThemeViewModel

class AttendanceActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AttendanceScreenMain()
        }
    }
}

@Composable
fun AttendanceScreenMain()
{
    var iSCheckedInState by remember {  mutableStateOf(false) }
    val themeViewModel: ThemeViewModel = viewModel()
    val uDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    AttendanceTrackerTheme(useDarkTheme = uDarkTheme) {
        AttendanceScreen(
            isCheckedIn =   iSCheckedInState,
            onCheckIn   = { iSCheckedInState = true },
            onCheckOut  = { iSCheckedInState = false },
            onToggleTheme = { themeViewModel.toggleTheme() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    isCheckedIn: Boolean,
    onCheckIn: () -> Unit,
    onCheckOut: () -> Unit,
    onToggleTheme: () -> Unit
) {

    // NEW STATES TO SHOW/HIDE CARDS
    var showCheckInCard by remember { mutableStateOf(false) }
    var showCheckOutCard by remember { mutableStateOf(false) }
    val activity = LocalContext.current as? Activity
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Attendance Submission",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {activity?.finish()}
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                }
            )
        },
        bottomBar = { BottomNavBar(3) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Today, 24 July",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 18.sp
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = if (isCheckedIn) "You are currently checked in." else "You are currently checked out.",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(30.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                // CHECK-IN BUTTON
                Button(
                    onClick = {
                        onCheckIn()
                        Log.d("MY_LOG", isCheckedIn.toString());
                        showCheckInCard = !showCheckInCard
                    },
                    enabled = !isCheckedIn,
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp)
                ) {
                    Text("Check-in", fontSize = 18.sp)
                }

                Spacer(Modifier.width(12.dp))

                // CHECK-OUT BUTTON
                Button(
                    onClick = {
                        onCheckOut()
                        showCheckOutCard = !showCheckOutCard
                    },
                    enabled = isCheckedIn,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color(0xFFE3E8EF),
                        disabledContentColor = Color(0xFF9AA5B1)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(55.dp)
                ) {
                    Text("Check-out", fontSize = 18.sp)
                }
            }

            Spacer(Modifier.height(40.dp))

            Text(
                text = "Today's Log",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(16.dp))

            // SHOW CHECK-IN CARD ONLY WHEN CLICKED
            if (showCheckInCard) {
                LogCard(
                    icon = Icons.Default.Login,
                    iconColor = Color(0xFF12D272),
                    bgColor = Color(0xFFE8FFF4),
                    title = "Checked-in at:",
                    time = "09:03 AM"
                )

                Spacer(Modifier.height(12.dp))
            }

            // SHOW CHECK-OUT CARD ONLY WHEN CLICKED
            if (showCheckOutCard) {
                LogCard(
                    icon = Icons.Default.Logout,
                    iconColor = Color(0xFFE53935),
                    bgColor = Color(0xFFFFEAEA),
                    title = "Checked-out at:",
                    time = "05:15 PM"
                )
            }
        }
    }


}

@Composable
fun LogCard(
    icon: ImageVector,
    iconColor: Color,
    bgColor: Color,
    title: String,
    time: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(bgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = time,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLight1() {
    AttendanceTrackerTheme(useDarkTheme = false) {
        AttendanceScreen(
            isCheckedIn = false,
            onCheckIn = {},
            onCheckOut = {},
            onToggleTheme = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDark2() {
    AttendanceTrackerTheme(useDarkTheme = true) {
        AttendanceScreen(
            isCheckedIn = false,
            onCheckIn = {},
            onCheckOut = {},
            onToggleTheme = {}
        )
    }
}
