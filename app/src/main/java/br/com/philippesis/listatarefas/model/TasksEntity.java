package br.com.philippesis.listatarefas.model;

public class TasksEntity {

    private long mId;
    private String mTaskDesc;

    private TasksEntity(long id, String taskDesc) {

        this.mId = id;
        this.mTaskDesc = taskDesc;
    }

    public static class Builder {

        private long id;
        private String taskDesc;

        public Builder setId(long id) {
            this.id = id;
            return this;
        }

        public Builder setTaskDesc(String taskDesc) {
            this.taskDesc = taskDesc;
            return this;
        }

        public TasksEntity build() {
            return new TasksEntity(id, taskDesc);
        }

    }

    public long getId() {
        return mId;
    }

    public String getTaskDesc() {
        return mTaskDesc;
    }
}
