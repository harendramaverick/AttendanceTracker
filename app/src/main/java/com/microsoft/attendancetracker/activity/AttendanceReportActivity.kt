package com.microsoft.attendancetracker.activity

import android.R
import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.microsoft.attendancetracker.component.BottomNavBar
import com.microsoft.attendancetracker.ui.theme.AttendanceTrackerTheme
import com.microsoft.attendancetracker.viewmodel.ThemeViewModel

// ------------------------------------------------------------------------
//               ACTIVITY (With Theme Toggle on Back Button)
// ------------------------------------------------------------------------
class AttendanceReportActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AttendanceReportScreenMain()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceReportScreenMain()
{
    val themeViewModel: ThemeViewModel = viewModel()
    val uDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity
    Surface(modifier = Modifier.fillMaxSize()) {
        AttendanceTrackerTheme(useDarkTheme = uDarkTheme)
        {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Attendance Reports") },
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
            },
            bottomBar = { BottomNavBar(2) }
        ) {
            padding->
            AttendanceReportScreen(modifier = Modifier.padding(padding))
        }
        }
    }
}

// ------------------------------------------------------------------------
//                           MAIN SCREEN UI
// ------------------------------------------------------------------------
@Composable
fun AttendanceReportScreen(modifier: Modifier = Modifier)
{
    val context = LocalContext.current
    val activity = context as? Activity

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

           // Spacer(Modifier.height(120.dp))

            // TITLE ---------------------------------------------------------------
            Text(
                "Filter Your Report",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(20.dp))

            // FILTERS -------------------------------------------------------------
            FilterCard("Student", "All Students", R.drawable.ic_menu_myplaces)
            Spacer(Modifier.height(14.dp))
            FilterCard("Status", "All", R.drawable.ic_menu_sort_by_size)
            Spacer(Modifier.height(14.dp))
            FilterCard("Start Date", "01 Aug 2024", R.drawable.ic_menu_month)
            Spacer(Modifier.height(14.dp))
            FilterCard("End Date", "31 Aug 2024", R.drawable.ic_menu_month)

            Spacer(Modifier.height(20.dp))

            // GENERATE BUTTON -----------------------------------------------------
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("🔁  Generate Report", fontSize = 18.sp)
            }

            Spacer(Modifier.height(28.dp))

            // SUMMARY TITLE -------------------------------------------------------
            Text(
                "Report Summary",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(16.dp))

            // SUMMARY CARDS -------------------------------------------------------
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                SummaryCard("Total Present", "18", Color(0xFF22C55E))
                SummaryCard("Total Absent", "2", Color(0xFFFF9800))
            }

            Spacer(Modifier.height(14.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                SummaryCard("Total Late", "1", Color(0xFFFF9800))
                SummaryCard("Attendance %", "90%", MaterialTheme.colorScheme.primary)
            }

            Spacer(Modifier.height(28.dp))

            // BREAKDOWN TITLE -----------------------------------------------------
            Text(
                "Attendance Breakdown",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(14.dp))

            BreakdownCard()

            Spacer(Modifier.height(28.dp))

            // DETAILED RECORDS ----------------------------------------------------
            Text(
                "Detailed Records",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(16.dp))

            StudentRow("Eleanor Pena", "August 29, 2024", "Present", Color(0xFF22C55E))
            StudentRow("Cody Fisher", "August 28, 2024", "Absent", Color(0xFFFF9800))
            StudentRow("Jacob Jones", "August 27, 2024", "Late", Color(0xFFFF5722))

           // Spacer(Modifier.height(90.dp))
        }
}


// ------------------------------------------------------------------------
//                           COMPONENTS
// ------------------------------------------------------------------------

@Composable
fun FilterCard(title: String, value: String, icon: Int) {
    Column {
        Text(title, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)

        Spacer(Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.width(12.dp))
            Text(value, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
fun SummaryCard(title: String, value: String, color: Color) {
    Card(
        modifier = Modifier
            .width(165.dp)
            .height(95.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontSize = 14.sp)
            Spacer(Modifier.height(8.dp))
            Text(value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun BreakdownCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.padding(16.dp)) {

            // Bar ------------------------------------------------------------
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(22.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Box(Modifier
                    .weight(0.85f)
                    .background(Color(0xFF22C55E)))
                Box(Modifier
                    .weight(0.095f)
                    .background(Color(0xFFFF9800)))
                Box(Modifier
                    .weight(0.048f)
                    .background(Color(0xFFFF5722)))
            }

            Spacer(Modifier.height(16.dp))

            // Labels ---------------------------------------------------------
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier
                    .size(10.dp)
                    .background(Color(0xFF22C55E), CircleShape))
                Spacer(Modifier.width(8.dp))
                Text("Present (85.7%)")
            }

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier
                    .size(10.dp)
                    .background(Color(0xFFFF9800), CircleShape))
                Spacer(Modifier.width(8.dp))
                Text("Absent (9.5%)")
            }

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier
                    .size(10.dp)
                    .background(Color(0xFFFF5722), CircleShape))
                Spacer(Modifier.width(8.dp))
                Text("Late (4.8%)")
            }
        }
    }
}

@Composable
fun StudentRow(name: String, date: String, status: String, color: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            Modifier.padding(16.dp),
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
                Text(name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(date, fontSize = 13.sp)
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(color.copy(alpha = 0.15f))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(status, color = color, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ------------------------------------------------------------------------
//                          PREVIEWS
// ------------------------------------------------------------------------
@Preview(showSystemUi = true, name = "Light Mode")
@Composable
fun PreviewLight() {
    AttendanceTrackerTheme(useDarkTheme = false){
        AttendanceReportScreen()
    }
}

@Preview(showSystemUi = true, name = "Dark Mode")
@Composable
fun PreviewDark() {
    AttendanceTrackerTheme(useDarkTheme = true) {
        AttendanceReportScreen()
    }
}
