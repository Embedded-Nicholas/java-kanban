package Tasks;

import Enums.Status;

import java.util.ArrayList;
import java.util.List;

public class EpicTask extends Task {
    private final List<SubTask> subTasksList;

    public EpicTask() {
        super();
        this.subTasksList = new ArrayList<>();
    }

    public EpicTask(List<SubTask> subTaskList) {
        super();
        this.subTasksList = subTaskList;
    }

    public List<SubTask> getSubTasksList() {
        return this.subTasksList;
    }

    void addSubTask(SubTask subTask) {
        this.subTasksList.add(subTask);
    }

    public void deleteSubTask(SubTask subTask) {
        this.subTasksList.remove(subTask);
    }

    void checkEpicTaskFulfilled() {
        boolean allSubTasksDone = this.subTasksList
                .stream()
                .allMatch(subTask -> subTask.getStatus() == Status.DONE);

        boolean epicTaskHasSubTasksInProcessOrDone = this.subTasksList.
                stream().
                anyMatch(subTask -> subTask.getStatus().equals(Status.IN_PROGRESS)
                        || subTask.getStatus().equals(Status.DONE));

        if (allSubTasksDone) {
            this.setStatus(Status.DONE);
        } else if (epicTaskHasSubTasksInProcessOrDone) {
            this.status = Status.IN_PROGRESS;
        }
    }

    @Override
    public String toString() {
        return STR."\{super.toString()} {subTaskIds=\{subTasksList.stream().map(Task::getTaskUUID).toList()}}";
    }
}
