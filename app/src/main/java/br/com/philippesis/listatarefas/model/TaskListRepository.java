package br.com.philippesis.listatarefas.model;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TaskListRepository {

    private SQLiteDatabase mDatabase;

    private static TaskListRepository instance = null;

    // Empty Constructor
    private TaskListRepository() { }

    // Create Data Base
    public void createDataBase(Activity activity) {

        mDatabase = activity.openOrCreateDatabase("task_list_db", Context.MODE_PRIVATE, null);

        // Create Table
        mDatabase.execSQL("CREATE TABLE IF NOT EXISTS tasks(id INTEGER PRIMARY KEY AUTOINCREMENT, task VARCHAR)");

    }

    // Persist in DB
    public void save(TasksEntity task) {
        mDatabase.execSQL("INSERT INTO tasks (task) VALUES('"+task.getTaskDesc()+"')");
    }

    // Find All Data from Table
    public List<TasksEntity> findAll() {

        //
        TasksEntity tasksEntity;

        List<TasksEntity> tasks = new ArrayList<>();

        Cursor cursor = mDatabase.rawQuery("SELECT * FROM tasks ORDER BY id DESC", null);

        cursor.isFirst();

        // While exists data
        while (cursor.moveToNext()) {
            tasksEntity = new TasksEntity.Builder()
                    .setId(cursor.getLong(cursor.getColumnIndex("id")))
                    .setTaskDesc(cursor.getString(cursor.getColumnIndex("task")))
                    .build();
            tasks.add(tasksEntity);
        }

        return tasks;

    }

    // Delete from Tasks
    public void delete(long id) {
        mDatabase.execSQL("DELETE FROM tasks WHERE id="+id);
    }

    // Singleton
    public static TaskListRepository getInstance() {
        if(instance == null) {
            instance = new TaskListRepository();
        }
        return instance;
    }
}
