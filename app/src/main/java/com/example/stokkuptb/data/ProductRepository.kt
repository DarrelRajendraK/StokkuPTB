package com.example.stokkuptb.data

import kotlinx.coroutines.flow.Flow

class ProductRepository(
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao
) {
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    suspend fun insertProduct(product: Product) {
        productDao.insert(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.delete(product)
    }

    val allCategories: Flow<List<Category>> = categoryDao.getAllCategories()

    suspend fun insertCategory(category: Category) {
        categoryDao.insert(category)
    }

    suspend fun deleteCategory(category: Category) {
        categoryDao.delete(category)
    }
}