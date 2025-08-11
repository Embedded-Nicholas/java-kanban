package ru.yandex.practicum.manager.taskserver;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.manager.handler.*;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.manager.util.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private HttpServer server;
    private final int port;

    public HttpTaskServer(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        final TaskManager taskManager = Managers.getDefault();
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/tasks", new TaskHandler(taskManager));
        server.createContext("/subtasks", new SubTaskHandler(taskManager));
        server.createContext("/epics", new EpicTaskHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
        server.start();
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    public static void main(String[] args) {
        HttpTaskServer server = new HttpTaskServer(8080);
        try {
            server.start();
            System.out.println("Server started on port 8080");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

