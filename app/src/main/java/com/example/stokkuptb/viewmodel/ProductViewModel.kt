package com.example.stokkuptb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stokkuptb.data.Product
import com.example.stokkuptb.data.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    val products: StateFlow<List<Product>> = repository.allProducts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    // Update fungsi ini untuk menerima priceStr
    fun addProduct(name: String, category: String, stockStr: String, priceStr: String) {
        viewModelScope.launch {
            val stock = stockStr.toIntOrNull() ?: 0
            val price = priceStr.toDoubleOrNull() ?: 0.0 // Konversi String ke Double

            if (name.isNotBlank()) {
                repository.insert(
                    Product(
                        name = name,
                        category = category,
                        stock = stock,
                        price = price
                    )
                )
            }
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.delete(product)
        }
    }
}