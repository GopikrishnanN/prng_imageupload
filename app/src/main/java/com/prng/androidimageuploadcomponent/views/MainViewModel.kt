package com.prng.androidimageuploadcomponent.views

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prng.androidimageuploadcomponent.domain.AuthRepository
import com.prng.androidimageuploadcomponent.util.CountingRequestBody
import kotlinx.coroutines.launch
import java.io.File

class MainViewModel constructor(private val authRepository: AuthRepository) : ViewModel() {

    val fetchImageUpload = authRepository.imageUpload

    fun sendImageUpload(file: File, context: Context, listener: CountingRequestBody.Listener) {
        viewModelScope.launch {
            authRepository.sendImageUpload(file, context, listener)
        }
    }

}