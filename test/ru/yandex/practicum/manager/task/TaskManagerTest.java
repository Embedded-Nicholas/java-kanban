package ru.yandex.practicum.manager.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.exception.TaskIntersectionException;
import ru.yandex.practicum.model.EpicTask;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;
import ru.yandex.practicum.status.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task;
    protected EpicTask epicTask;
    protected SubTask subTask;

    @BeforeEach
    void init() {
        this.taskManager = createTaskManager();
        this.task = new Task("Task", "Description", Duration.ofHours(2),
                LocalDateTime.of(2025, Month.JULY, 1, 12, 0));
        this.epicTask = new EpicTask("Epic", "Description");
        this.subTask = new SubTask("Subtask", "Description", this.epicTask.getId(),
                Duration.ofMinutes(45), LocalDateTime.of(2025,
                Month.JULY, 2, 12, 0));
        this.addTasks();
    }

    protected abstract T createTaskManager();

    protected void addTasks() {
        this.taskManager.add(task);
        this.taskManager.add(epicTask);
        this.taskManager.add(subTask);
    }

    @Test
    void getAllTasks() {
        ArrayList<Task> tasks = (ArrayList<Task>) taskManager.getAllTasks();
        assertAll(
                () -> assertEquals(3, taskManager.getAllTasks().size()),
                () -> assertEquals(tasks.get(0), task, "Should be simple task"),
                () -> assertEquals(tasks.get(1), epicTask, "Should be epic task"),
                () -> assertEquals(tasks.get(2), subTask, "Should be subtask")

        );
    }

    @Test
    void removeAllTasks() {
        taskManager.removeAllTasks();
        assertEquals(0, this.taskManager.getAllTasks().size(), "All tasks should be removed");
    }

    @Test
    void getTaskByUUID() {
        Task task = this.taskManager.getTaskByUUID(this.epicTask.getId());
        assertAll(
                () -> assertEquals(task.getId(), this.epicTask.getId(), "Tasks id should match"),
                () -> assertTrue(this.taskManager.getHistory().contains(task)),
                () -> assertEquals(1, this.taskManager.getHistory().size())
        );
    }

    @Test
    void updateTask() {
        this.taskManager.updateTask(this.task, Status.DONE);
        this.taskManager.updateTask(this.epicTask, Status.DONE);
        this.taskManager.updateTask(this.subTask, Status.IN_PROGRESS);

        assertAll(
                () -> assertSame(this.task.getStatus(), Status.DONE),
                () -> assertSame(this.epicTask.getStatus(), Status.IN_PROGRESS),
                () -> assertSame(this.subTask.getStatus(), Status.IN_PROGRESS)
        );
    }

    @Test
    void deleteEpicTaskByUUID() {
        taskManager.deleteTaskByUUID(this.epicTask.getId());
        assertEquals(1, this.taskManager.getAllTasks().size(), "Only simple task should remain");
    }

    @Test
    void getTasksByType() {
        SubTask additionalSubTask = new SubTask("additionalSubtask", "additionalDescription",
                this.epicTask.getId(), Duration.ofMinutes(45), LocalDateTime.of(2025, Month.JULY,
                5, 12, 0));
        this.taskManager.add(additionalSubTask);
        List<SubTask> subtasks = this.taskManager.getTasksByType(SubTask.class);
        assertAll(
                () -> assertTrue(subtasks.contains(additionalSubTask)),
                () -> assertTrue(subtasks.contains(this.subTask)),
                () -> assertEquals(2, subtasks.size())
        );

    }

    @Test
    void add() {
        assertAll(
                () -> assertTrue(this.taskManager.getTasksByType(Task.class).contains(task),
                        "Task should be present in the manager"),
                () -> assertTrue(this.taskManager.getTasksByType(EpicTask.class).contains(epicTask),
                        "Epic should be present in the manager"),
                () -> assertTrue(this.taskManager.getTasksByType(SubTask.class).contains(subTask),
                        "Subtask should be present in the manager"),
                () -> assertEquals(3, this.taskManager.getAllTasks().size(),
                        "Total tasks count should be 3")
        );
    }

    @Test
    void getHistory() {
        this.taskManager.getTaskByUUID(this.task.getId());
        this.taskManager.getTaskByUUID(this.epicTask.getId());
        this.taskManager.getTaskByUUID(this.subTask.getId());

        ArrayList<Task> tasks = (ArrayList<Task>) this.taskManager.getHistory();
        assertAll(
                () -> assertEquals(3, tasks.size()),
                () -> assertEquals(tasks.getFirst(), this.task),
                () -> assertEquals(tasks.get(1), this.epicTask),
                () -> assertEquals(tasks.getLast(), this.subTask)

        );
    }

    @Test
    void getPrioritizedTasks() {
        Set<Task> prioritizedTasks = this.taskManager.getPrioritizedTasks();
        Task[] arr = prioritizedTasks.toArray(Task[]::new);
        assertAll(
                () -> assertEquals(3, arr.length),
                () -> assertEquals(arr[0], this.task),
                () -> assertEquals(arr[1], this.epicTask),
                () -> assertEquals(arr[2], this.subTask)

        );
    }

    @Test
    void checkTaskIntersection() {
        Task taskWithIntersection = new Task(this.task);
        Assertions.assertThrows(TaskIntersectionException.class, () -> this.taskManager.add(taskWithIntersection));
    }
}
