package com.prng.androidimageuploadcomponent.data

import com.prng.androidimageuploadcomponent.data.model.ImageUploadModel
import okhttp3.MultipartBody
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApi {

    @Headers("Accept:*/*")
    @Multipart
    @POST("api/v1/files/upload")
    suspend fun sendImageUpload(@Part file: MultipartBody.Part) : ImageUploadModel
}