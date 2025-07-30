package ru.yandex.practicum.manager.task.memory;

import ru.yandex.practicum.manager.exception.TaskIntersectionException;
import ru.yandex.practicum.manager.history.HistoryManager;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.manager.util.Managers;
import ru.yandex.practicum.model.EpicTask;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<UUID, Task> tasks = new LinkedHashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    public HistoryManager getHistoryManager() {
        return this.historyManager;
    }

    protected final Set<Task> prioritizedTasks = new TreeSet<>((task1, task2) -> {
        Optional<LocalDateTime> time1 = task1.getStartTime();
        Optional<LocalDateTime> time2 = task2.getStartTime();

        if (time1.isPresent() && time2.isPresent()) {
            int timeCompare = time1.get().compareTo(time2.get());

            if (timeCompare != 0) return timeCompare;

            String type1 = task1.getClass().getSimpleName();
            String type2 = task2.getClass().getSimpleName();

            int typeCompare = type1.compareTo(type2);

            if (typeCompare != 0) return typeCompare;

            return task1.getName().compareTo(task2.getName());
        }

        if (time1.isPresent()) return -1;

        if (time2.isPresent()) return 1;

        return task1.getName().compareTo(task2.getName());
    });

    protected HashMap<UUID, Task> getTasks() {
        return this.tasks;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    @Override
    public <T extends Task> List<T> getTasksByType(Class<T> taskType) {
        return this.tasks.values().stream()
                .filter(taskType::isInstance)
                .map(taskType::cast).collect(Collectors.toList());
    }

    @Override
    public void add(Task newTask) {
        if (checkTaskIntersection(newTask)) {
            throw new TaskIntersectionException("Task " + newTask + " cannot be added due to time intersection with existing tasks");
        }

        if (newTask instanceof SubTask subTask) {
            if (subTask.getId().equals(subTask.getEpicTaskId())) {
                throw new IllegalArgumentException("A subtask cannot be its own epic");
            }

            Task potentialEpic = this.tasks.get(subTask.getEpicTaskId());
            if (!(potentialEpic instanceof EpicTask)) {
                throw new IllegalStateException("Parent epic not found for subtask");
            }

            EpicTask epicTask = (EpicTask) potentialEpic;
            epicTask.addSubTaskById(subTask.getId());
            this.tasks.put(subTask.getId(), subTask);
            this.prioritizedTasks.add(subTask);
            initializeEpicTaskTimeFields(epicTask);
        } else {
            this.tasks.put(newTask.getId(), newTask);
            this.prioritizedTasks.add(newTask);
        }
    }

    private void initializeEpicTaskTimeFields(EpicTask epicTask) {
        this.prioritizedTasks.remove(epicTask);

        List<Task> subTaskList = this.prioritizedTasks.stream()
                .filter(task -> task instanceof SubTask subTask && subTask.getEpicTaskId()
                        .equals(epicTask.getId())).toList();

        if (subTaskList.isEmpty()) return;

        Optional<LocalDateTime> firstStartTime = subTaskList.getFirst().getStartTime();

        if (firstStartTime.isEmpty()) return;

        List<LocalDateTime> endTimes = subTaskList.stream()
                .map(Task::getEndTime)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        if (endTimes.isEmpty()) return;

        LocalDateTime epicStartLocalDateTime = firstStartTime.get();
        LocalDateTime epicEndLocalDateTime = endTimes.stream()
                .max(LocalDateTime::compareTo)
                .orElse(epicStartLocalDateTime);

        Duration duration = Duration.between(epicStartLocalDateTime, epicEndLocalDateTime);

        epicTask.setStartTime(epicStartLocalDateTime);
        epicTask.setDuration(duration);
        epicTask.setEndTime(epicEndLocalDateTime);

        this.prioritizedTasks.add(epicTask);
    }

    @Override
    public List<Task> getHistory() {
        return this.historyManager.getHistory();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return this.prioritizedTasks;
    }

    @Override
    public boolean checkTaskIntersection(Task taskToAdd) {
        return this.prioritizedTasks.stream().anyMatch(existingTask -> isOverlapping(existingTask, taskToAdd));
    }

    private boolean isOverlapping(Task taskToAdd, Task existingTask) {
        if (taskToAdd.getStartTime().isEmpty() || taskToAdd.getEndTime().isEmpty()
                || existingTask.getStartTime().isEmpty() || existingTask.getEndTime().isEmpty()) {
            return false;
        }

        LocalDateTime taskToAddStart = taskToAdd.getStartTime().get();
        LocalDateTime taskToAddEnd = taskToAdd.getEndTime().get();

        LocalDateTime existingStart = existingTask.getStartTime().get();
        LocalDateTime existingEnd = existingTask.getEndTime().get();

        boolean isOverlapping = (taskToAddStart.isAfter(existingStart) && taskToAddStart.isBefore(existingEnd))
                || (taskToAddEnd.isAfter(existingStart) && taskToAddEnd.isBefore(existingEnd))
                || (taskToAddStart.compareTo(existingStart) >= 0 && taskToAddEnd.compareTo(existingEnd) <= 0)
                || (taskToAddStart.compareTo(existingStart) <= 0 && taskToAddEnd.compareTo(existingEnd) >= 0);
        return isOverlapping;
    }

    @Override
    public void removeAllTasks() {
        this.tasks.clear();
    }

    @Override
    public Task getTaskByUUID(UUID uuid) {
        if (this.tasks.containsKey(uuid)) {
            this.historyManager.add(this.tasks.get(uuid));
        }
        return this.tasks.get(uuid);
    }

    @Override
    public void updateTask(Task task, Status newStatus) {
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
        if (task == null) {
            return;
        }

        switch (task) {
            case EpicTask epicTask -> deleteEpicTask(epicTask);
            case SubTask subTask -> deleteSubTask(subTask);
            default -> {
            }
        }

        if (isInHistory(task)) {
            this.historyManager.remove(uuid);
        }
        this.tasks.remove(uuid);
        this.prioritizedTasks.remove(task);
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

        this.tasks.put(epicTask.getId(), epicTask);
    }

    protected List<SubTask> getValidSubTasks(EpicTask epicTask) {
        return epicTask.getSubTasksIdList().stream().map(this.tasks::get).filter(SubTask.class::isInstance).map(SubTask.class::cast).collect(Collectors.toList());
    }

    protected void deleteEpicTask(EpicTask epicTask) {
        if (isInHistory(epicTask)) epicTask.getSubTasksIdList().forEach(this.historyManager::remove);
        epicTask.getSubTasksIdList().forEach(this.tasks::remove);
        epicTask.clearSubTasks();

    }

    protected void deleteSubTask(SubTask subTask) {
        Task potentialEpic = this.tasks.get(subTask.getEpicTaskId());
        if (potentialEpic instanceof EpicTask epicTask) {
            epicTask.removeSubTaskId(subTask.getId());
            this.historyManager.update(epicTask);
            this.prioritizedTasks.remove(subTask);
            this.initializeEpicTaskTimeFields(epicTask);
        }
    }
}
