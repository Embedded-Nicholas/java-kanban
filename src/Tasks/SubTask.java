package Tasks;

import Enums.Status;

import java.util.UUID;

public class SubTask extends Task {
    private final UUID epicTaskId;

    public SubTask(UUID epicTaskId) {
        super();
        this.epicTaskId = epicTaskId;
    }

//    public void setStatus(Status status, EpicTask epicTask) {
//        super.setStatus(status);
//        epicTask.checkEpicTaskFulfilled();
//    }

    public UUID getEpicTaskId() {
        return epicTaskId;
    }

    @Override
    public String toString() {
        return STR."\{super.toString()} {epicTaskId=\{epicTaskId}}";
    }
}
