package ru.yandex.practicum;

import ru.yandex.practicum.status.Status;
import ru.yandex.practicum.manager.task.InMemoryTaskManager;
import ru.yandex.practicum.model.EpicTask;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;

import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        Task task1 = new Task("Task1", "Description1");
        Task task2 = new Task("Task2", "Description2");

        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        EpicTask epicTask1 = new EpicTask("Epic1", "EpicDescription1");
        SubTask subTask1 = new SubTask("Subtask1", "SubtaskDescription1", epicTask1.getId());
        SubTask subTask2 = new SubTask("Subtask2", "SubtaskDescription2", epicTask1.getId());
        SubTask subTask3 = new SubTask("Subtask3", "SubtaskDescription3", epicTask1.getId());

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

        List<String> taskStrings = taskManager.getHistory().stream()
                .map(Task::toString)
                .toList();

        System.out.println(String.join("\n", taskStrings));

        System.out.println();

        taskManager.deleteTaskByUUID(subTask1.getId());
        List<String> taskStrings2 = taskManager.getHistory().stream()
                .map(Task::toString)
                .toList();
        System.out.println(String.join("\n", taskStrings2));


        System.out.println(epicTask1.getStatus());
    }
}
