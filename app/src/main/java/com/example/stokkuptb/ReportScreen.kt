package com.example.stokkuptb

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.stokkuptb.utils.NotificationUtils
import com.example.stokkuptb.viewmodel.ProductViewModel

@Composable
fun ReportScreen(
    navController: NavController,
    viewModel: ProductViewModel
) {
    val context = LocalContext.current
    val allProducts by viewModel.products.collectAsState()
    val lowStockCount = allProducts.count { it.stock < 5 }
    val totalAssets = allProducts.sumOf { it.price * it.stock }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted && lowStockCount > 0) {
                NotificationUtils.showLowStockNotification(context, lowStockCount)
            }
        }
    )

    LaunchedEffect(lowStockCount) {
        if (lowStockCount > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val permissionCheck = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                )

                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    NotificationUtils.showLowStockNotification(context, lowStockCount)
                } else {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                NotificationUtils.showLowStockNotification(context, lowStockCount)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (lowStockCount > 0) {
            ReportAlertCard(
                message = "Perhatian: Ada $lowStockCount produk stok menipis (< 5)!",
                onClick = { /* Bisa diarahkan ke list produk */ }
            )
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Text("Semua stok produk aman.", color = Color(0xFF2E7D32))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Total Nilai Aset Stok", color = Color.DarkGray, fontSize = 16.sp)
                Text(
                    text = "Rp ${totalAssets.toLong()}",
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ReportAlertCard(message: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Peringatan",
                tint = Color.Red,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                color = Color.Red,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}