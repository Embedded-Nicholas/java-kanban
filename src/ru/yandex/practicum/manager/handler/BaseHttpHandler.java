package ru.yandex.practicum.manager.handler;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.manager.util.Managers;

import java.io.IOException;
import java.io.OutputStream;

public abstract class BaseHttpHandler {
    protected static final int RESPONSE_CODE = 200;
    protected TaskManager taskManager;

    protected BaseHttpHandler() {
        this.taskManager = Managers.getDefault();
    }

    protected void sendText(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
