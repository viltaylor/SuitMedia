package com.example.test1.network

import com.example.test1.BuildConfig // Import BuildConfig to access the API Key
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // NOTE: When you use a real API, you will change this URL.
    private const val BASE_URL = "https://reqres.in/api/"

    // --- This is the interceptor that adds the API Key to every request ---
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()

        // Get the API key securely from the BuildConfig you set up
        val apiKey = BuildConfig.API_KEY

        // Add the API Key as a header. Check your API's documentation for the correct header name.
        // Common examples are "X-Api-Key" or "Authorization".
        val requestBuilder = originalRequest.newBuilder()
            .header("X-Api-Key", apiKey)
        // If your API uses a Bearer Token, it would look like this:
        // .header("Authorization", "Bearer $apiKey")

        val newRequest = requestBuilder.build()
        chain.proceed(newRequest)
    }

    // --- This is the logging interceptor for debugging ---
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // --- Build the OkHttpClient, including BOTH interceptors ---
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // The logger
        .addInterceptor(authInterceptor)    // The API key interceptor
        .build()

    // --- Create the single Retrofit instance using our custom client ---
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient) // Use the client that has our interceptors
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // --- Create the lazy-initialized ApiService that the rest of your app will use ---
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}