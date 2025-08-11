package ru.yandex.practicum.model;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.manager.util.Managers;
import ru.yandex.practicum.status.Status;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EpicTaskTest {
    private EpicTask epicTask;
    private UUID subTask1Id;
    private UUID subTask2Id;
    private TaskManager taskManager;

    @BeforeEach
    void initialize() {
        epicTask = new EpicTask("Epic", "Description");
        taskManager = Managers.getDefault();

        taskManager.add(epicTask);

        SubTask subTask1 = new SubTask("Subtask", "Description", epicTask.getId(),
                null, null);
        SubTask subTask2 = new SubTask("Subtask", "Description", epicTask.getId(),
                null, null);

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
        EpicTask epic = new EpicTask("Epic", "Description");

        taskManager.add(epic);
        SubTask invalidSubTask = new SubTask("Subtask", "Description",
                epic.getId(), null, null);
        invalidSubTask.setId(epic.getId());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> taskManager.add(invalidSubTask)
        );

        assertEquals("A subtask cannot be its own epic", exception.getMessage());
    }

    @Test
    void checkEpicStatusIfSubtaskChanged() {
        Task subTask1 = this.taskManager.getTaskByUUID(this.subTask1Id);
        Task subTask2 = this.taskManager.getTaskByUUID(this.subTask2Id);

        this.taskManager.updateTask(subTask1, Status.DONE);
        this.taskManager.updateTask(subTask2, Status.DONE);
        assertEquals(Status.DONE, this.epicTask.getStatus());
    }
}