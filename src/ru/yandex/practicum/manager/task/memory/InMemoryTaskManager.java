package ru.yandex.practicum.manager.task.memory;

import ru.yandex.practicum.manager.history.HistoryManager;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.manager.util.Managers;
import ru.yandex.practicum.model.EpicTask;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.status.Status;

import java.util.*;
import java.util.stream.Collectors;


public abstract class InMemoryTaskManager<T extends Task> implements TaskManager<T> {
    protected final HashMap<UUID, T> tasks = new LinkedHashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    protected HashMap<UUID, T> getTasks() {
        return this.tasks;
    }

    @Override
    public List<T> getAllTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    @Override
    public <S extends T> List<S> getSpecialTypeTasks(Class<S> taskClass) {
        return this.tasks.values().stream()
                .filter(task -> task.getClass().equals(taskClass))
                .map(taskClass::cast)
                .collect(Collectors.toList());
    }

    @Override
    public void add(T newTask) {
        if (newTask instanceof SubTask subTask) {
            if (subTask.getId().equals(subTask.getEpicTaskId())) {
                throw new IllegalArgumentException("An epic cannot be a subtask of itself, and a subtask cannot be its own epic");
            }

            Task potentialEpic = this.tasks.get(subTask.getEpicTaskId());
            if (potentialEpic instanceof EpicTask epicTask) {
                epicTask.addSubTaskById(subTask.getId());
            } else {
                throw new IllegalStateException("An Epic-parent not found");
            }
        }
        this.tasks.put(newTask.getId(), newTask);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<T> getHistory() {
        return this.historyManager.getHistory();
    }

    @Override
    public void removeAllTasks() {
        this.tasks.clear();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getTaskByUUID(UUID uuid) {
        if (this.tasks.containsKey(uuid)) {
            this.historyManager.add(this.tasks.get(uuid));
        }
        return this.tasks.get(uuid);
    }

    @Override
    public void updateTask(T task, Status newStatus) {
        if (task == null || task instanceof EpicTask) {
            return;
        }

        task.setStatus(newStatus);
        this.tasks.put(task.getId(), task);

        if (task instanceof SubTask subTask) {
            updateEpicStatusForSubTask(subTask);
        }
    }

    @Override
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

        if (isInHistory(task)) {
            this.historyManager.remove(uuid);
        }
        this.tasks.remove(uuid);
    }

    protected boolean isInHistory(Task task) {
        return this.historyManager.getHistory().stream().toList().contains(task);
    }

    protected void updateEpicStatusForSubTask(SubTask subTask) {
        UUID epicId = subTask.getEpicTaskId();
        Task potentialEpic = this.tasks.get(epicId);

        if (potentialEpic instanceof EpicTask epicTask) {
            updateEpicStatus(epicTask);
        }
    }

    protected void updateEpicStatus(EpicTask epicTask) {
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

        this.tasks.put(epicTask.getId(), (T) epicTask);
    }

    protected List<SubTask> getValidSubTasks(EpicTask epicTask) {
        return epicTask.getSubTasksIdList().stream()
                .map(this.tasks::get)
                .filter(SubTask.class::isInstance)
                .map(SubTask.class::cast)
                .collect(Collectors.toList());
    }

    protected void deleteEpicTask(EpicTask epicTask) {
        if (isInHistory(epicTask))
            epicTask.getSubTasksIdList().forEach(this.historyManager::remove);

        epicTask.getSubTasksIdList().forEach(this.tasks::remove);
        epicTask.clearSubTasks();

    }

    protected void deleteSubTask(SubTask subTask) {
        Task potentialEpic = this.tasks.get(subTask.getEpicTaskId());
        if (potentialEpic instanceof EpicTask epicTask) {
            epicTask.removeSubTaskId(subTask.getId());
            this.historyManager.update(epicTask);
        }
    }
}
