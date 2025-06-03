package ru.yandex.practicum.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EpicTask extends Task {
    private final List<UUID> subTasksIdList;

    public EpicTask(String name, String description) {
        super(name, description);
        this.subTasksIdList = new ArrayList<>();
    }

    public EpicTask(EpicTask original){
        super(original);
        this.subTasksIdList = new ArrayList<>();
    }

    public List<UUID> getSubTasksIdList() {
        return this.subTasksIdList;
    }

    public void addSubTaskById(UUID subTaskId) {
        if (!this.subTasksIdList.contains(subTaskId))
            this.subTasksIdList.add(subTaskId);
    }

    public void removeSubTaskId(UUID subTaskId) {
        subTasksIdList.remove(subTaskId);
    }

    public void clearSubTasks(){
        this.subTasksIdList.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return STR."\{super.toString()} {subTaskIds=\{subTasksIdList.stream().toList()}}";
    }
}
