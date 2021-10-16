package net.holosen.todoapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import net.holosen.todoapp.models.ToDo
import net.holosen.todoapp.repositories.ToDoRepository

public class ToDoViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: ToDoRepository = ToDoRepository(application)
    private var allToDoList = repository.getAllToDos()

    suspend fun insert(toDo: ToDo) {
        repository.insertTodo(toDo)
    }

    suspend fun update(toDo: ToDo) {
        if (toDo.id <= 0) return
        repository.updateToDo(toDo)
    }

    suspend fun delete(toDo: ToDo) {
        if (toDo.id <= 0) return
        repository.deleteToDo(toDo)
    }

    fun getAllData(): LiveData<List<ToDo>> {
        return allToDoList
    }
}