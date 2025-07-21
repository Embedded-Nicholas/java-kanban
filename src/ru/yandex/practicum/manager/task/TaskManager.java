package ru.yandex.practicum.manager.task;

import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.status.Status;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface TaskManager<T extends Task> {
    List<T> getAllTasks();

    void removeAllTasks();

    T getTaskByUUID(UUID uuid);

    void updateTask(T task, Status newStatus);

    void deleteTaskByUUID(UUID uuid);

    <S extends T> List<S> getSpecialTypeTasks(Class<S> taskClass);

    void add(T newTask);

    Collection<T> getHistory();

    Collection<T> loadFromFile(Path path);

    void setFileName(Path fileName);
}
