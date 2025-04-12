package com.example.carrossel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.transformations
import coil3.transform.CircleCropTransformation
import coil3.transform.RoundedCornersTransformation
import coil3.util.DebugLogger
import com.example.carrossel.ui.theme.CarrosselTheme
import okio.Path.Companion.toOkioPath
import java.io.File


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarrosselTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ImageCarousel(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

data class CarouselImage(
    val url: String,
)

val imageList = listOf(
    CarouselImage("https://live.staticflickr.com/65535/54208276236_f0b750c0b1_o_d.png"),
    CarouselImage("https://live.staticflickr.com/65535/54102426319_9af09725b5_o_d.png"),
    CarouselImage("https://i.imgur.com/4s0Ga2j.jpeg")
)

@Composable
fun ImageCarousel(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Configura o ImageLoader com cache
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                val cacheDir = File(context.cacheDir, "image_cache")
                coil3.disk.DiskCache.Builder()
                    .directory(cacheDir.toOkioPath())
                    .maxSizeBytes(500L * 1024 * 1024)
                    .build()
            }
            .logger(DebugLogger())
            .build()
    }

    val customImageUrl = "https://t3.ftcdn.net/jpg/02/31/17/32/240_F_231173210_stzZNH7tblr3esfej44EpbKqAQfbkfsT.jpg" // substitua por um link real se quiser ver as transformações

    Column(modifier = modifier.padding(16.dp)) {
        Text("Carrossel de Imagens", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        // Carrossel de imagens
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Imagens do imageList
            items(imageList) { image ->
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(image.url)
                        .build(),
                    contentDescription = null,
                    imageLoader = imageLoader,
                    modifier = Modifier
                        .width(300.dp)
                        .height(300.dp)
                )
            }

            // Imagem normal
            item {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = customImageUrl,
                        imageLoader = imageLoader
                    ),
                    contentDescription = "Gatinho normal",
                    modifier = Modifier.size(300.dp)
                )
            }

            // Imagem com cantos arredondados
            item {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context)
                            .data(customImageUrl)
                            .transformations(RoundedCornersTransformation(32f))
                            .build(),
                        imageLoader = imageLoader
                    ),
                    contentDescription = "Gatinho com cantos arredondados",
                    modifier = Modifier.size(300.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Imagens dos gatos abaixo do carrossel
        Text("Imagens dos Gatos", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(customImageUrl)
                        .transformations(RoundedCornersTransformation(32f))
                        .build(),
                    imageLoader = imageLoader
                ),
                contentDescription = "Gatinho com cantos arredondados",
                modifier = Modifier.size(300.dp)
            )
            Image(
                painter = rememberAsyncImagePainter(
                    model = customImageUrl,
                    imageLoader = imageLoader
                ),
                contentDescription = "Gatinho normal",
                modifier = Modifier.size(300.dp)
            )
        }
    }
}
