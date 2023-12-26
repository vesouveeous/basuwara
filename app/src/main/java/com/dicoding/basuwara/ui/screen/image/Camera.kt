package com.dicoding.basuwara.ui.screen.image

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
import com.dicoding.basuwara.R
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
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

@Composable
fun CameraContent() {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val uri = getImageUri(timeStamp, context)
    Log.i("Camera Content", "timestamp: $timeStamp")


    var capturedImageUri by remember {mutableStateOf<Uri>(Uri.EMPTY) }
    var isImageTaken by rememberSaveable { mutableStateOf(false) }
    var uploadResult by remember { mutableStateOf("") }

    val retrofit = Retrofit.Builder()
        .baseUrl("http://34.101.214.203:5000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {result ->
            if (result) {
                capturedImageUri = uri
                isImageTaken = true
            }
            else {
                Toast.makeText(context, "No picture taken", Toast.LENGTH_SHORT).show()
            }
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
        color = MaterialTheme.colorScheme.background
    ) {
        AppContent(
            isImageTaken = isImageTaken,
            capturedImageUri = capturedImageUri,
            uploadResult = uploadResult,
            context = context,
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
                lifecycleOwner.lifecycleScope.launch {
                    uploadResult = onUploadButtonClick(timeStamp, capturedImageUri, apiService, context)
                }
            }
        )
    }
}

@Composable
fun AppContent(
    isImageTaken: Boolean,
    capturedImageUri: Uri,
    uploadResult: String,
    context: Context,
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
            if (isImageTaken) {
                onUploadButtonClick()
            } else {
                Toast.makeText(context, "Capture image first", Toast.LENGTH_SHORT).show()
            }

        }) {
            Text(text = "Upload Image")
        }
        if (capturedImageUri.path?.isNotEmpty() == true) {
            Image(
                modifier = Modifier
                    .padding(16.dp, 8.dp),
                painter = rememberImagePainter(capturedImageUri),
                contentDescription = null
            )
        } else{
            Image(
                painter = painterResource(id = R.drawable.ic_image),
                contentDescription = "image",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
        if (uploadResult.isNotEmpty()) {
            Card(
                backgroundColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth().padding(8.dp, 16.dp)
            ) {
                Text(
                    text = uploadResult,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CameraContent()
}

private suspend fun onUploadButtonClick(
    timeStamp: String,
    capturedImageUri: Uri,
    apiService: ApiService,
    context: Context,
): String {
    Toast.makeText(context, "Uploading Image...", Toast.LENGTH_SHORT).show()
    return try {
        val file = uriToFile(timeStamp, capturedImageUri, context)
        Log.i("Konversi uri to file", "Path: ${file.path}")
        Log.i("Konversi uri to file", "Size: ${file.length()} bytes")

        val requestFile = file.asRequestBody("image/jpeg".toMediaType())
        val imageBody = MultipartBody.Part.createFormData("image", file.name, requestFile)
        Log.i("MultipartBody", "File Name: ${file.name}")
        Log.i("MultipartBody", "File Size: ${requestFile.contentLength()}")
        Log.i("API Request", "Request: $imageBody")

        val response = apiService.uploadImage(imageBody)
        Log.i("API Response", "Response Code: ${response.code()}")
        Log.i("API Response", "Response Body: ${response.body()?.toString()}")

        if (response.isSuccessful) {
            val uploadResponse = response.body()
            if (uploadResponse?.result != null) {
                if (uploadResponse.result.isNotEmpty()) {
//                    Toast.makeText(context, "Image Uploaded: ${uploadResponse.result}", Toast.LENGTH_SHORT).show()
                    "Itu adalah ${uploadResponse.result}"
                } else {
//                    Toast.makeText(context, "Image Uploaded but can't recognize", Toast.LENGTH_SHORT).show()
                    "Karakter tidak dikenali"
                }
            } else {
//                Toast.makeText(context, "Upload Failed: ${uploadResponse?.error}", Toast.LENGTH_SHORT).show()
                "upload failed: ${uploadResponse?.error}"
            }
        } else {
//            Toast.makeText(context, "Upload Failed with response code: ${response.code()}", Toast.LENGTH_SHORT).show()
            "upload failed"
        }
    } catch (e: HttpException) {
        e.printStackTrace()
//        Toast.makeText(context, "Upload Failed ${e.message}", Toast.LENGTH_SHORT).show()
        "upload failed: ${e.message}"
    }
}

//private fun onUploadButtonClick(
//    timeStamp: String,
//    capturedImageUri: Uri,
//    apiService: ApiService,
//    context: Context,
//    lifecycleScope: LifecycleCoroutineScope
//): String {
//
//    Toast.makeText(context, "Image Uploaded", Toast.LENGTH_SHORT).show()
//    lifecycleScope.launch {
//        try {
//            val file = uriToFile(timeStamp, capturedImageUri, context)
//            Log.i("Konversi uri to file", "Path: ${file.path}")
//            Log.i("Konversi uri to file", "Size: ${file.length()} bytes")
//
//            val requestFile = file.asRequestBody("image/jpeg".toMediaType())
//            val imageBody = MultipartBody.Part.createFormData("image", file.name, requestFile)
//            Log.i("MultipartBody", "File Name: ${file.name}")
//            Log.i("MultipartBody", "File Size: ${requestFile.contentLength()}")
//            Log.i("API Request", "Request: $imageBody")
//
//            val response = apiService.uploadImage(imageBody)
//            Log.i("API Response", "Response Code: ${response.code()}")
//            Log.i("API Response", "Response Body: ${response.body()?.toString()}")
//
//            if (response.isSuccessful) {
//                val uploadResponse = response.body()
//                if (uploadResponse?.result != null) {
//                    if (uploadResponse.result.isNotEmpty()){
//                        Toast.makeText(context, "Image Uploaded: ${uploadResponse.result}", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(context, "Image Uploaded but cant recognize", Toast.LENGTH_SHORT).show()
//                    }
//
//                } else {
//                    Toast.makeText(context, "Upload Failed: ${uploadResponse?.error}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        } catch (e: HttpException) {
//            e.printStackTrace()
//            Toast.makeText(context, "Upload Failed ${e.message}", Toast.LENGTH_SHORT).show()
//        }
//    }
//}

fun getImageUri(timeStamp: String, context: Context): Uri {
    var uri: Uri? = null
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "$timeStamp.jpg")
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/MyCamera/")
    }
    uri = context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    )
    return uri!!
}

fun createCustomTempFile(timeStamp: String, context: Context): File {
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}

fun uriToFile(timeStamp: String, imageUri: Uri, context: Context): File {
    val myFile = createCustomTempFile(timeStamp, context)
    val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
    val outputStream = FileOutputStream(myFile)
    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it} > 0) outputStream.write(buffer, 0, length)
    outputStream.close()
    inputStream.close()

    return myFile
}
