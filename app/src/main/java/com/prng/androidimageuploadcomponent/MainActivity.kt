package com.prng.androidimageuploadcomponent

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.prng.androidimageuploadcomponent.data.model.ImageUploadModel
import com.prng.androidimageuploadcomponent.databinding.ActivityMainBinding
import com.prng.androidimageuploadcomponent.util.CountingRequestBody
import com.prng.androidimageuploadcomponent.util.toast
import com.prng.androidimageuploadcomponent.views.MainViewModel
import com.prng.prngimageuploadcomponent.util.OnClickListener
import java.io.File

class MainActivity : AppCompatActivity(), CountingRequestBody.Listener {

    private val viewModel: MainViewModel by lazy { MainViewModel(MyApplication.appModule.authRepository) }

    private lateinit var binding: ActivityMainBinding

    private var value = 0
    private var handle = Handler(Looper.myLooper()!!)

    private var r = Runnable {
        updateTable()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel

        val observer = Observer<ImageUploadModel> { data ->
            if (data.location.isNotEmpty())
                toast(data.location)
        }

        viewModel.fetchImageUpload.observe(this, observer)

        binding.iuComponent.onClickListener(object : OnClickListener {
            override fun onClickListener(uri: Uri) {
                uri.path?.let { File(it) }?.let {
                    // Dummy Api
                    viewModel.sendImageUpload(it, applicationContext, this@MainActivity)
                    updateTable()
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    override fun onRequestProgress(bytesWritten: Long, contentLength: Long) {
        val pc = (bytesWritten * 1f / contentLength * 100).toInt()

        Log.e("TAG", "onRequestProgress: $pc")
    }

    // Dummy Progress
    @SuppressLint("SetTextI18n")
    private fun updateTable() {
        binding.atvProgressCounter.visibility = View.VISIBLE
        val percentages = resources.getString(R.string.percentages)
        binding.atvProgressCounter.text = "$percentages$value%"
        value += 5
        handle.postDelayed(r, 200)
        if (value == 100) {
            handle.removeCallbacks(r)
            binding.atvProgressCounter.text = "Image Uploaded"
            Handler(Looper.myLooper()!!).postDelayed({
                binding.atvProgressCounter.visibility = View.GONE
                value = 0
            }, 2000)
        }
    }
}