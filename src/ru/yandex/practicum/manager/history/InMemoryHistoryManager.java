package ru.yandex.practicum.manager.history;

import ru.yandex.practicum.manager.history.custom.list.CustomTaskLinkedList;
import ru.yandex.practicum.manager.history.custom.list.CustomTaskList;
import ru.yandex.practicum.model.EpicTask;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;

import java.util.*;


public class InMemoryHistoryManager <T extends Task> implements HistoryManager<T> {
    private final CustomTaskList<Task> taskHistory = new CustomTaskLinkedList();

    @Override
    public void add(T task) {
        this.addTaskCopy(task);
    }

    @Override
    public ArrayList<T> getHistory() {
        ArrayList<T> history = new ArrayList<>();
        for (Task task : this.taskHistory) {
            history.add((T) task);
        }
        return history;
    }

    @Override
    public void remove(UUID uuid) {
        this.taskHistory.remove(uuid);
    }

    @Override
    public void update(T task) {
        this.taskHistory.update(task);
    }

    @SuppressWarnings("unchecked")
    private void addTaskCopy(T task) {
        if (task instanceof EpicTask) {
            taskHistory.add((T) new EpicTask((EpicTask) task));
        } else if (task instanceof SubTask) {
            taskHistory.add((T) new SubTask((SubTask) task));
        } else {
            taskHistory.add((T) new Task(task));
        }
    }

}
