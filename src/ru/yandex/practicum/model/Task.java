package ru.yandex.practicum.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import ru.yandex.practicum.status.Status;

public class Task {
    protected UUID id;
    protected String type;
    protected String name;
    protected String description;
    protected Status status;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(UUID id, String name, String description, Status status,
                Duration duration, LocalDateTime startTime) {
        this.id = id != null ? id : UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.status = status != null ? status : Status.NEW;
        this.type = this.getClass().getSimpleName();
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, Duration duration, LocalDateTime startTime) {
        this(null, name, description, null, duration, startTime);
    }

    public Task(Task original) {
        this(original.id, original.name, original.description,
                original.status, original.duration, original.startTime);
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

    public Optional<Duration> getDuration() {

        return Optional.ofNullable(duration);
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Optional<LocalDateTime> getStartTime() {
        return Optional.ofNullable(this.startTime);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Optional<LocalDateTime> getEndTime() {
        return this.startTime != null && this.duration != null ? Optional.of(this.startTime.plus(duration)) : Optional.empty();
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
        return String.format(
                "%s, %s, %s, %s, %s, %s, %s, %s",
                this.id, this.type, this.name, this.description, this.status,
                this.formatDuration(this.duration), this.getFormatted(this.startTime), this.getEndTime().map(this::getFormatted).orElse("N/A")
        );
    }

    private String getFormatted(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy|HH:mm");
        String formatDateTime = Optional.ofNullable(time)
                .map(t -> t.format(formatter))
                .orElse("N/A");
        return formatDateTime;
    }

    private String formatDuration(Duration duration) {
        String formattedDuration = Optional.ofNullable(duration)
                .map(time -> {
                    long hours = duration.toHours();
                    int minutes = (int) (duration.toMinutes() % 60);
                    int seconds = (int) (duration.getSeconds() % 60);

                    return String.format("%02d:%02d:%02d", hours, minutes, seconds);
                })
                .orElse("N/A");
        return formattedDuration;
    }
}
