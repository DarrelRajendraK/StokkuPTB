package com.example.stokkuptb.data
import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: List<ProductRemote>
)

data class ProductRemote(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("category") val category: String,
    @SerializedName("stock") val stock: Int,
    @SerializedName("price") val price: Double,
    @SerializedName("image_url") val imageUrl: String
)

data class BasicResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String
)