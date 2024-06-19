package com.myapp.skinsavvy.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import androidx.annotation.RequiresApi
import com.myapp.skinsavvy.R
import com.myapp.skinsavvy.ml.ModelSkinSavvy
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class ImageClassification(
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    private var imageClassifier: ModelSkinSavvy? = null

    private val classLabels = listOf("Clear", "Level 0", "Level 1", "Level 2")

    init {
        setupImageClassifier()
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: List<String>?,
            inferenceTime: Long
        )
    }

    private fun setupImageClassifier() {
        try {
            imageClassifier = ModelSkinSavvy.newInstance(context)
        } catch (e: IllegalStateException) {
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun classifyStaticImage(imageUri: Uri) {
        if (imageClassifier == null) {
            setupImageClassifier()
        }

        val bitmap = toBitmap(imageUri)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)

        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(resizedBitmap)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(tensorImage.buffer)

        var inferenceTime = SystemClock.uptimeMillis()
        val outputs = imageClassifier?.process(inputFeature0)
        inferenceTime = SystemClock.uptimeMillis() - inferenceTime

        val outputFeature0 = outputs?.outputFeature0AsTensorBuffer

        val result = outputFeature0?.floatArray?.let {
            val maxIndex = it.indices.maxByOrNull { index -> it[index] } ?: -1
            if (maxIndex != -1) {
                classLabels[maxIndex]
            } else {
                "Unknown"
            }
        }

        classifierListener?.onResults(result?.let { listOf(it) }, inferenceTime)
        imageClassifier?.close()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun toBitmap(imageUri: Uri): Bitmap {
        val source = ImageDecoder.createSource(context.contentResolver, imageUri)
        val bitmap = ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
            decoder.setTargetColorSpace(android.graphics.ColorSpace.get(android.graphics.ColorSpace.Named.SRGB))
        }

        return if (bitmap.config == Bitmap.Config.ARGB_8888) {
            bitmap
        } else {
            bitmap.copy(Bitmap.Config.ARGB_8888, true)
        }
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }
}