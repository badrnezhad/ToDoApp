package net.holosen.todoapp.repositories

import android.app.Application
import androidx.lifecycle.LiveData
import net.holosen.todoapp.dao.ToDoDao
import net.holosen.todoapp.db.ToDoDatabase
import net.holosen.todoapp.models.ToDo

class ToDoRepository(application: Application) {

    private var toDoDao: ToDoDao
    private lateinit var allToDoList: LiveData<List<ToDo>>

    init {
        val database = ToDoDatabase.getInstance(application)
        toDoDao = database.toDoDao()
        allToDoList = toDoDao.getAllToDos()
    }

    suspend fun insertTodo(toDo: ToDo) {
        toDoDao.insertToDo(toDo)
    }

    suspend fun updateToDo(toDo: ToDo) {
        toDoDao.updateToDo(toDo)
    }

    suspend fun deleteToDo(toDo: ToDo) {
        toDoDao.deleteToDo(toDo)
    }

    fun getAllToDos(): LiveData<List<ToDo>> {
        return allToDoList
    }
}