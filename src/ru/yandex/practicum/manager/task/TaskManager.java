package ru.yandex.practicum.manager.task;

import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.status.Status;

import java.util.List;
import java.util.UUID;

public interface TaskManager {
    List<Task> getAllTasks();

    void removeAllTasks();

    Task getTaskByUUID(UUID uuid);

    void updateTask(Task task, Status newStatus);

    void deleteTaskByUUID(UUID uuid);

    public <T extends Task> List<T> getTasksByType(Class<T> taskType);

    void add(Task newTask);

    List<Task> getHistory();
}
