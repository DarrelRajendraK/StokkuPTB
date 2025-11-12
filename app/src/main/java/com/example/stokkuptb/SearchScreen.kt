package com.example.stokkuptb

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.stokkuptb.ui.theme.StokkuAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var filterSelected by remember { mutableStateOf(false) }

    var showFilterDialog by remember { mutableStateOf(false) }

    if (showFilterDialog) {
        CategoryFilterDialog(onDismiss = { showFilterDialog = false })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Cari Produk") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Cari") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF0F0F0),
                focusedContainerColor = Color.White,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Red
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
        )

        Spacer(modifier = Modifier.height(8.dp))

        FilterChip(
            selected = filterSelected,
            onClick = { showFilterDialog = true },
            label = { Text("Filter") },
            leadingIcon = { Icon(Icons.Default.FilterAlt, contentDescription = "Filter") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(4) {
                ProductGridItem(navController = navController)
            }
        }
    }
}

@Composable
fun ProductGridItem(navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.DarkGray)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Nama", style = MaterialTheme.typography.bodyLarge)

                IconButton(
                    onClick = {
                        navController.navigate(Screen.DetailProduct.route)
                    }
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit Produk"
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryFilterDialog(onDismiss: () -> Unit) {
    val categories = listOf(
        "Kategori 1", "Kategori 2", "Kategori 3",
        "Kategori 4", "Kategori 5", "Kategori 6"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pilih Kategori") },
        text = {
            LazyColumn {
                items(categories) { category ->
                    Text(
                        text = category,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // TODO: Terapkan logika filter di sini
                                onDismiss()
                            }
                            .padding(vertical = 12.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup")
            }
        }
    )
}

@Preview(name = "Search Screen Content", showBackground = true)
@Composable
fun SearchScreenContentPreview() {
    StokkuAppTheme {
        SearchScreen(navController = rememberNavController())
    }
}