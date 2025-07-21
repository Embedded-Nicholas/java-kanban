package ru.yandex.practicum.manager.task.file;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.manager.util.Managers;
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
    private TaskManager taskManager;
    private Task task;
    private EpicTask epicTask;
    private SubTask subTask;
    private Path path;

    @BeforeEach
    void init() throws IOException {
        this.path = Files.createTempFile("kanban", ".txt");
        this.taskManager = Managers.getDefault();
        this.taskManager.setFileName(path);

        this.task = new Task("Task", "Description");
        this.epicTask = new EpicTask("Epic", "Description");
        this.subTask = new SubTask("Subtask", "Description", epicTask.getId());
    }

    @Test
    public <T extends Task> void isFileClear() {
        Collection<T> items = this.taskManager.loadFromFile(this.path);
        assertTrue(items.isEmpty());
    }

    @Test
    public <T extends Task> void add() throws InterruptedException, IOException {
        this.addToTaskManager();
        List<T> items = (List<T>) this.taskManager.loadFromFile(this.path);
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