package ru.yandex.practicum.manager.history;

import ru.yandex.practicum.model.Task;

import java.util.Collection;

public interface HistoryManager {
    void add(Task task);
    Collection<Task> getHistory();
}
