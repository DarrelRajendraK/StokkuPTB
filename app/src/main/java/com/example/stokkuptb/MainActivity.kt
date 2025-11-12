package com.example.stokkuptb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.stokkuptb.ui.theme.StokkuAppTheme

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Splash : Screen("splash", "Splash", Icons.Default.Home)
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Add : Screen("add", "Add", Icons.Default.AddShoppingCart)
    object Info : Screen("info", "Info", Icons.Default.Info)
    object ProductList : Screen("productList", "Produk", Icons.Default.Home)
    object Report : Screen("report", "Laporan", Icons.AutoMirrored.Filled.Article)
    object Search : Screen("search", "Cari", Icons.Default.Search)
    object Filter : Screen("filter", "Filter", Icons.Default.FilterAlt)
    object DetailProduct : Screen("detailProduct", "Detail", Icons.Default.Edit)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StokkuAppTheme {
                MainAppScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StokkuAppContent() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val teamNavItems = listOf(Screen.Home, Screen.Info, Screen.Add)
    val mainNavItems = listOf(Screen.Home, Screen.Search, Screen.Filter, Screen.Report)

    val showMainBars = mainNavItems.any { it.route == currentRoute }
    val showTeamBars = teamNavItems.any { it.route == currentRoute } && currentRoute != Screen.Home.route ||
            currentRoute == Screen.DetailProduct.route ||
            currentRoute == Screen.ProductList.route

    Scaffold(
        topBar = {
            when {
                showMainBars -> HomeTopBar()
                showTeamBars -> TeamTopBar()
                else -> {}
            }
        },
        bottomBar = {
            when {
                showMainBars -> MainBottomBar(navController, currentRoute, mainNavItems)
                showTeamBars -> TeamBottomBar(navController, currentRoute, teamNavItems)
                else -> {}
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Screen.Splash.route) { SplashScreen(navController) }
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Add.route) { AddProductScreen() }
            composable(Screen.Info.route) { Box(Modifier.padding(innerPadding)) { Text("Halaman Info") } }
            composable(Screen.ProductList.route) { ProductListScreen() }

            composable(Screen.Report.route) { ReportScreen(navController) }

            composable(Screen.Search.route) { SearchScreen(navController) }

            composable(Screen.Filter.route) {
                ManagementReportScreen(navController)
            }

            composable(Screen.DetailProduct.route) {
                AddProductScreen()
            }
        }
    }
}

@Composable
fun MainAppScreen() {
    StokkuAppContent()
}

@Preview(name = "Alur Aplikasi Penuh", showSystemUi = true)
@Composable
fun FullAppInteractivePreview() {
    StokkuAppTheme {
        StokkuAppContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    TopAppBar(
        title = { Text("STOKKU", fontWeight = FontWeight.Bold, color = Color.White) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Red)
    )
}

@Composable
fun MainBottomBar(navController: NavController, currentRoute: String?, navItems: List<Screen>) {
    NavigationBar(containerColor = Color.Red) {
        navItems.forEach { screen ->
            val isSelected = currentRoute == screen.route
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label, tint = Color.White) },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Red,
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f)
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamTopBar() {
    TopAppBar(
        title = { Text("STOKKU", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
    )
}

@Composable
fun TeamBottomBar(navController: NavController, currentRoute: String?, navItems: List<Screen>) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.primary) {
        navItems.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label, tint = MaterialTheme.colorScheme.onPrimary) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.secondary)
            )
        }
    }
}