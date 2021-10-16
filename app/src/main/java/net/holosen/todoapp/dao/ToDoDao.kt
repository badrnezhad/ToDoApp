package net.holosen.todoapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import net.holosen.todoapp.models.ToDo

@Dao
interface ToDoDao {
    //CRUD => Create, Read, Update, Delete

    @Insert
    suspend fun insertToDo(todo: ToDo)

    @Update
    suspend fun updateToDo(todo: ToDo)

    @Delete
    suspend fun deleteToDo(todo: ToDo)

    @Query("SELECT * FROM todo order by id asc")
    fun getAllToDos(): LiveData<List<ToDo>>

    //LiveData => Live + Data
}