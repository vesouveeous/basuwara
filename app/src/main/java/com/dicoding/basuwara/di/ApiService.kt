package com.dicoding.basuwara.di

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("prediction")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
    ): Response<UploadResponse>

    data class UploadResponse(
        val error: String?,
        val result: String?
    )
}
