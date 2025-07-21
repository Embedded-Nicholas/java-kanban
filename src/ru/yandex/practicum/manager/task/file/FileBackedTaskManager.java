package ru.yandex.practicum.manager.task.file;


import ru.yandex.practicum.manager.loader.FileLoader;
import ru.yandex.practicum.manager.saver.FileSaver;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.manager.task.memory.InMemoryTaskManager;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.status.Status;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.UUID;


public class FileBackedTaskManager<T extends Task> extends InMemoryTaskManager<T> implements TaskManager<T> {
    private Path currentFilePath  = Paths.get("result.txt");
    private static final FileSaver fileSaver = new FileSaver();

    @Override
    public void add(T newTask) {
        super.add(newTask);
        this.save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        this.save();
    }

    @Override
    public void updateTask(T task, Status newStatus) {
        super.updateTask(task, newStatus);
        this.save();
    }

    @Override
    public void deleteTaskByUUID(UUID uuid) {
        super.deleteTaskByUUID(uuid);
        this.save();
    }

    @Override
    public Collection<T> loadFromFile(Path path) {
        FileLoader fileLoader = new FileLoader();
        Collection<T> tasks = fileLoader.load(path);
        return tasks;
    }

    public void setFileName(Path fileName) {
        currentFilePath = fileName;
    }

    private void save() {
        fileSaver.save(this.getAllTasks(),currentFilePath);
    }
}
