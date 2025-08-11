package ru.yandex.practicum.manager.util;

import ru.yandex.practicum.manager.history.HistoryManager;
import ru.yandex.practicum.manager.history.InMemoryHistoryManager;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.manager.task.file.FileBackedTaskManager;

public class Managers {
    private Managers() {
    }

    public static TaskManager getDefault() {
        return new FileBackedTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}