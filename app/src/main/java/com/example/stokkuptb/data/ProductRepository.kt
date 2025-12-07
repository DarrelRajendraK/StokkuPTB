package com.example.stokkuptb.data

import kotlinx.coroutines.flow.Flow

class ProductRepository(
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao
) {

    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    // BARU: Fungsi get by ID
    fun getProductById(id: Long): Flow<Product> {
        return productDao.getProductById(id)
    }

    suspend fun insertProduct(product: Product) {
        productDao.insert(product)
    }

    // BARU: Fungsi update
    suspend fun updateProduct(product: Product) {
        productDao.update(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.delete(product)
    }

    // --- Kategori ---
    val allCategories: Flow<List<Category>> = categoryDao.getAllCategories()

    suspend fun insertCategory(category: Category) {
        categoryDao.insert(category)
    }

    suspend fun deleteCategory(category: Category) {
        categoryDao.delete(category)
    }
}