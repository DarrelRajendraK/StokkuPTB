package com.example.stokkuptb.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://script.google.com/macros/s/AKfycbwA-IiVfL81PkKAsZBTkcF_RixGX7Z4tHeTKvKKbNvHRr2NO5Fj4nuxWEB57cr8SNPHig/exec/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}