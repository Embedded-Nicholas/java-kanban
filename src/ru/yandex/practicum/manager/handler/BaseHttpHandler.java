package ru.yandex.practicum.manager.handler;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.adapter.DurationAdapter;
import ru.yandex.practicum.adapter.LocalDateTimeAdapter;
import ru.yandex.practicum.manager.exception.NotFoundException;
import ru.yandex.practicum.manager.exception.TaskIntersectionException;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class BaseHttpHandler<T extends Task> implements HttpHandler {
    private final Class<T> taskType;
    protected static final int OK_RESPONSE_CODE = 200;
    protected static final int NOT_FOUND_RESPONSE_CODE = 404;
    protected static final int INTERSECTION_RESPONSE_CODE = 406;
    protected static final int INTERNAL_SERVER_ERROR_RESPONSE_CODE = 500;

    protected static final String HAS_INTERSECTION_MESSAGE = "Not Acceptable";
    protected static final String INTERNAL_BAD_MESSAGE = "Internal Server Error";

    protected static final int REQUEST_LENGTH_WITHOUT_ID_PARAMETER = 2;
    protected static final int REQUEST_LENGTH_WITH_ID_PARAMETER = 3;

    protected static TaskManager taskManager;
    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .serializeNulls()
            .create();


    protected BaseHttpHandler(Class<T> taskType, TaskManager taskManagerToInitialize) {
        this.taskType = taskType;
        taskManager = taskManagerToInitialize;
    }

    @Override
    public void handle(HttpExchange exchange) {
        try {
            switch (exchange.getRequestMethod()) {
                case "GET" -> this.handleGetRequest(exchange);
                case "POST" -> this.handlePostRequest(exchange, this.taskType);
                case "DELETE" -> this.handleDeleteRequest(exchange);
                default -> exchange.sendResponseHeaders(405, -1);
            }
        } catch (NotFoundException e) {
            try {
                this.sendNotFound(exchange, e.getMessage());
            } catch (IOException ignored) {
            }
        } catch (TaskIntersectionException e) {
            try {
                this.sendHasInteractions(exchange);
            } catch (IOException ignored) {
            }
        } catch (JsonSyntaxException | IOException e) {
            try {
                this.sendInternalServerError(exchange);
            } catch (IOException ignored) {
            }
        } finally {
            exchange.close();
        }
    }

    protected void sendText(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        this.createResponse(exchange, OK_RESPONSE_CODE, response);
    }

    protected void sendNotFound(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "text/plain");
        this.createResponse(exchange, NOT_FOUND_RESPONSE_CODE, response);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "text/plain");
        this.createResponse(exchange, INTERSECTION_RESPONSE_CODE, HAS_INTERSECTION_MESSAGE);
    }

    protected void sendInternalServerError(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "text/plain");
        this.createResponse(exchange, INTERNAL_SERVER_ERROR_RESPONSE_CODE, INTERNAL_BAD_MESSAGE);
    }

    private void createResponse(HttpExchange exchange, int code, String response) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(code, response.getBytes().length);
            os.write(response.getBytes());
        }
    }

    protected <T extends Task> void getTasks(HttpExchange exchange, Gson gson, Class<T> type) throws IOException {
        String response = gson.toJson(taskManager.getTasksByType(type));
        this.sendText(exchange, response);
    }

    protected void getTaskById(HttpExchange exchange, String[] components, Gson gson) throws NotFoundException, IOException {
        UUID id = UUID.fromString(components[2]);
        Task task = taskManager.getTaskByUUID(id);
        String response = gson.toJson(task);
        this.sendText(exchange, response);
    }

    protected String[] parsePath(HttpExchange exchange) {
        URI uri = exchange.getRequestURI();
        return uri.getPath().split("/");
    }

    protected <T extends Task> void handlePostRequest(HttpExchange exchange, Class<T> taskType)
            throws IOException, JsonSyntaxException, TaskIntersectionException {
        if (exchange.getRequestBody() == null) {
            this.sendInternalServerError(exchange);
        }

        try (InputStream inputStream = exchange.getRequestBody();
             InputStreamReader reader = new InputStreamReader(inputStream)) {

            JsonElement jsonElement = JsonParser.parseReader(reader);

            if (!jsonElement.isJsonObject()) {
                this.sendInternalServerError(exchange);
            }

            JsonObject jsonTask = jsonElement.getAsJsonObject();
            T task = this.gson.fromJson(jsonTask, taskType);

            if (jsonTask.has("id") && !jsonTask.get("id").isJsonNull()) {
                taskManager.updateTask(task, task.getStatus());
                this.sendText(exchange, this.gson.toJson(taskManager.getTaskByUUID(task.getId())));
            } else {
                taskManager.add(task);
                this.sendText(exchange, this.gson.toJson(taskManager.getTaskByUUID(task.getId())));
            }
        }
    }

    protected void handleDeleteRequest(HttpExchange exchange) throws NotFoundException, IOException {
        String[] components = this.parsePath(exchange);
        if (components.length == REQUEST_LENGTH_WITH_ID_PARAMETER) {
            UUID id = UUID.fromString(components[2]);
            taskManager.deleteTaskByUUID(id);
            this.sendText(exchange, this.gson.toJson(id));
        }
    }

    protected abstract void handleGetRequest(HttpExchange exchange) throws IOException;
}
