package com.biglist.ui.screens
import com.biglist.ui.viewModels.UserDetailsViewModel
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreen(
    viewModel: UserDetailsViewModel = viewModel(),
    onUserDetailsSaved: () -> Unit
) {
    val context = LocalContext.current

    val name by viewModel.name.collectAsState()
    val surname by viewModel.surname.collectAsState()
    val photoPath by viewModel.photoPath.collectAsState()
    var vmName by remember { mutableStateOf(name) }
    var vmSurname by remember { mutableStateOf(surname) }
    val photoUri: Uri? by remember(photoPath) {
        derivedStateOf {
            if (!photoPath.isNullOrEmpty()) {
                try {
                    Uri.parse(photoPath)
                } catch (e: Exception) {
                    Log.e("UserDetailsScreen", "Invalid photo URI string: $photoPath", e)
                    null
                }
            } else {
                null
            }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )

            viewModel.saveUserDetails(vmName, vmSurname, uri.toString())
            Toast.makeText(context, "Zdjęcie z galerii wybrane!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Nie wybrano zdjęcia z galerii.", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        val imageModifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .fillMaxSize()
            .align(Alignment.CenterHorizontally)

        if (photoUri != null) {
            Image(
                painter = rememberAsyncImagePainter(photoUri),
                contentDescription = "Profile Photo",
                contentScale = ContentScale.Crop,
                modifier = imageModifier
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Default Profile Photo",
                modifier = imageModifier,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = { galleryLauncher.launch("image/*") }) {
                Text("Wybierz z Galerii")
            }
            if (photoUri != null) {
                Button(onClick = { viewModel.clearPhoto() }) {
                    Text("Wyczyść Zdjęcie")
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = vmName,
            onValueChange = { newValue -> vmName = newValue },
            label = { Text("Imię") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = vmSurname,
            onValueChange = { vmSurname = it },
            label = { Text("Nazwisko") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.saveUserDetails(
                    name = vmName,
                    surname = vmSurname,
                    photoPath = photoPath
                )
                Toast.makeText(context, "Detale zapisane!", Toast.LENGTH_SHORT).show()
                onUserDetailsSaved()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Zapisz Detale")
        }
    }
}