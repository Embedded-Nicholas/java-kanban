package ru.yandex.practicum.manager.loader;

import ru.yandex.practicum.model.Task;

import java.nio.file.Path;
import java.util.Collection;

public interface Loader<T extends Task> {
    Collection<T> load(Path path);
}
