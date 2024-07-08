package com.example.notesforthecar.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class NotesDB : RoomDatabase() {

    abstract fun noteDao(): NoteDAO

    companion object {

        @Volatile
        private var INSTANCE: NotesDB? = null

        fun getInstance(context: Context): NotesDB {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NotesDB::class.java,
                        "notes_db"
                    ).build()
                }
                return instance
            }
        }
    }
}