package com.example.stokkuptb

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stokkuptb.viewmodel.ProductViewModel

@Composable
fun AddProductScreen(
    navController: NavController? = null, // Ditambahkan
    viewModel: ProductViewModel? = null   // Ditambahkan
) {
    var namaProduk by remember { mutableStateOf("") }
    var stok by remember { mutableStateOf("") }
    var harga by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Placeholder Gambar Besar (Tetap sama)
        Box(
            modifier = Modifier
                .size(150.dp)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                contentDescription = "Upload Gambar",
                modifier = Modifier.size(100.dp),
                tint = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Form "Detail Produk" (Tetap sama)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Detail Produk",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                CustomTextField(
                    value = namaProduk,
                    onValueChange = { namaProduk = it },
                    label = "Nama Produk"
                )
                Spacer(modifier = Modifier.height(8.dp))

                CustomTextField(
                    value = stok,
                    onValueChange = { stok = it },
                    label = "Stok",
                    keyboardType = KeyboardType.Number
                )
                Spacer(modifier = Modifier.height(8.dp))

                CustomTextField(
                    value = harga,
                    onValueChange = { harga = it },
                    label = "Harga",
                    keyboardType = KeyboardType.Number
                )
                Spacer(modifier = Modifier.height(8.dp))

                CustomTextField(
                    value = kategori,
                    onValueChange = { kategori = it },
                    label = "Kategori"
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol Tambah (Logic dihubungkan disini)
        Button(
            onClick = {
                if (viewModel != null && navController != null) {
                    // Panggil fungsi simpan ke database
                    viewModel.addProduct(
                        name = namaProduk,
                        category = kategori,
                        stockStr = stok,
                        priceStr = harga
                    )
                    // Kembali ke halaman sebelumnya
                    navController.popBackStack()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Tambah", color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

// CustomTextField tetap sama seperti buatan teman Anda
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Gray
        )
    )
}

@Preview(showBackground = true)
@Composable
fun AddProductScreenPreview() {
    // Preview tetap aman
    AddProductScreen()
}