package ru.yandex.practicum.manager.saver;

import ru.yandex.practicum.model.Task;

import java.nio.file.Path;
import java.util.Collection;

public interface Saver<T extends Task> {
    void save(Collection<T> data, Path path);
}
