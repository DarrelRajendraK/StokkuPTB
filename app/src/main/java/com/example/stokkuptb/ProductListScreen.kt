package com.example.stokkuptb

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.stokkuptb.ui.theme.StokkuAppTheme
import com.example.stokkuptb.viewmodel.ProductViewModel

@Composable
fun ProductListScreen(
    navController: NavController? = null,
    viewModel: ProductViewModel? = null
) {
    // Ambil data LIVE dari database
    val productList = viewModel?.products?.collectAsState()?.value ?: emptyList()

    Column(modifier = Modifier.fillMaxSize()) {
        // Judul Halaman
        Text(
            text = "Daftar Produk",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        if (productList.isEmpty()) {
            // Tampilan jika data kosong
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Belum ada data produk", color = Color.Gray)
            }
        } else {
            // Daftar Item dari Database
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(productList) { product ->
                    ProductListItem(
                        namaProduk = product.name,
                        // Format harga sederhana
                        harga = "Rp ${product.price.toLong()}",
                        // Stok ditampilkan juga agar informatif
                        stok = product.stock,
                        onDelete = { viewModel?.deleteProduct(product) },
                        onEdit = { /* Nanti bisa tambahkan fitur edit disini */ }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductListItem(
    namaProduk: String,
    harga: String,
    stok: Int,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Placeholder Gambar (Tetap sama)
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "Gambar Produk",
                    modifier = Modifier.size(60.dp),
                    tint = Color.Gray
                )
            }

            // Kolom Teks (Nama & Harga & Stok)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = namaProduk,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = harga,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Stok: $stok",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Kolom Ikon (Edit & Hapus)
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.height(80.dp)
            ) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Outlined.Edit, contentDescription = "Edit", tint = Color.Gray)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListScreenPreview() {
    StokkuAppTheme {
        ProductListScreen()
    }
}