package com.prng.prngimageuploadcomponent

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.prng.prngimageuploadcomponent.file_paths.URIPathHelper
import java.io.File
import java.util.*

class ImageUploadComponent @JvmOverloads constructor(
    activity: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(activity.applicationContext, attrs, defStyleAttr) {

    private val pickGalleryImage: AppCompatImageView
    private val submitButton: AppCompatButton
    private val imageView: AppCompatImageView
    private val fileInfoTextView: AppCompatTextView
    private val aivSelectImageView: AppCompatImageView
    private val aivPreviewImageView: AppCompatImageView
    private val atvSelectPhoto: AppCompatTextView
    private val llcGetImagePreview: LinearLayoutCompat
    private var mFullScreenDialog: Dialog? = null

    private lateinit var currentPhotoPath: String
    private var selectedBitmap: Bitmap? = null

    private var onClick: com.prng.prngimageuploadcomponent.util.OnClickListener? = null
    private var currentPhotoUri: Uri? = null

    private val takePictureLauncher =
        (activity as ComponentActivity).registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val uriPathHelper = URIPathHelper()
                currentPhotoUri = uri
                currentPhotoPath = uriPathHelper.getPath(context, uri).toString()
                displayImagePreview()
            }
        }

    private val lowLevelPermission =
        arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    @RequiresApi(Build.VERSION_CODES.Q)
    private val latestLevelPermission = arrayOf(Manifest.permission.ACCESS_MEDIA_LOCATION)

    private val multiplePermission =
        (activity as ComponentActivity).registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsStatusMap ->
            if (!permissionsStatusMap.containsValue(false)) {
                dataVisibility()

                selectImage()
            } else {
                fetchPermissions()
            }
        }

    init {
        inflate(context, R.layout.image_upload_component, this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_TIME)
        pickGalleryImage = findViewById(R.id.pickGalleryImage)
        submitButton = findViewById(R.id.submitButton)
        imageView = findViewById(R.id.imageView)
        fileInfoTextView = findViewById(R.id.fileInfoTextView)

        aivSelectImageView = findViewById(R.id.aivSelectImageView)
        aivPreviewImageView = findViewById(R.id.aivPreviewImageView)
        atvSelectPhoto = findViewById(R.id.atvSelectPhoto)

        llcGetImagePreview = findViewById(R.id.llcGetImagePreview)

        pickGalleryImage.setOnClickListener { selectImage() }
        submitButton.setOnClickListener {
            submitImage()
                submitButton.visibility = View.GONE
            Handler(Looper.myLooper()!!).postDelayed({
                submitButton.visibility = View.VISIBLE
            }, 4000)
        }
        imageView.setOnClickListener { fetchPermission() }
        aivPreviewImageView.setOnClickListener {
            if (isImageData()) {
                openFullScreenDialog(activity)
            }
        }

        dataVisibility()
    }

    private fun fetchPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            multiplePermission.launch(latestLevelPermission)
        } else {
            multiplePermission.launch(lowLevelPermission)
        }
    }

    private fun selectImage() {
        takePictureLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    @SuppressLint("SetTextI18n")
    private fun displayImagePreview() {
        if (::currentPhotoPath.isInitialized && currentPhotoPath.isNotEmpty()) {
            selectedBitmap = BitmapFactory.decodeFile(currentPhotoPath)
            if (currentPhotoUri != null) {
                Glide.with(context).load(currentPhotoUri).into(imageView)
            }
            val file = File(currentPhotoPath)
            fileInfoTextView.text = "File Name: ${file.name}\nFile Type: ${
                file.extension.uppercase(Locale.ROOT)
            }"
            dataVisibility()
        }
    }

    private fun submitImage() {
        if (currentPhotoUri != null) {
            println("Image submitted: ${currentPhotoUri?.path}")
            val file = File(currentPhotoPath)
            onClick?.onClickListener(file.toUri())
        }
    }

    private fun fetchPermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val media: Int =
                ActivityCompat.checkSelfPermission(context, latestLevelPermission[0])
            if (media != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context as Activity, latestLevelPermission, 1)
                return true
            }
        } else {
            val permission: Int =
                ActivityCompat.checkSelfPermission(context, lowLevelPermission[1])
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context as Activity, lowLevelPermission, 1)
                return true
            }
        }
        return false
    }

    private fun isVerifyPermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val media: Int =
                ActivityCompat.checkSelfPermission(context, latestLevelPermission[0])
            if (media == PackageManager.PERMISSION_GRANTED) {
                return true
            }
        } else {
            val permission: Int =
                ActivityCompat.checkSelfPermission(context, lowLevelPermission[1])
            if (permission == PackageManager.PERMISSION_GRANTED) {
                return true
            }
        }
        return false
    }

    private fun dataVisibility() {
        if (isImageData()) {
            aivSelectImageView.visibility = View.GONE
            aivPreviewImageView.visibility = View.VISIBLE
            atvSelectPhoto.visibility = View.GONE
            fileInfoTextView.visibility = View.VISIBLE
            submitButton.visibility = View.VISIBLE
        } else {
            aivSelectImageView.visibility = View.VISIBLE
            aivPreviewImageView.visibility = View.GONE
            atvSelectPhoto.visibility = View.VISIBLE
            fileInfoTextView.visibility = View.GONE
            submitButton.visibility = View.GONE
        }
    }

    private fun isImageData(): Boolean {
        return currentPhotoUri != null
    }

    fun onClickListener(onClickListener: com.prng.prngimageuploadcomponent.util.OnClickListener) {
        onClick = onClickListener
    }

    private fun openFullScreenDialog(context: Context) {
        val matchParent = WindowManager.LayoutParams.MATCH_PARENT

        mFullScreenDialog = Dialog(context as Activity)
        mFullScreenDialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        mFullScreenDialog?.setContentView(R.layout.preview_full_screen_image_layout)
        mFullScreenDialog?.window?.setLayout(matchParent, matchParent)
        mFullScreenDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.BLACK))
        mFullScreenDialog?.setCanceledOnTouchOutside(false)
        mFullScreenDialog?.setCancelable(true)

        val imageView = mFullScreenDialog?.findViewById<AppCompatImageView>(R.id.imageView)
        val aivPreviewImageView =
            mFullScreenDialog?.findViewById<AppCompatImageView>(R.id.aivPreviewImageView)

        if (currentPhotoUri != null) {
            Glide.with(context).load(currentPhotoUri).into(imageView!!)
        }

        aivPreviewImageView?.setOnClickListener {
            if (mFullScreenDialog?.isShowing!!)
                mFullScreenDialog?.dismiss()
        }

        mFullScreenDialog?.show()
    }
}
