package ru.yandex.practicum.manager.history;

import ru.yandex.practicum.model.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> taskHistory = new ArrayList<>();
    private static final int HISTORY_LIMIT = 10;

    @Override
    public void add(Task task){
        this.checkHistoryList(task);
    }

    @Override
    public Collection<Task> getHistory() {
        return this.taskHistory;
    }

    private void checkHistoryList(Task task) {
        try{
            Task taskCopy = (Task) task.clone();
            int size = this.taskHistory.size();
            if (size == HISTORY_LIMIT){
                this.taskHistory.removeFirst();
            }
            this.taskHistory.add(taskCopy);
        } catch (CloneNotSupportedException e) {
            e.getMessage();
        }

    }

}
