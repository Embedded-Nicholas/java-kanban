package ru.yandex.practicum.manager.history.custom.list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.model.EpicTask;
import ru.yandex.practicum.model.SubTask;
import ru.yandex.practicum.model.Task;

import static org.junit.jupiter.api.Assertions.*;

class CustomTaskLinkedListTest {

    private CustomTaskLinkedList list;
    private Task task;
    private EpicTask epicTask;
    private SubTask subTask;

    @BeforeEach
    void setUp() {
        this.list = new CustomTaskLinkedList();
        task = new Task("Task", "Description");
        epicTask = new EpicTask("Epic", "Description");
        subTask = new SubTask("Subtask", "Description", epicTask.getId());
    }

    @Test
    void add() {
        this.list.add(task);
        this.list.add(epicTask);
        this.list.add(subTask);
        assertEquals(3, this.list.size());
    }

    @Test
    void remove() {
        this.add();
        this.list.remove(task.getId());
        assertAll(
                () -> assertEquals(this.list.getHead(), this.epicTask),
                () -> assertEquals(this.list.getTail(), this.subTask),
                () -> assertEquals(this.list.size(), 2)
        );
    }

    @Test
    void clear() {
        this.add();
        this.list.clear();
        assertAll(
                () -> assertNull(this.list.get(task.getId())),
                () -> assertNull(this.list.get(epicTask.getId())),
                () -> assertNull(this.list.get(subTask.getId())),
                () -> assertEquals(this.list.size(), 0)
        );
    }
}