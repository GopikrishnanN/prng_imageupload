package com.prng.androidimageuploadcomponent.data

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.prng.androidimageuploadcomponent.data.model.ImageUploadModel
import com.prng.androidimageuploadcomponent.domain.AuthRepository
import com.prng.androidimageuploadcomponent.util.CountingRequestBody
import com.prng.androidimageuploadcomponent.util.createProgressiveStream
import okhttp3.MultipartBody
import java.io.File

class AuthRepositoryImpl(private val api: AuthApi) : AuthRepository {

    override val imageUpload: MutableLiveData<ImageUploadModel> by lazy {
        MutableLiveData<ImageUploadModel>()
    }

    override suspend fun sendImageUpload(
        file: File,
        context: Context,
        listener: CountingRequestBody.Listener
    ) {
        try {
            val filePath: MultipartBody.Part = createProgressiveStream(
                context,
                "files",
                Uri.fromFile(file),
                listener
            )!!

            imageUpload.value = api.sendImageUpload(file = filePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}