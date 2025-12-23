package com.example.stokkuptb.data

import kotlinx.coroutines.flow.Flow

class ProductRepository(
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao
) {

    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    fun getProductById(id: Long): Flow<Product> = productDao.getProductById(id)

    suspend fun insertProduct(product: Product) = productDao.insert(product)

    suspend fun updateProduct(product: Product) = productDao.update(product)

    suspend fun deleteProduct(product: Product) = productDao.delete(product)

    val allCategories: Flow<List<Category>> = categoryDao.getAllCategories()

    suspend fun insertCategory(category: Category) = categoryDao.insert(category)

    suspend fun deleteCategory(category: Category) = categoryDao.delete(category)

    suspend fun updateCategoryWithProducts(category: Category, newName: String) {
        val oldName = category.name

        categoryDao.update(category.copy(name = newName))

        productDao.updateCategoryName(oldName, newName)
    }
}