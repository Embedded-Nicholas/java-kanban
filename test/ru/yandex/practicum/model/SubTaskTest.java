package ru.yandex.practicum.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.util.Managers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    private static TaskManager taskManager;

    @BeforeAll
    static void initialize() {
        taskManager = Managers.getDefault();
    }

    @Test
    void subCannotBeEpicOfItself() {
        EpicTask epic = new EpicTask("Эпик", "Описание");
        SubTask invalidSubTask = new SubTask("Подзадача", "Описание", epic.getId());
        invalidSubTask.setId(epic.getId());

        taskManager.add(epic);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskManager.add(invalidSubTask)
        );

        assertEquals("Эпик не может быть подзадачей самого себя или подзадача не может быть своим эпиком", exception.getMessage());

    }
}