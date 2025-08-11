package ru.yandex.practicum.manager.task.file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.task.TaskManagerTest;
import ru.yandex.practicum.model.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    @Test
    public void isFileClear() throws IOException {
        this.taskManager.setPath(Files.createTempFile("kanban", ".txt"));
        Collection<Task> items = this.taskManager.loadFromFile();
        this.taskManager.setPath(Path.of("result.txt"));
        assertTrue(items.isEmpty());
    }

    @Test
    public void save() {
        Assertions.assertDoesNotThrow(() -> {
            this.taskManager.save();
        });
    }

    @Test
    public void loadFromFile() {
        for (Task task : this.taskManager.getAllTasks()) {
            System.out.println(task);
        }
        this.taskManager.save();

        List<Task> tasks = this.taskManager.loadFromFile().stream().toList();
        assertAll(
                () -> assertEquals(3, tasks.size()),
                () -> assertEquals(tasks.getFirst(), this.task),
                () -> assertEquals(tasks.get(1), this.epicTask),
                () -> assertEquals(tasks.getLast(), this.subTask)
        );
    }

    @Override
    protected FileBackedTaskManager createTaskManager() {
        return new FileBackedTaskManager(Path.of("result.txt"));
    }
}