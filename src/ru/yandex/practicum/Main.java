package ru.yandex.practicum;

import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.manager.util.Managers;
import ru.yandex.practicum.model.EpicTask;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Task task1 = new Task("Task1", "Description1", null, null);
        Task task2 = new Task("Task2", "Description2", Duration.ofMinutes(25), LocalDateTime.of(2025, Month.JULY, 1, 11, 55));

        TaskManager taskManager = Managers.getDefault();

        EpicTask epicTask1 = new EpicTask("Epic1", "EpicDescription1");
        SubTask subTask1 = new SubTask("Subtask1", "SubtaskDescription1", epicTask1.getId(), Duration.ofHours(2), LocalDateTime.of(2025, Month.JULY, 1, 12, 20));
        SubTask subTask2 = new SubTask("Subtask2", "SubtaskDescription2", epicTask1.getId(), Duration.ofHours(2), LocalDateTime.of(2025, Month.JULY, 2, 18, 45));
        SubTask subTask3 = new SubTask("Subtask3", "SubtaskDescription3", epicTask1.getId(), Duration.ofHours(2), LocalDateTime.of(2025, Month.JULY, 3, 18, 59));

        EpicTask epicTask2 = new EpicTask("Epic2", "EpicDescription2");

        taskManager.add(task1);
        taskManager.add(task2);

        taskManager.add(epicTask1);
        taskManager.add(subTask1);
        taskManager.add(subTask2);
        taskManager.add(subTask3);

        taskManager.add(epicTask2);

        taskManager.getTaskByUUID(task1.getId());
        taskManager.getTaskByUUID(task2.getId());
        taskManager.getTaskByUUID(epicTask1.getId());
        taskManager.getTaskByUUID(subTask1.getId());
        taskManager.getTaskByUUID(subTask2.getId());
        taskManager.getTaskByUUID(subTask3.getId());
        taskManager.getTaskByUUID(epicTask2.getId());

        List<String> taskStrings = taskManager.getHistory().stream()
                .map(task -> task.toString())
                .toList();

        System.out.println(String.join("\n", taskStrings));

        System.out.println();

        List<String> taskStrings2 = taskManager.getHistory().stream()
                .map(task -> task.toString())
                .toList();
        System.out.println(String.join("\n", taskStrings2));


        System.out.println(epicTask1.getStatus());
        System.out.println(taskManager.getPrioritizedTasks());
    }
}
