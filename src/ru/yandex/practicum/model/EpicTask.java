package ru.yandex.practicum.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EpicTask extends Task {
    private final List<UUID> subTasksList;

    public EpicTask() {
        super();
        this.subTasksList = new ArrayList<>();
    }

    public EpicTask(List<UUID> subTaskList) {
        super();
        this.subTasksList = subTaskList;
    }

    public List<UUID> getSubTasksIdList() {
        return this.subTasksList;
    }

    public void addSubTaskById(UUID subTaskId) {
        if (!this.subTasksList.contains(subTaskId))
            this.subTasksList.add(subTaskId);
    }

    public void removeSubTaskId(UUID subTaskId) {
        subTasksList.remove(subTaskId);
    }

    public void clearSubTasks(){
        this.subTasksList.clear();
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        EpicTask epicTask = (EpicTask) obj;
        return Objects.equals(this.subTasksList, epicTask.getSubTasksIdList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.subTasksList);
    }

    @Override
    public String toString() {
        return STR."\{super.toString()} {subTaskIds=\{subTasksList.stream().toList()}}";
    }
}
