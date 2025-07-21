package ru.yandex.practicum.manager.util;

import ru.yandex.practicum.model.EpicTask;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.status.Status;

import java.util.*;

public class StringToTaskConverter<T extends Task> {
    private static final int EXTENDED_TASK_ELEMENTS = 6;

    public T convert(String line) {
        Objects.requireNonNull(line, "Input line cannot be null");
        String[] parts = line.split(", ");

        Task baseTask = createBaseTask(parts);

        if (parts.length >= EXTENDED_TASK_ELEMENTS) {
            if (line.contains("[") && line.contains("]")) {
                return createEpicTask(line, baseTask);
            } else {
                return createSubTask(parts, baseTask);
            }
        }
        return (T) baseTask;
    }

    private Task createBaseTask(String[] parts) {
        try {
            UUID uuid = UUID.fromString(parts[0]);
            String name = parts[1];
            String description = parts[2];
            Status status = Status.fromString(parts[3]);
            return new Task(uuid, name, description, status);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse base task fields", e);
        }
    }

    private T createSubTask(String[] parts, Task baseTask) {
        UUID epicId = UUID.fromString(parts[5]);
        return (T) new SubTask(baseTask, epicId);
    }

    private T createEpicTask(String line, Task baseTask) {
        int listStart = line.indexOf("[");
        int listEnd = line.indexOf("]");
        String idsList = line.substring(listStart + 1, listEnd);

        List<UUID> subtaskIds = parseSubtaskIds(idsList);
        EpicTask epicTask = new EpicTask(baseTask, subtaskIds);
        return (T) (epicTask);
    }

    private List<UUID> parseSubtaskIds(String input) {
        if (input == null || input.trim().isEmpty() || input.equals("[]")) {
            return Collections.emptyList();
        }

        List<UUID> uuids = new ArrayList<>();
        String[] parts = input.split(",\\s*");

        for (String part : parts) {
            try {
                String uuidStr = part.trim();
                if (!uuidStr.isEmpty()) {
                    uuids.add(UUID.fromString(uuidStr));
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Fatal parsing UUID: '" + part + "'");
            }
        }

        return uuids;
    }
}