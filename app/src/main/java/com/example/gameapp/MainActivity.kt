package com.example.gameapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gameapp.ui.theme.GameAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    ProfileEditorScreen(navController = navController, numPlayers = 6)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditorScreen(navController: NavController, numPlayers: Int) {
    val initialColors = listOf(
        extendedLight.deepRed.color,
        extendedLight.deepBlue.color,
        extendedLight.deepGreen.color,
        extendedLight.deepPurple.color,
        extendedLight.deepOrange.color,
        extendedLight.deepTeal.color
    )
    val playerColors = remember(numPlayers) {
        mutableStateListOf<Color>().also {
            it.addAll(List(numPlayers) { index ->
                initialColors[index % initialColors.size]
            })
        }
    }

    var showColorDialog by remember { mutableStateOf(false) }
    var showNameDialog by remember { mutableStateOf(false) }
    var selectedPlayerIndex by remember { mutableStateOf<Int?>(null) }
    val playerNames = remember(numPlayers) {
        mutableStateListOf<String>().also {
            it.addAll(List(numPlayers) { index -> "Player ${index + 1}" })
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Customize profiles")
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            for (index in 0 until numPlayers) {
                PlayerItem(
                    playerName = playerNames[index],
                    color = playerColors[index],
                    onEditClick = {
                        selectedPlayerIndex = index
                        showColorDialog = true
                    },
                    onNameChange = {
                        selectedPlayerIndex = index
                        showNameDialog = true
                    }
                )
            }
        }
    }

    if (showColorDialog) {
        ColorPickerDialog(
            onDismissRequest = { showColorDialog = false },
            onColorSelected = { color ->
                selectedPlayerIndex?.let {
                    playerColors[it] = color
                    showColorDialog = false
                }
            }
        )
    }

    if (showNameDialog) {
        NameEditDialog(
            initialName = selectedPlayerIndex?.let { playerNames[it] } ?: "",
            onDismissRequest = { showNameDialog = false },
            onNameConfirmed = { newName ->
                selectedPlayerIndex?.let {
                    playerNames[it] = newName
                    showNameDialog = false
                }
            }
        )
    }
}

@Composable
fun NameEditDialog(
    initialName: String,
    onDismissRequest: () -> Unit,
    onNameConfirmed: (String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Edit Player Name") },
        text = {
            TextField(
                value = name,
                onValueChange = { name = it },
                singleLine = true,
                label = { Text("Player Name") }
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onNameConfirmed(name) }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun PlayerItem(
    playerName: String,
    color: Color,
    onEditClick: () -> Unit,
    onNameChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceBright
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(color)
                        .clickable { onEditClick() }
                )
                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = playerName,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (playerName.startsWith("Player ")) MaterialTheme.colorScheme.onSurfaceVariant else Color.Unspecified,
                    fontWeight = if (playerName.startsWith("Player ")) FontWeight.Normal else FontWeight.Bold,
                    modifier = Modifier.clickable {
                        // Call onNameChange to trigger the name edit dialog
                        onNameChange(playerName)
                    }
                )
            }
        }
    }
}

@Composable
fun ColorPickerDialog(
    onDismissRequest: () -> Unit,
    onColorSelected: (Color) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Choose Color") },
        text = {
            ColorPickerList(onColorSelected)
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) { Text("Close") }
        }
    )
}

@Composable
fun ColorPickerList(onColorSelected: (Color) -> Unit) {
    val colorFamilies = listOf(
        extendedLight.deepRed,
        extendedLight.deepOrange,
        extendedLight.deepGreen,
        extendedLight.deepTeal,
        extendedLight.deepBlue,
        extendedLight.deepIndigo,
        extendedLight.deepPurple,
        extendedDark.deepRed,
        extendedDark.deepOrange,
        extendedDark.deepGreen,
        extendedDark.deepTeal,
        extendedDark.deepBlue,
        extendedDark.deepIndigo,
        extendedDark.deepPurple
    )

    LazyColumn {
        itemsIndexed(colorFamilies) { index, colorFamily ->
            ColorPickerItem(colorFamily, onColorSelected, index < colorFamilies.size - 1)
        }
    }
}

@Composable
fun ColorPickerItem(
    colorFamily: ColorFamily,
    onColorSelected: (Color) -> Unit,
    isNotLastItem: Boolean = true
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onColorSelected(colorFamily.color) }
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(colorFamily.color)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = when (colorFamily) {
                    extendedLight.deepRed -> "Crimson"
                    extendedLight.deepOrange -> "Coral"
                    extendedLight.deepGreen -> "Emerald"
                    extendedLight.deepTeal -> "Turquoise"
                    extendedLight.deepBlue -> "Azure"
                    extendedLight.deepIndigo -> "Indigo"
                    extendedLight.deepPurple -> "Violet"
                    extendedDark.deepRed -> "Rose"
                    extendedDark.deepOrange -> "Peach"
                    extendedDark.deepGreen -> "Mint"
                    extendedDark.deepTeal -> "Aqua"
                    extendedDark.deepBlue -> "Sky Blue"
                    extendedDark.deepIndigo -> "Lavender"
                    extendedDark.deepPurple -> "Lilac"
                    else -> colorFamily.color.toString()
                }
            )
        }
        if (isNotLastItem) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 8.dp),
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

//Preview composable to view the screen
@Preview
@Composable
fun ProfileEditorListPreview() {
    val navController = rememberNavController()
    ProfileEditorScreen(navController = navController, numPlayers = 6)
}