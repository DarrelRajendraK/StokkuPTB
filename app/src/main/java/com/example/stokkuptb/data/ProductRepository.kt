package com.example.stokkuptb.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ProductRepository(
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao,
    private val apiService: ApiService
) {
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    suspend fun refreshProductsFromCloud() {
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getProducts()
                if (response.status == "success") {
                    val newProducts = DataMapper.mapRemoteToEntity(response.data)
                    productDao.deleteAllForSync()
                    productDao.insertAll(newProducts)
                }
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    suspend fun addProductToCloud(name: String, category: String, stock: Int, price: Double, imageBase64: String) {
        withContext(Dispatchers.IO) {
            try {
                apiService.addProduct(action="insert", name=name, category=category, stock=stock, price=price, imageBase64=imageBase64)
                refreshProductsFromCloud()
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    suspend fun updateProductInCloud(product: Product, imageBase64: String) {

        productDao.update(product)

        withContext(Dispatchers.IO) {
            try {
                apiService.updateProduct(
                    id = product.id.toString(),
                    name = product.name,
                    category = product.category,
                    stock = product.stock,
                    price = product.price,
                    imageBase64 = imageBase64
                )
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    suspend fun deleteProductFromCloud(product: Product) {

        productDao.delete(product)

        withContext(Dispatchers.IO) {
            try {
                apiService.deleteProduct(id = product.id.toString())
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    val allCategories: Flow<List<Category>> = categoryDao.getAllCategories()

    suspend fun insertCategory(category: Category) = categoryDao.insert(category)

    suspend fun deleteCategory(category: Category) = categoryDao.delete(category)

    suspend fun updateCategoryWithProducts(category: Category, newName: String) {
        val oldName = category.name

        categoryDao.update(category.copy(name = newName))
        productDao.updateCategoryName(oldName, newName)

        withContext(Dispatchers.IO) {
            try {
                apiService.updateCategoryBatch(oldName = oldName, newName = newName)
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    fun getProductById(id: Long): Flow<Product> = productDao.getProductById(id)
}