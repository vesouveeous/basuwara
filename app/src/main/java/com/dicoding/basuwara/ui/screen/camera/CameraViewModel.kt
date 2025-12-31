package com.dicoding.basuwara.ui.screen.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.dicoding.basuwara.util.AksaraClassifier
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
open class CameraViewModel @Inject constructor(
    private val classifier: AksaraClassifier
):ViewModel() {
    private val _classificationResult = MutableStateFlow("")
    val classificationResult: Flow<String> = _classificationResult

    fun classifyImage(bitmap: Bitmap) {
        val hasil = classifier.classify(bitmap)
        _classificationResult.value = hasil
    }
}