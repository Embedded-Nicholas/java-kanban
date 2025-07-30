package ru.yandex.practicum.manager.history;

import ru.yandex.practicum.manager.history.custom.list.CustomTaskLinkedList;
import ru.yandex.practicum.manager.history.custom.list.CustomTaskList;
import ru.yandex.practicum.model.EpicTask;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {
    private final CustomTaskList<Task> taskHistory = new CustomTaskLinkedList();

    @Override
    public void add(Task task) {
        if (!this.taskHistory.contains(task))
            this.addTaskCopy(task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> history = new ArrayList<>();
        for (Task task : this.taskHistory) {
            history.add(task);
        }
        return history;
    }

    @Override
    public void remove(UUID uuid) {
        this.taskHistory.remove(uuid);
    }

    @Override
    public void update(Task task) {
        this.taskHistory.update(task);
    }

    private void addTaskCopy(Task task) {
        if (task instanceof EpicTask) {
            taskHistory.add(new EpicTask((EpicTask) task));
        } else if (task instanceof SubTask) {
            taskHistory.add(new SubTask((SubTask) task));
        } else {
            taskHistory.add(new Task(task));
        }
    }

}
