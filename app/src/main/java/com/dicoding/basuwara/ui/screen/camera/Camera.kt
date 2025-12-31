package com.dicoding.basuwara.ui.screen.camera

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.dicoding.basuwara.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun CameraContent(
    viewModel: CameraViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var uri by remember { mutableStateOf(Uri.EMPTY) }


    var capturedImageUri by remember {mutableStateOf<Uri>(Uri.EMPTY) }
    var isImageTaken by rememberSaveable { mutableStateOf(false) }

    val classificationResult = viewModel.classificationResult.collectAsState("")

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

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {result ->
        if (result != null) {
            capturedImageUri = result
            isImageTaken = true
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            uri = getImageUri(context)
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
            classificationResult = classificationResult.value,
            context = context,
            onCaptureButtonClick = {
                val permissionCheckResult =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                    uri = getImageUri(context)
                    cameraLauncher.launch(uri)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            },
            onGalleryClick = {
                galleryLauncher.launch("image/*")
            },
            onUploadButtonClick = {
                val stream = context.contentResolver.openInputStream(capturedImageUri)
                val bitmap = BitmapFactory.decodeStream(stream) ?: throw IllegalArgumentException("Cannot decode bitmap from URI")
                viewModel.classifyImage(bitmap)
            },

        )
    }
}

@Composable
fun AppContent(
    isImageTaken: Boolean,
    capturedImageUri: Uri,
    classificationResult: String,
    context: Context,
    onCaptureButtonClick: () -> Unit,
    onUploadButtonClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
            ,
        ) {
            if (capturedImageUri.path?.isNotEmpty() == true) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .align(Alignment.TopCenter)
                        .testTag("image"),
                    painter = rememberImagePainter(capturedImageUri),
                    contentDescription = null
                )
            } else{
                Image(
                    painter = painterResource(id = R.drawable.ic_image),
                    contentDescription = "image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }
            Text(
                text = capturedImageUri.toString()
            )
            if (classificationResult.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 16.dp)
                        .align(Alignment.BottomCenter)
                        .testTag("result")
                ) {
                    Text(
                        text = classificationResult,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }


        Row {
            Button(onClick = { onCaptureButtonClick() }) {
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = "Upload",
                )
                Text(
                    text = " Camera"
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(onClick = { onGalleryClick() }) {
                Icon(
                    Icons.Default.Photo,
                    contentDescription = "Gallery",
                )
                Text(text = "Gallery")
            }
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            if (isImageTaken) {
                onUploadButtonClick()
            } else {
                Toast.makeText(context, "Capture image first", Toast.LENGTH_SHORT).show()
            }

        }) {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
            )
            Text(
                text = "Search"
            )
        }
    }
}


fun getImageUri(context: Context): Uri {
    var timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
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



