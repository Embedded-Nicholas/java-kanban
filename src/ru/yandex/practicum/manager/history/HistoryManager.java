package ru.yandex.practicum.manager.history;

import ru.yandex.practicum.model.Task;

import java.util.ArrayList;
import java.util.UUID;

public interface HistoryManager {
    void add(Task task);

    ArrayList<Task> getHistory();

    void remove(UUID uuid);

    void update(Task task);
}
