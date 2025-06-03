package ru.yandex.practicum.model;

import java.util.Objects;
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

    public UUID getEpicTaskId() {
        return epicTaskId;
    }

    @Override
    public String toString() {
        return STR."\{super.toString()} {epicTaskId=\{epicTaskId}}";
    }
}
