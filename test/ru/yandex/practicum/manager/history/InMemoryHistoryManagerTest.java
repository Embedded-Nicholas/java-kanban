package ru.yandex.practicum.manager.history;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.exception.NotFoundException;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.manager.util.Managers;
import ru.yandex.practicum.model.Task;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private TaskManager taskManager;
    private Task task1;
    private Task task2;

    @BeforeEach
    void init() {
        this.task1 = new Task("Task1", "Description1", null, null);
        this.task2 = new Task("Task2", "Description2", null, null);
        this.taskManager = Managers.getDefault();
    }

    @Test
    public void checkHistoryWithoutAddingToManager() {
        assertThrows(NotFoundException.class, () -> {
            this.taskManager.getTaskByUUID(this.task1.getId());
        });

        assertFalse(this.taskManager.getHistory().contains(this.task1));
    }

    @Test
    void editedDataShouldBeSavedInHistoryList() {
        taskManager.add(this.task1);
        taskManager.getTaskByUUID(this.task1.getId());

        this.task1.setName("Changed task");
        this.task1.setDescription("Changed description");

        Task retrievedTask = this.taskManager.getHistory().stream().toList().getFirst();
        assertAll(
                () -> assertEquals("Task1", retrievedTask.getName()),
                () -> assertEquals("Description1", retrievedTask.getDescription())
        );
    }

    @Test
    public void add() {
        this.taskManager.add(this.task1);
        taskManager.getTaskByUUID(this.task1.getId());
        assertTrue(this.taskManager.getHistory().contains(this.task1));
    }

    @Test
    public void addTaskDuplicate() {
        this.taskManager.add(this.task1);

        this.taskManager.getTaskByUUID(task1.getId());

        assertAll(
                () -> assertTrue(this.taskManager.getHistory().contains(task1)),
                () -> assertEquals(1, this.taskManager.getHistory().size())
        );
    }

    @Test
    public void getHistory() {
        this.taskManager.add(this.task1);
        this.taskManager.add(this.task2);

        this.taskManager.getTaskByUUID(task1.getId());
        this.taskManager.getTaskByUUID(task2.getId());

        assertAll(
                () -> assertTrue(taskManager.getHistory().contains(task1)),
                () -> assertTrue(taskManager.getHistory().contains(task2))
        );
    }

    @Test
    public void update() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(this.task1);

        this.task1.setName("Changed task");
        historyManager.update(this.task1);

        assertAll(
                () -> assertEquals(this.task1.getName(), historyManager.getHistory().getFirst().getName()),
                () -> assertEquals(1, historyManager.getHistory().size())
        );
    }

    @Test
    public void addTaskCopy() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        String nameBefore = this.task1.getName();

        historyManager.add(this.task1);

        this.task1.setName("Changed task");

        assertEquals(nameBefore, historyManager.getHistory().getFirst().getName());
    }

    @Test
    public void removeTaskAtFirstPosition() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        Task additionalTask = new Task("Task3", "Description3", null, null);

        this.task1.setId(UUID.randomUUID());
        this.task2.setId(UUID.randomUUID());
        additionalTask.setId(UUID.randomUUID());

        historyManager.add(this.task1);
        historyManager.add(this.task2);
        historyManager.add(additionalTask);

        historyManager.remove(this.task1.getId());

        assertEquals(historyManager.getHistory().size(), 2);
    }

}