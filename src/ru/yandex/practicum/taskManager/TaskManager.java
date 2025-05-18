package ru.yandex.practicum.taskManager;

import java.util.*;
import java.util.stream.Collectors;
import ru.yandex.practicum.status.Status;
import ru.yandex.practicum.model.EpicTask;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;

public class TaskManager {
    private final HashMap<UUID, Task> tasks = new HashMap<>();

    public TaskManager() {}

    public List<Task> getAllTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    public <T extends Task> List<T> getSpecialTypeTasks(Class<T> taskClass) {
        return tasks.values().stream()
                .filter(task -> task.getClass().equals(taskClass))
                .map(taskClass::cast)
                .collect(Collectors.toList());
    }

    public <T extends Task> void createTask(T newTask) {
        if (newTask instanceof SubTask subTask) {
            Task potentialEpic = this.tasks.get(subTask.getEpicTaskId());
            if (potentialEpic instanceof EpicTask epicTask) {
                epicTask.addSubTaskById(subTask.getId());
            } else {
                throw new IllegalStateException("Parent epic not found or wrong type");
            }
        }
        tasks.put(newTask.getId(), newTask);
    }

    public void removeAllTasks() {
        this.tasks.clear();
    }

    public Task getTaskByUUID(UUID uuid) {
        return this.tasks.get(uuid);
    }

    public <T extends Task> void updateTask(T task, Status newStatus) {
        if (task == null || task instanceof EpicTask) {
            return;
        }

        task.setStatus(newStatus);
        tasks.put(task.getId(), task);

        if (task instanceof SubTask subTask) {
            updateEpicStatusForSubTask(subTask);
        }
    }

    private void updateEpicStatusForSubTask(SubTask subTask) {
        UUID epicId = subTask.getEpicTaskId();
        Task potentialEpic = tasks.get(epicId);

        if (potentialEpic instanceof EpicTask epicTask) {
            updateEpicStatus(epicTask);
        }
    }

    private void updateEpicStatus(EpicTask epicTask) {
        List<SubTask> subTasks = getValidSubTasks(epicTask);

        if (subTasks.isEmpty()) {
            return;
        }

        boolean allDone = subTasks.stream().allMatch(st -> st.getStatus() == Status.DONE);
        boolean anyInProgress = subTasks.stream().anyMatch(st -> st.getStatus() == Status.IN_PROGRESS);

        if (allDone) {
            epicTask.setStatus(Status.DONE);
        } else if (anyInProgress) {
            epicTask.setStatus(Status.IN_PROGRESS);
        } else {
            epicTask.setStatus(Status.NEW);
        }

        tasks.put(epicTask.getId(), epicTask);
    }

    private List<SubTask> getValidSubTasks(EpicTask epicTask) {
        return epicTask.getSubTasksIdList().stream()
                .map(tasks::get)
                .filter(SubTask.class::isInstance)
                .map(SubTask.class::cast)
                .collect(Collectors.toList());
    }

    public void deleteTaskByUUID(UUID uuid) {
        Task task = this.tasks.get(uuid);
        switch (task) {
            case null -> {
                return;
            }
            case EpicTask epicTask -> deleteEpicTask(epicTask);
            case SubTask subTask -> deleteSubTask(subTask);
            default -> {
            }
        }

        this.tasks.remove(uuid);
    }

    private void deleteEpicTask(EpicTask epicTask) {
        new ArrayList<>(epicTask.getSubTasksIdList()).forEach(this.tasks::remove);
        epicTask.clearSubTasks();
    }

    private void deleteSubTask(SubTask subTask) {
        Task potentialEpic = this.tasks.get(subTask.getEpicTaskId());
        if (potentialEpic instanceof EpicTask epicTask) {
            epicTask.removeSubTaskId(subTask.getId());
        }
    }
}
