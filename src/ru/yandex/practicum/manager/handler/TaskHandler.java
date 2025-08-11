package ru.yandex.practicum.manager.handler;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.model.Task;

import java.io.IOException;

public class TaskHandler extends BaseHttpHandler {
    public TaskHandler(TaskManager taskManager) {
        super(Task.class, taskManager);
    }

    @Override
    protected void handleGetRequest(HttpExchange exchange) throws IOException {
        String[] components = parsePath(exchange);

        if (components.length == REQUEST_LENGTH_WITHOUT_ID_PARAMETER) {
            this.getTasks(exchange, this.gson, Task.class);
        }

        if (components.length == REQUEST_LENGTH_WITH_ID_PARAMETER) {
            this.getTaskById(exchange, components, this.gson);
        }
    }
}
