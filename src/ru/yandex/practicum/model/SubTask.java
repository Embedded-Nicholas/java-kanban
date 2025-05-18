package ru.yandex.practicum.model;

import java.util.Objects;
import java.util.UUID;

public class SubTask extends Task {
    private final UUID epicTaskId;

    public SubTask(UUID epicTaskId) {
        super();
        this.epicTaskId = epicTaskId;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        SubTask subTask = (SubTask) obj;
        return Objects.equals(this.epicTaskId, subTask.epicTaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.epicTaskId);
    }

    public UUID getEpicTaskId() {
        return epicTaskId;
    }

    @Override
    public String toString() {
        return STR."\{super.toString()} {epicTaskId=\{epicTaskId}}";
    }
}
