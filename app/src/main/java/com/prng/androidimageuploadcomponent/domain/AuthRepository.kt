package com.prng.androidimageuploadcomponent.domain

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.prng.androidimageuploadcomponent.data.model.ImageUploadModel
import com.prng.androidimageuploadcomponent.util.CountingRequestBody
import java.io.File

interface AuthRepository {
    val imageUpload: MutableLiveData<ImageUploadModel>

    suspend fun sendImageUpload(
        file: File,
        context: Context,
        listener: CountingRequestBody.Listener
    )
}