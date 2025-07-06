package ru.yandex.practicum.model;

import java.util.Objects;
import java.util.UUID;

import ru.yandex.practicum.status.Status;

public class Task {
    protected UUID id;
    protected String name;
    protected String description;
    protected Status status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.id = UUID.randomUUID();
        this.status = Status.NEW;
    }

    public Task(Task original) {
        this.id = original.id;
        this.name = original.name;
        this.description = original.description;
        this.status = original.status;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UUID getId() {
        return this.id;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return STR."{name='\{name}', description='\{description}', taskUUID=\{id}, status=\{status}}";
    }

}
