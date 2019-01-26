package br.com.philippesis.listatarefas;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.philippesis.listatarefas.model.TaskListRepository;
import br.com.philippesis.listatarefas.model.TasksEntity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditTask;
    private Button mBtnAdd;
    private RecyclerView mRecyclerTasks;
    private List<TasksEntity> mTasks;
    private TaskListRepository mTaskListRepository;
    private TaskListAdapter mTaskListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cria instancia da conaxão
        mTaskListRepository = TaskListRepository.getInstance();

        setupUI();
        loadData();
        listeners();

    }

    private void setupUI() {

        mRecyclerTasks = findViewById(R.id.id_recycle_tasks);
        mBtnAdd = findViewById(R.id.id_btn_add);
        mEditTask = findViewById(R.id.id_edit_task);

    }

    private void loadData() {

        // Cria bando caso não exista
        try {
            mTaskListRepository.createDataBase(MainActivity.this);
        } catch (Exception er) {
            Log.e("Err. ", er.toString());
        }

        if (mTaskListRepository != null) {

            // Busca todos registros
            try {
                mTasks = new ArrayList<>();
                mTasks = mTaskListRepository.findAll();
                adapter();
            } catch (Exception er) {
                Log.e("Err. ", er.toString());
            }

        }

    }

    private void adapter() {
        mTaskListAdapter = new TaskListAdapter(MainActivity.this, mTasks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerTasks.setLayoutManager(linearLayoutManager);
        mRecyclerTasks.setAdapter(mTaskListAdapter);
        mRecyclerTasks.setHasFixedSize(true);

    }

    private void listeners() {
        mBtnAdd.setOnClickListener(this);
            }

    @Override
    public void onClick(View v) {
        if(v == mBtnAdd) {
            if(!mEditTask.getText().toString().isEmpty() && !mEditTask.getText().toString().equals(" ")
            && mTaskListRepository != null) {
                TasksEntity tasksEntity = new TasksEntity.Builder()
                        .setTaskDesc(mEditTask.getText().toString()).build();
                // Persistir
                try {
                    mTaskListRepository.save(tasksEntity);
                    mEditTask.setText("");
                    mTaskListAdapter.swapItems(mTaskListRepository.findAll());
                    setMsg("Salvo com Sucesso.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setMsg(String s) {
        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
