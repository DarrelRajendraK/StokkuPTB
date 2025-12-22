package com.example.stokkuptb

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.stokkuptb.data.AppDatabase
import com.example.stokkuptb.data.ProductRepository
import com.example.stokkuptb.ui.theme.StokkuAppTheme
import com.example.stokkuptb.utils.NotificationUtils
import com.example.stokkuptb.viewmodel.ProductViewModel
import com.example.stokkuptb.viewmodel.ViewModelFactory

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Splash : Screen("splash", "Splash", Icons.Default.Home)
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Add : Screen("add", "Add", Icons.Default.AddShoppingCart)
    object ProductList : Screen("productList", "Produk", Icons.Default.ViewList)
    object Report : Screen("report", "Laporan", Icons.AutoMirrored.Filled.Article)
    object Search : Screen("search", "Cari", Icons.Default.Search)
    object Management : Screen("management", "Manajemen", Icons.Default.Category)
    object DetailProduct : Screen("detailProduct", "Detail", Icons.Default.Edit)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)
        val repo = ProductRepository(db.productDao(), db.categoryDao())
        val factory = ViewModelFactory(repo)

        NotificationUtils.createNotificationChannel(this)

        val destinationFromNotif = intent.getStringExtra("DESTINATION")

        setContent {
            StokkuAppTheme {
                MainAppScreen(factory, destinationFromNotif)
            }
        }
    }
}

@Composable
fun MainAppScreen(factory: ViewModelFactory? = null, startDestinationExtra: String? = null) {
    StokkuAppContent(factory, startDestinationExtra)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StokkuAppContent(factory: ViewModelFactory? = null, startDestinationExtra: String? = null) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val productViewModel: ProductViewModel = if (factory != null) {
        viewModel(factory = factory)
    } else {
        viewModel()
    }

    LaunchedEffect(startDestinationExtra) {
        if (startDestinationExtra == "product_list") {
            navController.navigate(Screen.ProductList.route) {
                popUpTo(Screen.Home.route)
            }
        }
    }

    val homeNavItems = listOf(Screen.Home)
    val productNavItems = listOf(Screen.Home, Screen.ProductList, Screen.Add)
    val reportNavItems = listOf(Screen.Home, Screen.Search, Screen.Management, Screen.Report)

    val isHomeScreen = currentRoute == Screen.Home.route
    val isProductModule = currentRoute == Screen.ProductList.route ||
            currentRoute == Screen.Add.route ||
            currentRoute?.startsWith(Screen.DetailProduct.route) == true
    val isReportModule = currentRoute == Screen.Report.route ||
            currentRoute == Screen.Search.route ||
            currentRoute == Screen.Management.route

    Scaffold(
        topBar = {
            when {
                isHomeScreen -> HomeTopBar()
                isReportModule -> HomeTopBar()
                isProductModule -> TeamTopBar()
                else -> {}
            }
        },
        bottomBar = {
            when {
                isHomeScreen -> MainBottomBar(navController, currentRoute, homeNavItems)
                isProductModule -> TeamBottomBar(navController, currentRoute, productNavItems)
                isReportModule -> MainBottomBar(navController, currentRoute, reportNavItems)
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

            composable(Screen.Add.route) {
                AddProductScreen(navController = navController, viewModel = productViewModel, productId = -1L)
            }

            composable(Screen.ProductList.route) {
                ProductListScreen(navController = navController, viewModel = productViewModel)
            }

            composable(Screen.Report.route) {
                ReportScreen(navController = navController, viewModel = productViewModel)
            }

            composable(Screen.Search.route) {
                SearchScreen(navController = navController, viewModel = productViewModel)
            }

            composable(Screen.Management.route) {
                ManagementReportScreen(navController, productViewModel)
            }

            composable(
                route = "detailProduct/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.LongType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getLong("productId") ?: -1L
                AddProductScreen(navController = navController, viewModel = productViewModel, productId = productId)
            }
        }
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
                label = { Text(screen.label) },
                alwaysShowLabel = true,
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                selected = isSelected,
                onClick = {
                    if (screen.route == Screen.Home.route && currentRoute == Screen.Home.route) {
                    } else {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Black,
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    unselectedTextColor = Color.White.copy(alpha = 0.7f)
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
            val isSelected = currentRoute == screen.route
            NavigationBarItem(
                label = { Text(screen.label) },
                alwaysShowLabel = true,
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Black,
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                )
            )
        }
    }
}