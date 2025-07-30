package ru.yandex.practicum.manager.task.file;


import ru.yandex.practicum.manager.exception.ManagerSaveException;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.manager.task.memory.InMemoryTaskManager;
import ru.yandex.practicum.model.EpicTask;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.status.Status;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private static final String FILE_HEADER = "id, type, name, description, status, duration, start time, end time, epicId/SubtaskIdsList";
    private static final int EXTENDED_TASK_ELEMENTS = 6;
    private Path path = Paths.get("result.txt");

    public void setPath(Path path) {
        this.path = path;
    }

    public FileBackedTaskManager() {
    }

    public FileBackedTaskManager(Path path) {
        this.path = path;
    }

    @Override
    public void add(Task newTask) {
        super.add(newTask);
        this.save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        this.save();
    }

    @Override
    public void updateTask(Task task, Status newStatus) {
        super.updateTask(task, newStatus);
        this.save();
    }

    @Override
    public void deleteTaskByUUID(UUID uuid) {
        super.deleteTaskByUUID(uuid);
        this.save();
    }

    public void save() {
        try (BufferedWriter bf = new BufferedWriter(Files.newBufferedWriter(this.path))) {
            bf.write(FILE_HEADER + System.lineSeparator());
            this.getAllTasks().stream().forEach(task -> {
                try {
                    bf.write(task.toString() + System.lineSeparator());
                } catch (IOException e) {
                    throw new RuntimeException("Inner stream mistake");
                }
            });
        } catch (IOException e) {
            throw new ManagerSaveException("Something went wrong while trying to save the file.");
        }
    }

    public Collection<Task> loadFromFile() {
        try (BufferedReader br = new BufferedReader(Files.newBufferedReader(this.path))) {
            return br.lines().skip(1)
                    .map(line -> {
                        try {
                            return fromString(line);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                            return null;
                        }
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return Collections.emptyList();
    }

    private Task fromString(String line) {
        Objects.requireNonNull(line, "Input line cannot be null");
        String[] parts = line.split(", ");

        if (parts.length < 8) {
            throw new IllegalArgumentException("Invalid task string format. Expected at least 8 parts, got: " + parts.length);
        }

        String type = parts[1];

        switch (type) {
            case "Task":
                return createBaseTask(parts);
            case "EpicTask":
                return createEpicTask(line, createBaseTask(parts));
            case "SubTask":
                return createSubTask(parts, createBaseTask(parts));
            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }
    }

    private static Task createBaseTask(String[] parts) {
        try {
            UUID uuid = UUID.fromString(parts[0]);
            String type = parts[1];
            String name = parts[2];
            String description = parts[3];
            Status status = Status.fromString(parts[4]);
            Duration duration = parseDuration(parts[5]);
            LocalDateTime startTime = parseDateTime(parts[6]);

            return new Task(uuid, name, description, status, duration, startTime);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse base task fields", e);
        }
    }

    private static Duration parseDuration(String durationStr) {
        if (durationStr.equals("N/A") || durationStr.equals("null")) {
            return null;
        }

        String[] parts = durationStr.split(":");
        if (parts.length == 3) {
            long hours = Long.parseLong(parts[0]);
            long minutes = Long.parseLong(parts[1]);
            long seconds = Long.parseLong(parts[2]);
            return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
        }

        throw new IllegalArgumentException("Invalid duration format: " + durationStr);
    }

    private static LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr.equals("N/A")) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy|HH:mm");
        return LocalDateTime.parse(dateTimeStr, formatter);
    }

    private static Task createSubTask(String[] parts, Task baseTask) {
        if (parts.length < 9) {
            throw new IllegalArgumentException("SubTask requires epicId field at position 8");
        }
        UUID epicId = UUID.fromString(parts[8]);
        return new SubTask(baseTask, epicId);
    }

    private static Task createEpicTask(String line, Task baseTask) {
        int listStart = line.indexOf("[");
        int listEnd = line.indexOf("]");
        String idsList = line.substring(listStart + 1, listEnd);

        List<UUID> subtaskIds = parseSubtaskIds(idsList);
        EpicTask epicTask = new EpicTask(baseTask, subtaskIds);
        return epicTask;
    }

    private static List<UUID> parseSubtaskIds(String input) {
        if (input == null || input.trim().isEmpty() || input.equals("[]")) {
            return Collections.emptyList();
        }
        return Arrays.stream(input.split(",\\s*"))
                .map(String::trim)
                .filter(part -> !part.isEmpty())
                .map(part -> {
                    try {
                        return UUID.fromString(part);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Invalid UUID format: '" + part + "'");
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
