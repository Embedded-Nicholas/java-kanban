package ru.yandex.practicum.manager.handler;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.model.Task;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler {
    public PrioritizedHandler(TaskManager taskManager) {
        super(Task.class, taskManager);
    }

    @Override
    protected void handleGetRequest(HttpExchange exchange) throws IOException {
        String[] components = this.parsePath(exchange);

        if (components.length == REQUEST_LENGTH_WITHOUT_ID_PARAMETER) {
            this.sendText(exchange, this.gson.toJson(taskManager.getPrioritizedTasks()));
        }
    }
}
