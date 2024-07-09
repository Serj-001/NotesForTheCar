package com.example.notesforthecar.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notesforthecar.repository.Repository
import com.example.notesforthecar.room.NotesDB
import com.example.notesforthecar.viewmodel.NoteViewModel

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val db = NotesDB.getInstance(context)
    val repository = Repository(db)
    val myViewModel = NoteViewModel(repository)
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "Main") {
        composable(route = "Main") {
            MainScreen(viewModel = myViewModel, navController )
        }
        composable("Card/{noteId}/{noteDescription}/{costType}/{costOfExpenses}") {
            CardScreen(
                navController,
                viewModel = myViewModel,
                noteId = it.arguments?.getString("noteId"),
                noteDescription = it.arguments?.getString("noteDescription"),
                costType = it.arguments?.getString("costType"),
                costOfExpenses = it.arguments?.getString("costOfExpenses")
            )
        }
    }

}