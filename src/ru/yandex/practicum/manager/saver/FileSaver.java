package ru.yandex.practicum.manager.filesaver;

import ru.yandex.practicum.model.Task;

import java.io.*;
import java.util.Collection;

public class FileSaver<T extends Task> implements Saver<T> {
    private static final String FILE_NAME = "result.txt";

    @Override
    public void save(Collection<T> data) {
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(FILE_NAME,false))) {
            for (T task : data) {
                bf.write(task.toString() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Исключение");
        }
    }
}
