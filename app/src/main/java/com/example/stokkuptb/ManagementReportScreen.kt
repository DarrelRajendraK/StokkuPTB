package com.example.stokkuptb

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stokkuptb.viewmodel.ProductViewModel
import com.example.stokkuptb.data.Category

@Composable
fun ManagementReportScreen(
    navController: NavController,
    viewModel: ProductViewModel? = null
) {
    var newCategoryText by remember { mutableStateOf("") }

    val categories = viewModel?.categories?.collectAsState()?.value ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Kelola Kategori",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = newCategoryText,
            onValueChange = { newCategoryText = it },
            placeholder = { Text("Nama Kategori Baru") },
            trailingIcon = {
                IconButton(onClick = {
                    viewModel?.addCategory(newCategoryText)
                    newCategoryText = ""
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Tambah Kategori")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryItemCard(
                    category = category,
                    onDelete = { viewModel?.deleteCategory(category) }
                )
            }
        }
    }
}

@Composable
fun CategoryItemCard(category: Category, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Warning, contentDescription = "Ikon", tint = Color.Black)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = category.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.Red)
            }
        }
    }
}