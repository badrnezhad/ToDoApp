package net.holosen.todoapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.holosen.todoapp.dao.ToDoDao
import net.holosen.todoapp.models.ToDo

@Database(entities = [ToDo::class], version = 2)
abstract class ToDoDatabase : RoomDatabase() {

    abstract fun toDoDao(): ToDoDao

    companion object {
        private var instance: ToDoDatabase? = null
        fun getInstance(context: Context): ToDoDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    ToDoDatabase::class.java, "todo.db"
                ).fallbackToDestructiveMigration().build()
            }
            return instance as ToDoDatabase
        }
    }
}