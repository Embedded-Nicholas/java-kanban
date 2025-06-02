package ru.yandex.practicum.manager.task;

import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.status.Status;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface TaskManager {
    List<Task> getAllTasks();
    void removeAllTasks();
    Task getTaskByUUID(UUID uuid);
    <T extends Task> void updateTask(T task, Status newStatus);
    void deleteTaskByUUID(UUID uuid);
    <T extends Task> List<T> getSpecialTypeTasks(Class<T> taskClass);
    <T extends Task> void add(T newTask);
    Collection<Task> getHistory();
}
