package com.dicoding.basuwara.di

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("upload")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response<UploadResponse> // Gantilah YourResponseModel dengan model respon yang sesuai

    data class UploadResponse(
        val status: Boolean = false,
        val message: String = ""
    )
}
