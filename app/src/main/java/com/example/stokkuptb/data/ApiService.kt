package com.example.stokkuptb.data

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("exec")
    suspend fun getProducts(@Query("action") action: String = "read"): ProductResponse

    @FormUrlEncoded
    @POST("exec")
    suspend fun addProduct(
        @Field("action") action: String = "insert",
        @Field("name") name: String,
        @Field("category") category: String,
        @Field("stock") stock: Int,
        @Field("price") price: Double,
        @Field("image") imageBase64: String
    ): BasicResponse

    @FormUrlEncoded
    @POST("exec")
    suspend fun deleteProduct(
        @Field("action") action: String = "delete",
        @Field("id") id: String
    ): BasicResponse

    @FormUrlEncoded
    @POST("exec")
    suspend fun updateProduct(
        @Field("action") action: String = "update",
        @Field("id") id: String,
        @Field("name") name: String,
        @Field("category") category: String,
        @Field("stock") stock: Int,
        @Field("price") price: Double,
        @Field("image") imageBase64: String
    ): BasicResponse

    @FormUrlEncoded
    @POST("exec")
    suspend fun updateCategoryBatch(
        @Field("action") action: String = "update_category_batch",
        @Field("old_name") oldName: String,
        @Field("new_name") newName: String
    ): BasicResponse
}