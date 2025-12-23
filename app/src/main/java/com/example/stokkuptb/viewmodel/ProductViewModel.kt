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

    fun getProductById(id: Long) = repository.getProductById(id)

    fun addProduct(name: String, category: String, stockStr: String, priceStr: String, imageUri: String?) {
        viewModelScope.launch {
            val stock = stockStr.toIntOrNull() ?: 0
            val price = priceStr.toDoubleOrNull() ?: 0.0
            if (name.isNotBlank()) {
                repository.insertProduct(
                    Product(name = name, category = category, stock = stock, price = price, imageUri = imageUri)
                )
            }
        }
    }

    fun updateProduct(id: Long, name: String, category: String, stockStr: String, priceStr: String, imageUri: String?) {
        viewModelScope.launch {
            val stock = stockStr.toIntOrNull() ?: 0
            val price = priceStr.toDoubleOrNull() ?: 0.0
            if (name.isNotBlank()) {
                repository.updateProduct(
                    Product(id = id, name = name, category = category, stock = stock, price = price, imageUri = imageUri)
                )
            }
        }
    }

    fun deleteProduct(product: Product) = viewModelScope.launch { repository.deleteProduct(product) }

    val categories: StateFlow<List<Category>> = repository.allCategories.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList()
    )

    fun addCategory(name: String) = viewModelScope.launch {
        if (name.isNotBlank()) repository.insertCategory(Category(name = name))
    }

    fun updateCategory(category: Category, newName: String) {
        viewModelScope.launch {
            if (newName.isNotBlank()) {
                repository.updateCategoryWithProducts(category, newName)
            }
        }
    }

    fun deleteCategory(category: Category) = viewModelScope.launch { repository.deleteCategory(category) }
}