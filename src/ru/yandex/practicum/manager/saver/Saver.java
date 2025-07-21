package ru.yandex.practicum.manager.filesaver;

import ru.yandex.practicum.model.Task;

import java.util.Collection;

public interface Saver<T extends Task> {
    void save(Collection<T> data);
}
