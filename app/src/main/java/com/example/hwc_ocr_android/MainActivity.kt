package com.example.hwc_ocr_android

import android.Manifest
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.drawToBitmap
import com.alibaba.fastjson.JSONObject
import com.example.hwc_ocr_android.databinding.ActivityMainBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var imageUri: Uri
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        with(binding) {
            setContentView(root)
            btnCam.setOnClickListener {
                val outputFile = File(cacheDir, "output.png")
                imageUri = FileProvider.getUriForFile(
                    Objects.requireNonNull(applicationContext),
                    BuildConfig.APPLICATION_ID + ".provider",
                    outputFile
                )
                txtResult.setText(imageUri.toString())
                requestCam.launch(Manifest.permission.CAMERA)

            }
            btnAlbum.setOnClickListener {
                requestAlbum.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

            }
        }
    }
    private val requestCam =
        registerForActivityResult( ActivityResultContracts.RequestPermission()) { isGranted ->
            isGranted?.let {
                takePicture.launch(imageUri)
            }
        }
    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isGranted ->
            isGranted?.let {
                binding.imageView.setImageURI(imageUri)
                akskOcrService()
            }
        }
    private val requestAlbum =
        registerForActivityResult( ActivityResultContracts.RequestPermission()) { isGranted ->
            isGranted?.let {
                selectImageFromGalleryResult.launch("image/*")
            }
        }
    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                binding.imageView.setImageURI(uri)
                akskOcrService()
            }
        }
    private fun akskOcrService() {
        // TODO: Set required parameters
        val ak = applicationContext.getString(R.string.ak)  // AK from authentication
        val sk = applicationContext.getString(R.string.sk) // SK from authentication
        val region = applicationContext.getString(R.string.region)
        // initialize HWOcrClientAKSK from ak,sk and endpoint information
        val hwOcrClientAKSK = HWOcrClientAKSK(this, ak, sk, region)
        // ocr service
        val uri = "/v2/0cc4b36f1600f4012f15c010af45922a/ocr/thailand-id-card"
        val bitmap = binding.imageView.drawToBitmap()
        binding.txtResult.setText("Reading...")

        // Set params except image
        val params = JSONObject()
        // params.put("country_code", "GENERAL");
        hwOcrClientAKSK.requestOcrAkskService(uri, bitmap, params, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                var result = e.toString()
                runOnUiThread {
                    binding.txtResult.setText(result)
                }
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                var result = response.body!!.string()
                runOnUiThread {
                    binding.txtResult.setText(result)
                }
            }
        })
    }

}