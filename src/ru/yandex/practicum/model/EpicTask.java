package ru.yandex.practicum.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EpicTask extends Task {
    private final List<UUID> subTasksIdList;

    private LocalDateTime endTime;

    public EpicTask(String name, String description) {
        super(name, description, null, null);
        this.subTasksIdList = new ArrayList<>();
        this.endTime = null;
    }

    public EpicTask(EpicTask original) {
        super(original);
        this.subTasksIdList = new ArrayList<>(original.getSubTasksIdList());
    }

    public EpicTask(Task task, List<UUID> subTasksIdList) {
        super(task);
        this.subTasksIdList = subTasksIdList;
    }

    public List<UUID> getSubTasksIdList() {
        return this.subTasksIdList;
    }

    public void addSubTaskById(UUID subTaskId) {
        if (!this.subTasksIdList.contains(subTaskId))
            this.subTasksIdList.add(subTaskId);
    }

    public void removeSubTaskId(UUID subTaskId) {
        this.subTasksIdList.remove(subTaskId);
    }

    public void clearSubTasks() {
        this.subTasksIdList.clear();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public Optional<LocalDateTime> getEndTime() {
        return Optional.ofNullable(this.endTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return String.format(
                "%s, %s",
                super.toString(),
                subTasksIdList.stream().toList()
        );
    }
}
