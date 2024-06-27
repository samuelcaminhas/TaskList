package com.caminhas.tasklist

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class TaskAdapter(private var cursor: Cursor, private val clickListener: (Int) -> Unit) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskName: TextView = itemView.findViewById(R.id.text_view_task_name)
        val taskCompleted: CheckBox = itemView.findViewById(R.id.checkbox_task_completed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        if (!cursor.moveToPosition(position)) return

        val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
        val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
        val completed = cursor.getInt(cursor.getColumnIndexOrThrow("completed")) > 0

        holder.taskName.text = name
        holder.taskCompleted.isChecked = completed

        holder.itemView.setOnClickListener {
            clickListener(id)
        }

        holder.taskCompleted.setOnClickListener {
            val newStatus = !completed
            val db = TaskDatabaseHelper(holder.itemView.context)
            db.updateTaskStatus(id, newStatus)
            swapCursor(db.getAllTasks())
        }
    }

    override fun getItemCount(): Int {
        return cursor.count
    }

    fun swapCursor(newCursor: Cursor) {
        if (cursor != null) {
            cursor.close()
        }
        cursor = newCursor
        if (newCursor != null) {
            notifyDataSetChanged()
        }
    }
}