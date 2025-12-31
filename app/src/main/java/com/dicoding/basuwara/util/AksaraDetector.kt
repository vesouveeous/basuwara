package com.dicoding.basuwara.util

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.FileUtil
import java.nio.ByteBuffer

class AksaraClassifier(context: Context) {

    private val interpreter: Interpreter
    private val labels: List<String>
    private val imageProcessor: ImageProcessor

    init {
        // Load model
        val modelBuffer: ByteBuffer = FileUtil.loadMappedFile(context, "ModelFinal.tflite")
        interpreter = Interpreter(modelBuffer)

        // Load label
        labels = FileUtil.loadLabels(context, "labels.txt")

        // Image processor (resize to 150x150 + normalize)
        imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(150, 150, ResizeOp.ResizeMethod.BILINEAR))
            .add(NormalizeOp(0f, 255f)) // normalize to 0–1
            .build()
    }

    fun classify(bitmap: Bitmap): String {
        val tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)
        val processedImage = imageProcessor.process(tensorImage)

        val inputBuffer = processedImage.buffer

        // Output buffer untuk 68 kelas
        val outputBuffer = TensorBuffer.createFixedSize(intArrayOf(1, labels.size), DataType.FLOAT32)

        // Jalankan model
        interpreter.run(inputBuffer, outputBuffer.buffer.rewind())

        // Ambil hasil tertinggi
        val scores = outputBuffer.floatArray
        val maxIndex = scores.indices.maxByOrNull { scores[it] } ?: -1

        return labels[maxIndex]
    }
}
