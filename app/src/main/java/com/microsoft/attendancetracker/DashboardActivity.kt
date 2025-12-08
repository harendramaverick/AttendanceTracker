package com.microsoft.attendancetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.microsoft.attendancetracker.ui.theme.AttendanceTrackerTheme
import com.microsoft.attendancetracker.viewmodel.ThemeViewModel
import com.microsoft.attendancetracker.component.BottomNavBar
import com.microsoft.attendancetracker.component.Logout

class DashboardActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DashboardMainScreen()
        }
    }
}

@Composable
fun DashboardMainScreen()
{
    val themeViewModel: ThemeViewModel = viewModel()
    val uDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    AttendanceTrackerTheme(useDarkTheme = uDarkTheme)
    {
        DashboardScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                navigationIcon = {
                    IconButton(onClick = {
                        Logout(context)
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Toggle Theme"
                        )
                    }
                }
            )
        },
        bottomBar = { BottomNavBar(1) }
    ) { padding ->
        DashboardContent(modifier = Modifier.padding(padding))
    }
}

@Composable
fun DashboardContent(modifier: Modifier = Modifier) {

    var selectedTab by remember { mutableStateOf(0) }
    val tabItems = listOf("Week", "Month", "Year")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Text("Dashboard", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(18.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            tabItems.forEachIndexed { index, text ->
                FilterChip(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    label = { Text(text) }
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Row(Modifier.fillMaxWidth()) {
            StatCard(
                title = "Total Attendance",
                value = "1,234",
                change = "+5%",
                changeColor = Color(0xFF00C853),
                icon = Icons.Default.CheckCircle,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(12.dp))
            StatCard(
                title = "Average Attendance",
                value = "92%",
                change = "-1%",
                changeColor = Color.Red,
                icon = Icons.Default.BarChart,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(20.dp))

        AttendanceTrendsCard()

        Spacer(Modifier.height(25.dp))

        Text("Recent Absences", fontWeight = FontWeight.Bold, fontSize = 20.sp)

        Spacer(Modifier.height(10.dp))

        AbsenceItem("Sarah Johnson", "Oct 26, 2023", "Absent")
        AbsenceItem("Michael Lee", "Oct 25, 2023", "Absent")
        AbsenceItem("Emily Chen", "Oct 24, 2023", "Absent")
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    change: String,
    changeColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(160.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                Text(title, color = Color.Gray)
            }

            Spacer(Modifier.height(8.dp))
            Text(value, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(change, color = changeColor, fontSize = 16.sp)
        }
    }
}

@Composable
fun AttendanceTrendsCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(Modifier.padding(16.dp)) {

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Attendance Trends", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("This Week", color = Color.Gray)
                }
                Text("+3%", color = Color(0xFF00C853), fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            )

            Spacer(Modifier.height(18.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach {
                    Text(it)
                }
            }
        }
    }
}

@Composable
fun AbsenceItem(name: String, date: String, status: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null)
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(date, color = Color.Gray)
            }

            Text(status, color = Color.Red, fontWeight = FontWeight.Bold)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LightPreview() {
    AttendanceTrackerTheme(useDarkTheme = false) {
        Surface(modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background)
        {
            DashboardScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DarkPreview() {
    AttendanceTrackerTheme(useDarkTheme = true) {
        Surface(modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background)
        {
            DashboardScreen()
        }
    }
}
