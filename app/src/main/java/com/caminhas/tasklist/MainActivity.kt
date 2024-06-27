package com.caminhas.tasklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private lateinit var taskDatabaseHelper: TaskDatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        taskDatabaseHelper = TaskDatabaseHelper(this)

        recyclerView = findViewById(R.id.recycler_view_tasks)
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(taskDatabaseHelper.getAllTasks(), ::onTaskClicked)
        recyclerView.adapter = taskAdapter

        val addButton: MaterialButton = findViewById(R.id.button_add_task)
        addButton.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        taskAdapter.swapCursor(taskDatabaseHelper.getAllTasks())
    }
    private fun onTaskClicked(taskId: Int) {
        val intent = Intent(this, AddTaskActivity::class.java)
        intent.putExtra("TASK_ID", taskId)
        startActivity(intent)
    }




}

