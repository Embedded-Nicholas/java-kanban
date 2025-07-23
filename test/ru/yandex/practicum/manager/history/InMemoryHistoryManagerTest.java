package ru.yandex.practicum.manager.history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.manager.task.memory.InMemoryTaskManager;
import ru.yandex.practicum.manager.util.Managers;
import ru.yandex.practicum.model.Task;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private TaskManager taskManager;

    @BeforeEach
    void init(){
        this.taskManager = Managers.getDefault();
    }

    @Test
    void editedDataShouldBeSavedInHistoryList(){
        Task task = new Task("Task", "Description");
        taskManager.add(task);
        taskManager.getTaskByUUID(task.getId());
        task.setName("Changed task");
        task.setDescription("Changed description");

        Task retrievedTask = (Task) taskManager.getHistory().stream().toList().getFirst();
        assertAll(
                () -> assertEquals("Task", retrievedTask.getName()),
                () -> assertEquals("Description", retrievedTask.getDescription())
        );
    }

}