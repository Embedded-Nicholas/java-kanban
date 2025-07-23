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
import java.util.*;


public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private static final String FILE_HEADER = "id, type, name, description, status, epicId/SubtaskIdsList";
    private static final int EXTENDED_TASK_ELEMENTS = 6;
    private Path path = Paths.get("result.txt");

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
            for (Task task : this.getAllTasks()) {
                bf.write(task.toString() + System.lineSeparator());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Something went wrong while trying to save the file.");
        }
    }

    public static Collection<Task> loadFromFile(Path path) {
        Collection<Task> tasks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(Files.newBufferedReader(path))) {
            String line = br.readLine();
            while (br.ready()) {
                line = br.readLine();
                tasks.add(fromString(line));
            }
        } catch (IOException e) {
            e.getMessage();
        }
        return tasks;
    }

    public static Task fromString(String line) {
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
        return baseTask;
    }


    private static Task createBaseTask(String[] parts) {
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

    private static Task createSubTask(String[] parts, Task baseTask) {
        UUID epicId = UUID.fromString(parts[5]);
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
