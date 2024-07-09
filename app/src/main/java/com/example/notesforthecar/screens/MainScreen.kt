package com.example.notesforthecar.screens

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.notesforthecar.R
import com.example.notesforthecar.room.NoteEntity
import com.example.notesforthecar.viewmodel.NoteViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: NoteViewModel, navController: NavController) {
    var inputNote by remember { mutableStateOf("") }
    var noteCostType by remember { mutableStateOf("") }
    val empty by remember { mutableStateOf("") }
    var showDialog by remember {
        mutableStateOf(false)
    }
    val notes by viewModel.notes.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notes",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.background
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background,
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_24),
                    contentDescription = null
                )
            }
        }
    ) {
        if (notes.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                Text(text = "No notes")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                contentPadding = PaddingValues(10.dp)
            ) {
                items(notes) {
                    Card(
                        onClick = {
                            navController.navigate(
                                route = "Card/${Uri.encode(it.id.toString())}" +
                                        "/${Uri.encode(it.description)}" +
                                        "/${Uri.encode(it.costType)}"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column{
                            Text(
                                text = "" + it.costType,
                                fontSize = 24.sp,
                                modifier = Modifier.padding(start = 14.dp, end = 14.dp),
                            )
                            Text(
                                text = "" + it.description,
                                fontSize = 22.sp,
                                modifier = Modifier.padding(14.dp),
                            )
                            Text(
                                text = "" + ConvertLongToString(it.dateAdded),
                                fontSize = 18.sp,
                                modifier = Modifier.padding(start = 14.dp, end = 14.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                        inputNote = empty
                        noteCostType = empty
                    }
                ) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                if (inputNote.isNotEmpty())
                    Button(
                        onClick = {
                            viewModel.addNote(NoteEntity(
                                id = 0, inputNote, dateAdded = Date().time, noteCostType)
                            )
                            showDialog = false
                            inputNote = empty
                            noteCostType = empty
                        }
                    ) {
                        Text(text = "Save")
                    }
            },
            title = {
                Text(
                    text = "Add note",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    modifier = Modifier.padding(5.dp)
                )
            },
            text = {

                Column {
                    noteCostType = CostTypeDropMenu()

                    OutlinedTextField(
                        value = inputNote,
                        onValueChange = { inputNote = it },
                        label = { Text(text = "Note description") },
                        placeholder = { Text(text = "Enter description") }
                    )
                }
            }
        )
    }
}

fun ConvertLongToString(timeLong: Long) : String {
    val dateFormatt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dateLocal = dateFormatt.format(timeLong)
    return dateLocal
}