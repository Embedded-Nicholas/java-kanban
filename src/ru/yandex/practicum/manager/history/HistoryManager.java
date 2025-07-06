package ru.yandex.practicum.manager.history;

import ru.yandex.practicum.model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public interface HistoryManager <T extends Task> {
    void add(T task);
    ArrayList<T> getHistory();
    void remove(UUID uuid);
    void update(T task);
}
