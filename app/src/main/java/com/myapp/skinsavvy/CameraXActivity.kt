package com.myapp.skinsavvy

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.myapp.skinsavvy.databinding.ActivityCameraXactivityBinding
import com.yalantis.ucrop.UCrop
import java.io.File
import java.util.UUID

class CameraXActivity : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null
    private lateinit var binding: ActivityCameraXactivityBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    private lateinit var imageClassifierHelper: ImageClassification
    private var result: String? = null
    private var currentImageUri: Uri? = null


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraXactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.switchCamera.setOnClickListener{
            startCamera()
        }
        binding.captureImage.setOnClickListener{
            takePhoto()
        }

        binding.switchCamera.setOnClickListener{
            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }


        binding.gallery.setOnClickListener {
            startGallery()
        }

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
    }
    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    override fun onPause() {
        super.onPause()
        stopCamera()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val cropUri = Uri.fromFile(File(cacheDir, "cropped_image_${UUID.randomUUID()}.jpg"))

            UCrop.of(uri, cropUri)
                .withAspectRatio(16F, 16F)
                .withMaxResultSize(2000, 2000)
                .start(this)

            currentImageUri = uri
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultCropped = UCrop.getOutput(data!!)
            currentImageUri = resultCropped
            Log.d("crop success", "Cropping success: $resultCropped")
            analyzeImage()
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            Log.e("crop failed", "Cropping failed: $cropError")
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createCustomTempFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                @RequiresApi(Build.VERSION_CODES.P)
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    currentImageUri = Uri.fromFile(photoFile)
                    startCrop(currentImageUri!!)
                }
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraXActivity,
                        "Gagal mengambil gambar.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "onError: ${exc.message}")
                }
            }
        )
    }

    private fun startCrop(uri: Uri) {
        val cropUri = Uri.fromFile(File(cacheDir, "cropped_image_${UUID.randomUUID()}.jpg"))

        UCrop.of(uri, cropUri)
            .withAspectRatio(16F, 16F)
            .withMaxResultSize(2000, 2000)
            .start(this@CameraXActivity)
    }



    @RequiresApi(Build.VERSION_CODES.P)
    private fun analyzeImage() {
        imageClassifierHelper = ImageClassification(
            context = this,
            classifierListener = object : ImageClassification.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        Toast.makeText(this@CameraXActivity, error, Toast.LENGTH_SHORT).show()
                        hideProgressBar()
                    }
                }

                override fun onResults(results: List<String>?, inferenceTime: Long) {
                    runOnUiThread {
                        results?.let {
                            result = it.joinToString("\n")
                            // Cetak hasil klasifikasi dan waktu inferensi ke log
                            Log.d("Image Classification", "Result: $result")
                            Log.d("Image Classification", "Inference Time: $inferenceTime ms")
                            hideProgressBar()
                            moveToResult()
                        }
                    }
                }
            }
        )
        currentImageUri?.let { this.imageClassifierHelper.classifyStaticImage(it) }
    }



    private fun moveToResult() {
        val intent = Intent(this, DetectionResultActivity::class.java).apply {
            putExtra(DetectionResultActivity.EXTRA_IMAGE, currentImageUri.toString())
            putExtra(DetectionResultActivity.EXTRA_RESULT, result)
        }
        startActivity(intent)
    }


    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun startCamera() {
        try {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                    }
                imageCapture = ImageCapture.Builder().build()
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            }, ContextCompat.getMainExecutor(this))
        } catch (exc: Exception) {
            Log.e(TAG, "Error starting camera: ${exc.message}")
        }
    }

    private fun stopCamera() {
        try {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                cameraProvider.unbindAll()
            }, ContextCompat.getMainExecutor(this))
        } catch (exc: Exception) {
            Log.e(TAG, "Error stopping camera: ${exc.message}")
        }
    }

    private fun releaseCamera() {
        try {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                cameraProvider.unbindAll()
            }, ContextCompat.getMainExecutor(this))
        } catch (exc: Exception) {
            Log.e(TAG, "Error releasing camera: ${exc.message}")
        }
    }

    private val orientationEventListener by lazy {
        object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) {
                    return
                }
                val rotation = when (orientation) {
                    in 45 until 135 -> Surface.ROTATION_270
                    in 135 until 225 -> Surface.ROTATION_180
                    in 225 until 315 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                imageCapture?.targetRotation = rotation
            }
        }
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }
    override fun onDestroy() {
        super.onDestroy()
        releaseCamera()
    }

    private fun hideProgressBar() {
        View.GONE.also { binding.progressIndicator.visibility = it }
    }

    companion object {
        private const val TAG = "CameraActivity"
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}