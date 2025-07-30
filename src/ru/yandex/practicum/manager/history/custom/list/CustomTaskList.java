package ru.yandex.practicum.manager.history.custom.list;

import ru.yandex.practicum.model.Task;

import java.util.UUID;

public interface CustomTaskList<T extends Task> extends Iterable<T> {
    void add(T task);

    void remove(UUID taskId);

    void clear();

    void update(T task);

    T get(UUID taskId);

    int size();

    boolean contains(Task task);
}
