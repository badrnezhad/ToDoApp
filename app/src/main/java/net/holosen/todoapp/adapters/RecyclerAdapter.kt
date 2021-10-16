package net.holosen.todoapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import net.holosen.todoapp.R
import net.holosen.todoapp.models.ToDo

class RecyclerAdapter(
    private val context: Context
) : RecyclerView.Adapter<RecyclerAdapter.ToDoHolder>() {

    private val dataList = ArrayList<ToDo>()
    private lateinit var onItemClickListener: OnItemClickListener
    private lateinit var onDoneStateChangeListener: OnDoneStateChangeListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item_layout, parent, false)
        return ToDoHolder(inflate)
    }

    override fun onBindViewHolder(holder: ToDoHolder, position: Int) {
        val item = dataList[position]
        holder.checkbox.text = item.title
        holder.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            onDoneStateChangeListener.onStateChanged(item, isChecked)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setDataList(list: List<ToDo>) {
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    fun getToDo(position: Int): ToDo {
        return dataList[position]
    }

    inner class ToDoHolder(v: View) : RecyclerView.ViewHolder(v) {

        var checkbox: CheckBox = v.findViewById(R.id.chx)

        init {
            v.setOnClickListener {
                onItemClickListener.onItemClick(dataList[adapterPosition])
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(toDo: ToDo)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnDoneStateChangeListener {
        fun onStateChanged(toDo: ToDo, isChecked: Boolean)
    }

    fun setOnStateChangeListener(onDoneStateChangeListener: OnDoneStateChangeListener) {
        this.onDoneStateChangeListener = onDoneStateChangeListener
    }
}