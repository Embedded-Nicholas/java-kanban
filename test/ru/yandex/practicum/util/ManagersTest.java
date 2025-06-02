package ru.yandex.practicum.util;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.history.HistoryManager;
import ru.yandex.practicum.manager.task.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    void isTaskManagerInstanceInitialized() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager);
    }

    @Test
    void isHistoryManagerInstanceInitialized() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager);
    }
}