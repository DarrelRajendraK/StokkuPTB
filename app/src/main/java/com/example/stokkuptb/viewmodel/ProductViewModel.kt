package com.example.stokkuptb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stokkuptb.data.Category
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

    init { syncData() }

    fun syncData() {
        viewModelScope.launch { repository.refreshProductsFromCloud() }
    }

    fun getProductById(id: Long) = repository.getProductById(id)

    fun addProduct(name: String, category: String, stockStr: String, priceStr: String, imageUri: String?, imageBase64: String) {
        viewModelScope.launch {
            val stock = stockStr.toIntOrNull() ?: 0
            val price = priceStr.toDoubleOrNull() ?: 0.0
            if (name.isNotBlank()) {
                repository.addProductToCloud(name, category, stock, price, imageBase64)
            }
        }
    }

    fun updateProduct(id: Long, name: String, category: String, stockStr: String, priceStr: String, imageUri: String?) {
        viewModelScope.launch {
            val stock = stockStr.toIntOrNull() ?: 0
            val price = priceStr.toDoubleOrNull() ?: 0.0

            repository.updateProductInCloud(
                Product(id, name, category, stock, price, imageUri),
                ""
            )
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProductFromCloud(product)
        }
    }

    val categories = repository.allCategories.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addCategory(name: String) = viewModelScope.launch {
        if (name.isNotBlank()) repository.insertCategory(Category(name = name))
    }

    fun updateCategory(category: Category, newName: String) = viewModelScope.launch {
        repository.updateCategoryWithProducts(category, newName)
    }

    fun deleteCategory(category: Category) = viewModelScope.launch {
        repository.deleteCategory(category)
    }
}