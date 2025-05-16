package Tasks;

import Enums.Status;

public class SubTask extends Task {
    private EpicTask epicTask;

    public SubTask(EpicTask task) {
        super();
        this.epicTask = task;
        this.epicTask.addSubTask(this);
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
        this.epicTask.checkEpicTaskFulfilled();
    }

    @Override
    public String toString() {
        return STR."\{super.toString()} {epicTask=\{epicTask.getTaskUUID()}}";
    }
}
