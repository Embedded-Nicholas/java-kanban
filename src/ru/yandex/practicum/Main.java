package ru.yandex.practicum;

import ru.yandex.practicum.status.Status;
import ru.yandex.practicum.taskManager.TaskManager;
import ru.yandex.practicum.model.EpicTask;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;

import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        Task task1 = new Task();
        Task task2 = new Task();

        TaskManager taskManager = new TaskManager();

        EpicTask epicTask1 = new EpicTask();
        SubTask subTask1 = new SubTask(epicTask1.getId());
        SubTask subTask2 = new SubTask(epicTask1.getId());

        taskManager.createTask(epicTask1);
        taskManager.createTask(subTask1);
        taskManager.createTask(subTask2);

        taskManager.updateTask(subTask1, Status.DONE);
        taskManager.updateTask(subTask2, Status.IN_PROGRESS);

        System.out.println(epicTask1.getStatus());
    }
}
