package com.caminhas.tasklist

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddTaskActivity : AppCompatActivity() {

    private lateinit var taskDatabaseHelper: TaskDatabaseHelper
    private lateinit var taskNameEditText: EditText
    private lateinit var taskCompletedCheckBox: CheckBox
    private lateinit var addTaskButton: Button

    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        taskDatabaseHelper = TaskDatabaseHelper(this)
        taskNameEditText = findViewById(R.id.edit_text_task_name)
        taskCompletedCheckBox = findViewById(R.id.checkbox_task_completed)
        addTaskButton = findViewById(R.id.button_add_task)

        // Verificar se estamos editando uma tarefa existente
        if (intent.hasExtra("TASK_ID")) {
            taskId = intent.getIntExtra("TASK_ID", -1)
            if (taskId != -1) {
                val task = taskDatabaseHelper.getTaskById(taskId)
                taskNameEditText.setText(task.name)
                taskCompletedCheckBox.isChecked = task.completed
            }
        }

        addTaskButton.setOnClickListener {
            val taskName = taskNameEditText.text.toString()
            val taskCompleted = taskCompletedCheckBox.isChecked
            if (taskName.isNotEmpty()) {
                if (taskId == -1) {
                    taskDatabaseHelper.addTask(taskName)
                } else {
                    taskDatabaseHelper.updateTask(taskId, taskName, taskCompleted)
                }
                finish()
            }
        }
    }
}
