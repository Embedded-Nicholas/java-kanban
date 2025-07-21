package ru.yandex.practicum.manager.loader;

import ru.yandex.practicum.manager.util.StringToTaskConverter;
import ru.yandex.practicum.model.Task;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

public class FileLoader<T extends Task> implements Loader<T> {
    private static final StringToTaskConverter converter = new StringToTaskConverter();

    @Override
    public Collection<T> load(Path path) {
        Collection<T> tasks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
            String line = br.readLine();
            while (br.ready()) {
                line = br.readLine();
                tasks.add((T) converter.convert(line));
            }
        } catch (IOException e) {
            e.getMessage();
        }
        return tasks;
    }
}
