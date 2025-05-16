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
        List<? extends Task> tasksToAdd = newTask instanceof EpicTask
                ? ((EpicTask) newTask).getSubTasksList()
                : List.of(newTask);

        tasksToAdd.forEach(task -> this.tasks.put(task.getTaskUUID(), task));
    }

    public List<Task> getAllTasks() {
        return this.getSpecialTypeTasks(Task.class);
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
        this.tasks.remove(uuid);
    }

    public List<SubTask> getEpicTaskSubTasks(EpicTask epicTask) {
        return epicTask.getSubTasksList();
    }
}
