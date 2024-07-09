package com.example.notesforthecar.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.notesforthecar.R
import com.example.notesforthecar.room.NoteEntity
import com.example.notesforthecar.viewmodel.NoteViewModel
import java.util.Date

@Composable
fun CardScreen(
    navController: NavController,
    viewModel: NoteViewModel,
    noteId: String?,
    noteDescription: String?,
    costType: String?
) {
    val showDialog = remember {
        mutableStateOf(false)
    }
    val updateDialog = remember {
        mutableStateOf(false)
    }
    var inputNote by remember { mutableStateOf(noteDescription) }
    val empty by remember { mutableStateOf("") }
    var inputCostType by remember { mutableStateOf(costType) }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(15.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxSize()
            ) {
                
                Spacer(modifier = Modifier.height(40.dp))
                Row {
                    Text(
                        text = "id: ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "" + noteId,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Description: ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "" + noteDescription,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                ) {
                    OutlinedIconButton(
                        onClick = { showDialog.value = true },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.size(height = 50.dp, width = 100.dp)
                    ) {
                        Row {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_delete_24),
                                contentDescription = null
                            )
                            Text(text = "Delete")
                        }
                    }
                    OutlinedIconButton(
                        onClick = {
                            updateDialog.value = true
                        },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier.size(height = 50.dp, width = 110.dp)
                    ) {
                        Row {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_edit_24),
                                contentDescription = null
                            )
                            Text(text = "Update")
                        }
                    }
                }
            }
        }
    }
    
    if(showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            dismissButton = {
                Button(
                    onClick = { showDialog.value = false }
                ) {
                    Text(text = "No")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if(noteId != null) {
                            viewModel.deleteNote(
                                note = NoteEntity(id = noteId.toInt(),
                                    description = noteDescription.toString(),
                                    dateAdded = Date().time,
                                    costType = inputCostType.toString()
                                )
                            )
                        }
                        showDialog.value = false
                        navController.popBackStack()
                    }
                ) {
                    Text(text = "Yes")
                }
            },
            title = {
                Text(
                    text = "Delete book",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
            },
            text = {
                Text(
                    text = "Are you sure?",
                    fontSize = 20.sp
                )
            }
        )
    }

    if (updateDialog.value) {
        AlertDialog(
            onDismissRequest = { updateDialog.value = false },
            dismissButton = {
                Button(
                    onClick = {
                        updateDialog.value = false
                        inputNote = empty
                    }
                ) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                if (inputNote!!.isNotEmpty())
                    Button(
                        onClick = {
                            val newNote = NoteEntity(
                                noteId!!.toInt(),
                                inputNote.toString(),
                                dateAdded = Date().time,
                                inputCostType.toString())
                            viewModel.updateNote(newNote)
                            navController.popBackStack()
                            updateDialog.value = false
                            inputNote = empty
                        }
                    ) {
                        Text(text = "Update")
                    }
            },
            title = {
                Text(
                    text = "Update note",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    modifier = Modifier.padding(5.dp)
                )
            },
            text = {


                Column {
                    inputCostType = CostTypeDropMenu()

                    OutlinedTextField(
                        value = inputNote.toString(),
                        onValueChange = { inputNote = it },
                        label = { Text(text = "Note description") },
                        placeholder = { Text(text = "Enter description") }
                    )
                }
            }
        )
    }
}