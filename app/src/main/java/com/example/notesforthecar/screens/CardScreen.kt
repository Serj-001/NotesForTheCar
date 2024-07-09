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
import androidx.compose.runtime.mutableIntStateOf
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
    costType: String?,
    costOfExpenses: String?
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
    var inputCost by remember { mutableStateOf(costOfExpenses) }

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

                Text(text = noteDescription.toString())

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
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = "Удалить",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
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
                            Text(
                                text = "Изменить",
                                fontSize = 14.sp
                            )
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
                    Text(
                        text = "Нет",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
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
                                    costType = inputCostType.toString(),
                                    costOfExpenses = inputCost!!.toInt()
                                )
                            )
                        }
                        showDialog.value = false
                        navController.popBackStack()
                    }
                ) {
                    Text(
                        text = "Да",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            title = {
                Text(
                    text = "Удалить?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.error
                )
            },
            text = {
                Text(
                    text = "Запись будет удалена",
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
                        inputCost = empty
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
                                inputCostType.toString(),
                                inputCost!!.toInt()
                            )
                            viewModel.updateNote(newNote)
                            navController.popBackStack()
                            updateDialog.value = false
                            inputNote = empty
                            inputCost = empty
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
                    OutlinedTextField(
                        value = inputCost.toString(),
                        onValueChange = { inputCost = it },
                        label = { Text(text = "Note description") },
                        placeholder = { Text(text = "Enter description") }
                    )
                }
            }
        )
    }
}