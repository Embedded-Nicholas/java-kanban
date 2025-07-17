package ru.yandex.practicum.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void sameTaskSpecimensAreEqual() {
        UUID taskId = UUID.randomUUID();
        Task task1 = new Task("Task", "Description");
        Task task2 = new Task("Task", "Description");

        task1.setId(taskId);
        task2.setId(taskId);
        assertEquals(task1, task2);
    }

    @Test
    void sameTaskAncestorsSpecimensAreEqual() {
        UUID taskId = UUID.randomUUID();
        EpicTask epicTask = new EpicTask("Epic", "Description");

        SubTask subTask1 = new SubTask("Subtask", "Description", epicTask.getId());
        SubTask subTask2 = new SubTask("Subtask", "Description", epicTask.getId());

        subTask1.setId(taskId);
        subTask2.setId(taskId);
        assertEquals(subTask1, subTask2);
    }
}