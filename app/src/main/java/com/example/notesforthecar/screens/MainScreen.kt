package com.example.notesforthecar.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
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
    var noteCost by remember { mutableStateOf("") }
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
                        text = "Затраты авто:",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer)
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
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Card(
                modifier = Modifier.padding(10.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,

                    ) {
                    Column {
                        Text(
                            text = "Заправка: ${SumOfColumnRefill(notes)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Запчасти: ${SumOfColumnPart(notes)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Ремонт: ${SumOfColumnRepair(notes)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column {
                        Text(
                            text = "Всего:",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${SumAllRepair(notes)} р."
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            ) {
                items(notes) {
                    Card(
                        onClick = {
                            navController.navigate(
                                route = "Card/${Uri.encode(it.id.toString())}" +
                                        "/${Uri.encode(it.description)}" +
                                        "/${Uri.encode(it.costType)}" +
                                        "/${Uri.encode(it.costOfExpenses.toString())}"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column {
                            Text(
                                text = "" + it.costType,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(start = 14.dp, end = 14.dp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "" + it.description,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(start = 14.dp, end = 14.dp),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "" + ConvertLongToString(it.dateAdded),
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(start = 14.dp, end = 14.dp),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = "${it.costOfExpenses} руб.",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
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
                        noteCost = empty
                    }
                ) {
                    Text(text = "Отмена")
                }
            },
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            confirmButton = {
                if (inputNote.isNotEmpty())
                    Button(
                        onClick = {
                            viewModel.addNote(
                                NoteEntity(
                                    id = 0,
                                    inputNote,
                                    dateAdded = Date().time,
                                    noteCostType,
                                    noteCost.toInt()
                                )
                            )
                            showDialog = false
                            inputNote = empty
                            noteCostType = empty
                            noteCost = empty
                        }
                    ) {
                        Text(text = "Сохранить")
                    }
            },
            title = {
                Text(
                    text = "Добавить запись",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    modifier = Modifier.padding(5.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            text = {

                Column {
                    noteCostType = costTypeDropMenu()

                    OutlinedTextField(
                        value = inputNote,
                        onValueChange = { inputNote = it },
                        label = { Text(text = "Описание") },
                        placeholder = {
                            Text(
                                text = "Введите описание...",
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                    OutlinedTextField(
                        value = noteCost,
                        onValueChange = { if (it.isDigitsOnly()) noteCost = it },
                        label = { Text(text = "Стоимость") },
                        placeholder = {
                            Text(
                                text = "Введите стоимость...",
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                }
            }
        )
    }
}

fun ConvertLongToString(timeLong: Long): String {
    val dateFormatt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dateLocal = dateFormatt.format(timeLong)
    return dateLocal
}

fun SumOfColumnRefill(notes: List<NoteEntity>): Int {
    var sum = 0
    for (item in notes) {
        if (item.costType == "Заправка") sum += item.costOfExpenses
    }
    return sum
}

fun SumOfColumnPart(notes: List<NoteEntity>): Int {
    var sum = 0
    for (item in notes) {
        if (item.costType == "Запчасти") sum += item.costOfExpenses
    }
    return sum
}

fun SumOfColumnRepair(notes: List<NoteEntity>): Int {
    var sum = 0
    for (item in notes) {
        if (item.costType == "Ремонт") sum += item.costOfExpenses
    }
    return sum
}

fun SumAllRepair(notes: List<NoteEntity>): Int {
    var sum = 0
    for (item in notes) {
        sum += item.costOfExpenses
    }
    return sum
}