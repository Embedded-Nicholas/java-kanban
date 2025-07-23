package ru.yandex.practicum.model;

import ru.yandex.practicum.status.Status;

import java.util.UUID;

public class SubTask extends Task {
    private final UUID epicTaskId;

    public SubTask(String name, String description, UUID epicTaskId) {
        super(name, description);
        this.epicTaskId = epicTaskId;
    }

    public SubTask(SubTask original) {
        super(original);
        this.epicTaskId = original.epicTaskId;
    }

    public SubTask(Task task, UUID epicTaskId) {
        super(task);
        this.epicTaskId = epicTaskId;
    }

    public SubTask(UUID id, String name, String description, Status status, UUID epicTaskId) {
        super(id, name, description, status);
        this.epicTaskId = epicTaskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public UUID getEpicTaskId() {
        return epicTaskId;
    }

    @Override
    public String toString() {
        return String.format(
                "%s, %s",
                super.toString(),
                epicTaskId
        );
    }
}
