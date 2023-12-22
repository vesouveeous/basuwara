package com.dicoding.basuwara.ui.screen.image

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.dicoding.basuwara.BuildConfig
import com.dicoding.basuwara.di.ApiService
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import androidx.lifecycle.LifecycleCoroutineScope
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Camera : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CameraContent()
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun CameraContent() {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        BuildConfig.APPLICATION_ID + ".provider", file
    )

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.example.com/") // Gantilah dengan URL API yang sesuai
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        AppContent(
            capturedImageUri = capturedImageUri,
            onCaptureButtonClick = {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(uri)
                } else {
                    // Request a permission
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            onUploadButtonClick = {
                onUploadButtonClick(capturedImageUri, apiService, context, lifecycleOwner.lifecycleScope)
            }
        )
    }
}

@Composable
fun AppContent(
    capturedImageUri: Uri,
    onCaptureButtonClick: () -> Unit,
    onUploadButtonClick: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { onCaptureButtonClick() }) {
            Text(text = "Capture Image From Camera")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button for Upload
        Button(onClick = {
            onUploadButtonClick()
        }) {
            Text(text = "Upload Image")
        }
    }

    if (capturedImageUri.path?.isNotEmpty() == true) {
        Image(
            modifier = Modifier
                .padding(16.dp, 8.dp),
            painter = rememberImagePainter(capturedImageUri),
            contentDescription = null
        )
    }
}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir      /* directory */
    )
    return image
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CameraContent()
}

private fun onUploadButtonClick(
    capturedImageUri: Uri,
    apiService: ApiService,

    context: Context,
    lifecycleScope: LifecycleCoroutineScope
) {
    // Tambahkan logika untuk upload di sini
    Toast.makeText(context, "Image Uploaded", Toast.LENGTH_SHORT).show()
    // Menggunakan ApiService yang telah diinisialisasi
    lifecycleScope.launch {
        try {
            val file = File(capturedImageUri.path!!)
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            val imageBody = MultipartBody.Part.createFormData("image", file.name, requestFile)

            val description = RequestBody.create("text/plain".toMediaTypeOrNull(), "Image Description")

            val response = apiService.uploadImage(imageBody, description)
            if (response.isSuccessful) {
                val uploadResponse = response.body()
                if (uploadResponse?.status == true) {
                    Toast.makeText(context, "Image Uploaded", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Upload Failed: ${uploadResponse?.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show()
        }
    }
}
