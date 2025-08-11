package ru.yandex.practicum.manager.task.memory;


import ru.yandex.practicum.manager.task.TaskManagerTest;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }
}