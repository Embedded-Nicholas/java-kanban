package ru.yandex.practicum.manager.history;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.task.InMemoryTaskManager;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.model.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    void init(){
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void editedDataShouldBeSavedInHistoryList(){
        Task task = new Task("Задача", "Описание");
        taskManager.add(task);
        taskManager.getTaskByUUID(task.getId());
        task.setName("Измененная задача");
        task.setDescription("Измененное описание");

        Task retrievedTask = taskManager.getHistory().stream().toList().getFirst();
        assertAll(
                () -> assertEquals("Задача", retrievedTask.getName()),
                () -> assertEquals("Описание", retrievedTask.getDescription())
        );
    }

}