package com.example.coroutinephoto

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.coroutinephoto.ui.theme.CoroutinephotoTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoroutinephotoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoadImageFromUrl()
                }
            }
        }
    }
}

@Composable
fun LoadImageFromUrl() {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val coroutineScope = rememberCoroutineScope()

    val imageUrl = "https://users.metropolia.fi/~jarkkov/folderimage.jpg"


    LaunchedEffect(imageUrl) {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                bitmap = downloadBitmap(imageUrl)
            }
        } }

    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = "Loaded Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )
    }
}
private suspend fun downloadBitmap(imageUrl: String): Bitmap? {
    return try {
        val conn = URL(imageUrl).openConnection()
        conn.connect()
        val inputStream = conn.getInputStream()
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()
        bitmap
    } catch (e: Exception) {
        Log.e(TAG, "Exception $e")
        null
    }
}