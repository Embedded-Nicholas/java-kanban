package ru.yandex.practicum.manager.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.model.Task;

import java.io.IOException;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {
    public TaskHandler() {
        super();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        List<Task> tasks = this.taskManager.getTasksByType(Task.class);

        Gson gson = new Gson();
        String response = gson.toJson(tasks);
        this.sendText(exchange, response);
    }
}
