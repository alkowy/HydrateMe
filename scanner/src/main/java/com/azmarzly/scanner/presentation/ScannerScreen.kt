@file:OptIn(ExperimentalPermissionsApi::class)

package com.azmarzly.scanner.presentation

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.azmarzly.core.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.util.concurrent.Executors

@Composable
fun ScannerScreen(scannerViewModel: ScannerViewModel = hiltViewModel()) {
    Column {
        Text(text = "Scanner")
//        ScannerContent()
        ScannerContent()
    }

}
@Composable
fun  ScannerContent() {

    val context = LocalContext.current
    var lifecycleOwner = LocalLifecycleOwner.current

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
        )
    )
    var previewView: PreviewView = remember { PreviewView(context) }


    LaunchedEffect(Unit) {
        permissionState.launchMultiplePermissionRequest()
    }

    PermissionsRequired(
        multiplePermissionsState = permissionState,
        permissionsNotGrantedContent = {
            Toast.makeText(context, "permission not granted", Toast.LENGTH_SHORT).show()
        },
        permissionsNotAvailableContent = {
            Toast.makeText(context, "permission not available content", Toast.LENGTH_SHORT).show()

        }
    ) {
        Toast.makeText(context, "content", Toast.LENGTH_SHORT).show()

        Box(
            modifier = Modifier
                .background(Color.Blue)
                .fillMaxSize()
        ) {




            IconButton(
                onClick = {
                    Log.d("ANANAS", "ScannerContent: }")
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                Text(text = "Scan")
                Icon(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "scan",
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}


@androidx.camera.core.ExperimentalGetImage
@Composable
fun PreviewViewComposable() {
    AndroidView(
        { context ->
            val cameraExecutor = Executors.newSingleThreadExecutor()
            val previewView = PreviewView(context).also {
                it.scaleType = PreviewView.ScaleType.FILL_CENTER
            }
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                val imageCapture = ImageCapture.Builder().build()

                val imageAnalyzer = ImageAnalysis.Builder()
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, BarcodeAnalyser {
                            Log.d("ANANAS", "PreviewViewComposable: $it")
                        })
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll()

                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(
                        context as ComponentActivity, cameraSelector, preview, imageCapture, imageAnalyzer
                    )

                } catch (exc: Exception) {
                    Log.e("DEBUG", "Use case binding failed", exc)
                }
            }, ContextCompat.getMainExecutor(context))
            previewView
        },
        modifier = Modifier
            .size(width = 250.dp, height = 250.dp)
    )
}

@ExperimentalGetImage
class BarcodeAnalyser(
    val callback: () -> Unit
) : ImageAnalysis.Analyzer {
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.toBitmap()
        mediaImage?.let {
            callback()
        }
        imageProxy.close()
    }
}

