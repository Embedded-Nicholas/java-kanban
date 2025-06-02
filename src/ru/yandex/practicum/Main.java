package ru.yandex.practicum;

import ru.yandex.practicum.status.Status;
import ru.yandex.practicum.manager.task.InMemoryTaskManager;
import ru.yandex.practicum.model.EpicTask;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;

public class Main {

    public static void main(String[] args) {
        Task task1 = new Task("Задача1", "Описание1");
        Task task2 = new Task("Задача2", "Описание2");

        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        EpicTask epicTask1 = new EpicTask("Эпик", "ОписаниеЭпик");
        SubTask subTask1 = new SubTask("Подзадача1", "ОписаниеПодзадача1", epicTask1.getId());
        SubTask subTask2 = new SubTask("Подзадача2", "ОписаниеПодзадача2", epicTask1.getId());

        taskManager.add(epicTask1);
        taskManager.add(subTask1);
        taskManager.add(subTask2);

        taskManager.updateTask(subTask1, Status.DONE);
        taskManager.updateTask(subTask2, Status.IN_PROGRESS);

        System.out.println(epicTask1.getStatus());
    }
}
