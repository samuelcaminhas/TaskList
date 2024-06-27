package com.caminhas.tasklist


import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


private const val DATABASE_NAME = "tasks.db"
private const val DATABASE_VERSION = 1


private const val TABLE_TASKS = "tasks"
private const val COLUMN_ID = "id"
private const val COLUMN_NAME = "name"
private const val COLUMN_COMPLETED = "completed"

class TaskDatabaseHelper(context: Context) : SQLiteOpenHelper (context, DATABASE_NAME, null, DATABASE_VERSION){

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_TASKS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NAME TEXT NOT NULL, "
                + "$COLUMN_COMPLETED INTEGER NOT NULL DEFAULT 0)")
        db.execSQL(createTable)
    }

    ooverride fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }

    fun addTask(name: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_COMPLETED, 0)
        }
        db.insert(TABLE_TASKS, null, contentValues)
        db.close()
    }

    fun getAllTasks(): Cursor {
        val db = this.readableDatabase
        return db.query(TABLE_TASKS, null, null, null, null, null, null)
    }

    fun getTaskById(id: Int): Task{
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_TASK,
            arrayOf(COLUMN_ID, COLUMN_NAME, COLUMN_COMPLETED),
            "%$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        )
        cursor?.moveToFirst()
        val task = Task(
            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) > 0
        )
        cursor.close()
        db.close()
        return task
    }
    fun updateTask(id: Int, name: String, completed: Boolean) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_COMPLETED, if (completed) 1 else 0)
        }
        db.update(TABLE_TASKS, contentValues, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun updateTaskStatus(id:Int,completed: Boolean){
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_COMPLETED, if (completed) 1 else 0)
        }
        db.update(TABLE_TASKS, contentValues, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

}