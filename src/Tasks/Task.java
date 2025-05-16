package Tasks;

import java.util.Objects;
import java.util.UUID;
import Enums.Status;

public class Task {
    protected String name;
    protected String description;
    protected UUID taskUUID;
    protected Status status;

    public Task() {
        this.taskUUID = UUID.randomUUID();
        this.status = Status.NEW;
    }

    public Task(String name, String description, UUID taskUUID, Status status) {
        this.name = name;
        this.description = description;
        this.taskUUID = taskUUID;
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

    public UUID getTaskUUID() {
        return this.taskUUID;
    }

    public void setTaskUUID(UUID taskUUID) {
        this.taskUUID = taskUUID;
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
                && Objects.equals(taskUUID, task.taskUUID)
                && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, taskUUID, status);
    }

    @Override
    public String toString() {
        return STR."{name='\{name}', description='\{description}', taskUUID=\{taskUUID}, status=\{status}}";
    }
}
