package com.prng.androidimageuploadcomponent.util

import android.content.Context
import android.net.Uri
import android.widget.Toast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

fun Context.toast(msg: String) {
    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
}

fun createProgressiveStream(
    context: Context?,
    partName: String?,
    fileUri: Uri?,
    listener: CountingRequestBody.Listener?,
): MultipartBody.Part? {
    val file = File(FileUtils.getPathFromUri(context, fileUri))
    val part: MultipartBody.Part =
        constructMultipart(context, partName, fileUri)!!
    val body = CountingRequestBody(
        part.body,
        listener
    )
    return partName?.let { MultipartBody.Part.createFormData(it, file.name, body) }
}

private fun constructMultipart(
    context: Context?,
    partName: String?,
    fileUri: Uri?,
): MultipartBody.Part? {
    val path = (if (context == null) null else FileUtils.getPathFromUri(context, fileUri))
        ?: return null
    val file = File(path)

    // create RequestBody instance from file
    var mimeType = context!!.contentResolver.getType(fileUri!!)
    if (mimeType == null) {
        //return null;
        mimeType = "image/jpeg"
    }
    val fileType = mimeType.toMediaTypeOrNull()
    val requestFile = RequestBody.create(
        fileType,
        file
    )
    return partName?.let { MultipartBody.Part.createFormData(it, file.name, requestFile) }
}