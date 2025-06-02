package ru.yandex.practicum.util;

import ru.yandex.practicum.manager.history.HistoryManager;
import ru.yandex.practicum.manager.history.InMemoryHistoryManager;
import ru.yandex.practicum.manager.task.InMemoryTaskManager;
import ru.yandex.practicum.manager.task.TaskManager;

public class Managers {
    private Managers() {}

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }
}