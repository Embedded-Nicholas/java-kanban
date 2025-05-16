package TaskManager;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import Enums.Status;
import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;

public class TaskManager {
    private final HashMap<UUID, Task> tasks;

    public TaskManager() {
        tasks = new HashMap<>();
    }

    public <T extends Task> List<T> getSpecialTypeTasks(Class<T> taskClass) {
        return this.tasks.values().stream()
                .filter(task -> task.getClass().equals(taskClass))
                .map(taskClass::cast)
                .collect(Collectors.toList());
    }

    public <T extends Task> void createTask(T newTask) {
        if (newTask instanceof EpicTask epicTask) {
            this.tasks.put(epicTask.getTaskUUID(), epicTask);
            epicTask.getSubTasksList().forEach(subTask ->
                    this.tasks.put(subTask.getTaskUUID(), subTask));
        } else {
            this.tasks.put(newTask.getTaskUUID(), newTask);
        }
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    public void removeAllTasks() {
        this.tasks.clear();
    }

    public Task getTaskByUUID(UUID uuid) {
        return tasks.get(uuid);
    }

    public <T extends Task> void updateTask(T task, Status newStatus) {
        if (task instanceof EpicTask) return;
        task.setStatus(newStatus);
        tasks.put(task.getTaskUUID(), task);
    }

    public void deleteTaskByUUID(UUID uuid) {
        Task task = this.getTaskByUUID(uuid);
        if (task instanceof EpicTask) {
            ((EpicTask) task).getSubTasksList().forEach(
                    subtask -> this.tasks.remove(subtask.getTaskUUID())
            );
        }
        this.tasks.remove(uuid);
    }

    public List<SubTask> getEpicTaskSubTasks(EpicTask epicTask) {
        return epicTask.getSubTasksList();
    }
}
