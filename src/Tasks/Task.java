package Tasks;

import java.util.Objects;
import java.util.UUID;
import Enums.Status;

public class Task {
    protected final UUID id;
    protected String name;
    protected String description;
    protected Status status;

    public Task() {
        this.id = UUID.randomUUID();
        this.status = Status.NEW;
    }

    public Task(String name, String description, UUID taskUUID) {
        this.name = name;
        this.description = description;
        this.id = taskUUID;
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
        return Objects.equals(name, task.name)
                && Objects.equals(description, task.description)
                && Objects.equals(id, task.id)
                && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }

    @Override
    public String toString() {
        return STR."{name='\{name}', description='\{description}', taskUUID=\{id}, status=\{status}}";
    }
}
