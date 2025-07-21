package ru.yandex.practicum.manager.saver;

import ru.yandex.practicum.manager.exception.ManagerSaveException;
import ru.yandex.practicum.model.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

public class FileSaver<T extends Task> implements Saver<T> {
    private static final String FILE_HEADER = "id, type, name, description, status, epicId/SubtaskIdsList";

    @Override
    public void save(Collection<T> data, Path path) {
        try (BufferedWriter bf = new BufferedWriter(Files.newBufferedWriter(path))) {
            bf.write(FILE_HEADER + System.lineSeparator());
            for (T task : data) {
                bf.write(task.toString() + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Something went wrong while trying to save the file.");
        }
    }
}
