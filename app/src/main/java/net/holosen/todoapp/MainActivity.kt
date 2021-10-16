package net.holosen.todoapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import net.holosen.todoapp.adapters.RecyclerAdapter
import net.holosen.todoapp.handlers.SwipeToDeleteCallback
import net.holosen.todoapp.models.ToDo
import net.holosen.todoapp.viewmodels.ToDoViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var toDoViewModel: ToDoViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerAdapter
    private lateinit var fab: FloatingActionButton
    private lateinit var swipe: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        bindViews()
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = RecyclerAdapter(this)
        recyclerView.adapter = adapter
        toDoViewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)
        toDoViewModel.getAllData().observe(this) {
            adapter.setDataList(it)
        }

        val addNewToDoResultActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data ?: return@registerForActivityResult
                    val title = data.getStringExtra(AddOrEditToDoActivity.EXTRA_TITLE)
                    val description = data.getStringExtra(AddOrEditToDoActivity.EXTRA_DESC)
                    val todo = ToDo(title = title!!, description = description!!, done = false)
                    MainScope().launch { toDoViewModel.insert(todo) }
                    Toast.makeText(this, "new todo added", Toast.LENGTH_SHORT).show()
                }
            }

        fab.setOnClickListener {
            val intent = Intent(this, AddOrEditToDoActivity::class.java)
            addNewToDoResultActivity.launch(intent)
        }

        swipe.setOnRefreshListener {
            adapter.notifyDataSetChanged()
            swipe.isRefreshing = false
        }

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                MainScope().launch {
                    toDoViewModel.delete(adapter.getToDo(viewHolder.adapterPosition))
                    Toast.makeText(this@MainActivity, "todo item deleted", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        val editToDoResultActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data ?: return@registerForActivityResult
                    val id = data.getIntExtra(AddOrEditToDoActivity.EXTRA_ID, -1)
                    if (id == -1) {
                        Toast.makeText(this, "Invalid Data", Toast.LENGTH_SHORT).show()
                        return@registerForActivityResult
                    }
                    val title = data.getStringExtra(AddOrEditToDoActivity.EXTRA_TITLE)
                    val description = data.getStringExtra(AddOrEditToDoActivity.EXTRA_DESC)
                    val todo =
                        ToDo(title = title!!, description = description!!, done = false, id = id)
                    MainScope().launch { toDoViewModel.update(todo) }
                    Toast.makeText(this, "todo updated!", Toast.LENGTH_SHORT).show()
                }
            }

        adapter.setOnItemClickListener(object : RecyclerAdapter.OnItemClickListener {
            override fun onItemClick(toDo: ToDo) {
                val intent = Intent(this@MainActivity, AddOrEditToDoActivity::class.java)
                intent.putExtra(AddOrEditToDoActivity.EXTRA_TITLE, toDo.title)
                intent.putExtra(AddOrEditToDoActivity.EXTRA_DESC, toDo.description)
                intent.putExtra(AddOrEditToDoActivity.EXTRA_ID, toDo.id)
                editToDoResultActivity.launch(intent)
            }
        })

        adapter.setOnStateChangeListener(object : RecyclerAdapter.OnDoneStateChangeListener {
            override fun onStateChanged(toDo: ToDo, isChecked: Boolean) {
                toDo.done = isChecked
                MainScope().launch { toDoViewModel.update(toDo) }
            }

        })

    }

    private fun bindViews() {
        recyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.fab)
        swipe = findViewById(R.id.swipe)
    }
}