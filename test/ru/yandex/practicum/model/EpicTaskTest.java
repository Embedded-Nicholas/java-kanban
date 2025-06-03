package ru.yandex.practicum.model;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.manager.util.Managers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
class EpicTaskTest {

    private EpicTask epicTask;
    private UUID subTask1Id;
    private UUID subTask2Id;
    private TaskManager taskManager;

    @BeforeEach
    void initialize() {
        epicTask = new EpicTask("Эпик", "Описание");
        taskManager = Managers.getDefault();

        SubTask subTask1 = new SubTask("Подзадача", "Описание", epicTask.getId());
        SubTask subTask2 = new SubTask("Подзадача", "Описание", epicTask.getId());

        taskManager.add(epicTask);
        taskManager.add(subTask1);
        taskManager.add(subTask2);

        subTask1Id = subTask1.getId();
        subTask2Id = subTask2.getId();
    }

    @Test
    void getSubTasksIdList() {
        UUID[] subTasks = epicTask.getSubTasksIdList().toArray(UUID[]::new);
        assertArrayEquals(new UUID[]{subTask1Id, subTask2Id}, subTasks);
    }

    @Test
    void removeSubTaskId() {
        epicTask.removeSubTaskId(subTask1Id);
        UUID[] subTasks = epicTask.getSubTasksIdList().toArray(UUID[]::new);
        assertArrayEquals(new UUID[]{subTask2Id}, subTasks);
    }


    @Test
    void clearSubTasks() {
        EpicTask tempEpic = epicTask;
        tempEpic.addSubTaskById(subTask1Id);
        tempEpic.addSubTaskById(subTask2Id);

        tempEpic.removeSubTaskId(subTask1Id);
        UUID[] subTasks = tempEpic.getSubTasksIdList().toArray(UUID[]::new);
        assertArrayEquals(new UUID[]{subTask2Id}, subTasks);
    }

    @Test
    void epicCannotBeSubtaskOfItself() {
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