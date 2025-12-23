package com.example.stokkuptb.data

object DataMapper {
    fun mapRemoteToEntity(remote: List<ProductRemote>): List<Product> {
        return remote.map {
            Product(
                id = it.id.toLongOrNull() ?: 0L,
                name = it.name,
                category = it.category,
                stock = it.stock,
                price = it.price,
                imageUri = it.imageUrl
            )
        }
    }
}