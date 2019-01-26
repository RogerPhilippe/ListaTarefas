package br.com.philippesis.listatarefas;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.philippesis.listatarefas.model.TaskListRepository;
import br.com.philippesis.listatarefas.model.TasksEntity;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolderTaskList> {

    private Context mContext;
    private List<TasksEntity> mTasks;
    private TasksEntity mTasksEntity;

    public TaskListAdapter(Context context, List<TasksEntity> tasks) {
        this.mContext = context;
        this.mTasks = tasks;
    }

    @NonNull
    @Override
    public ViewHolderTaskList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_task_list, parent, false);
        ViewHolderTaskList viewHolderTaskList = new ViewHolderTaskList(view, parent.getContext());

        return viewHolderTaskList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderTaskList viewHolderTaskList, int position) {

        if(mTasks != null && mTasks.size() > 0) {

            try {
                viewHolderTaskList.txtId.setText(String.valueOf(mTasks.get(position).getId()));
                viewHolderTaskList.txtDesc.setText(mTasks.get(position).getTaskDesc());
            } catch (Exception er) {
                Log.e("Err - ", er.toString());
            }

        }

    }

    @Override
    public int getItemCount() {

        int listSize = 0;
        if(mTasks != null || mTasks.size() > 0) {
            listSize = mTasks.size();
        }

        return listSize;
    }

    public void swapItems(List<TasksEntity> tasks){
        this.mTasks = tasks;
        notifyDataSetChanged();
    }

    protected class ViewHolderTaskList extends RecyclerView.ViewHolder {

        protected TextView txtId;
        protected TextView txtDesc;

        public ViewHolderTaskList(@NonNull View itemView, final Context context) {
            super(itemView);

            txtId = itemView.findViewById(R.id.id_txt_task_id);
            txtDesc = itemView.findViewById(R.id.id_txt_task_desc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTasksEntity = mTasks.get(getLayoutPosition());

                    // Alert Dialok
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    TaskListRepository taskListRepository = TaskListRepository.getInstance();
                                    taskListRepository.delete(mTasksEntity.getId());
                                    swapItems(taskListRepository.findAll());
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Deseja EXCLUIR tarefa "+mTasksEntity.getId()+"?")
                            .setPositiveButton("Sim", dialogClickListener)
                            .setNegativeButton("Não", dialogClickListener).show();
                }
            });

        }
    }

}
