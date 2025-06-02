package ru.yandex.practicum.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class EpicTask extends Task {
    private final List<UUID> subTasksList;

    public EpicTask(String name, String description) {
        super(name, description);
        this.subTasksList = new ArrayList<>();
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
        return STR."\{super.toString()} {subTaskIds=\{subTasksList.stream().toList()}}";
    }
}
