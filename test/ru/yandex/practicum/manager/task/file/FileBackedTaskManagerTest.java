package ru.yandex.practicum.manager.task.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.EpicTask;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private FileBackedTaskManager taskManager;
    private Task task;
    private EpicTask epicTask;
    private SubTask subTask;
    private Path path;

    @BeforeEach
    void init() throws IOException {
        this.path = Files.createTempFile("kanban", ".txt");
        this.taskManager = new FileBackedTaskManager(this.path);

        this.task = new Task("Task", "Description");
        this.epicTask = new EpicTask("Epic", "Description");
        this.subTask = new SubTask("Subtask", "Description", epicTask.getId());
    }

    @Test
    public void isFileClear() {
        Collection<Task> items = FileBackedTaskManager.loadFromFile(this.path);
        assertTrue(items.isEmpty());
    }

    @Test
    public void add() {
        this.addToTaskManager();
        List<Task> items = (List<Task>) FileBackedTaskManager.loadFromFile(this.path);
        assertAll(
                () -> assertEquals(items.get(0), this.task),
                () -> assertEquals(items.get(1), this.epicTask),
                () -> assertEquals(items.get(2), this.subTask),
                () -> assertEquals(items.size(), 3)
        );
    }

    @Test
    public void deleteTaskByUUID() {
        this.addToTaskManager();
        this.taskManager.deleteTaskByUUID(this.task.getId());
        assertTrue(this.taskManager.loadFromFile(this.path).size() == 2);

    }

    private void addToTaskManager() {
        this.taskManager.add(task);
        this.taskManager.add(epicTask);
        this.taskManager.add(subTask);
    }

}