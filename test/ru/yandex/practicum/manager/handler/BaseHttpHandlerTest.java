package ru.yandex.practicum.manager.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.*;
import ru.yandex.practicum.adapter.DurationAdapter;
import ru.yandex.practicum.adapter.LocalDateTimeAdapter;
import ru.yandex.practicum.manager.taskserver.HttpTaskServer;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.status.Status;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class BaseHttpHandlerTest {
    private static HttpTaskServer taskServer;
    private static Gson gson;
    private static HttpClient client;

    @BeforeAll
    public static void init() throws IOException {
        taskServer = new HttpTaskServer(8080);
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .serializeNulls()
                .create();
        client = HttpClient.newHttpClient();
        taskServer.start();
    }

    @BeforeEach
    void setUp() throws IOException {
        taskServer.stop();
        taskServer.start();
    }

    private Task createTask() throws IOException, InterruptedException {
        Task task = new Task(null, "Task", "Desc", Status.NEW, null, null);
        HttpResponse<String> resp = sendPost("http://localhost:8080/tasks", gson.toJson(task));
        assertEquals(200, resp.statusCode());
        return gson.fromJson(resp.body(), Task.class);
    }

    private EpicTask createEpic() throws IOException, InterruptedException {
        EpicTask epic = new EpicTask("Epic", "Desc");
        HttpResponse<String> resp = sendPost("http://localhost:8080/epics", gson.toJson(epic));
        assertEquals(200, resp.statusCode());
        return gson.fromJson(resp.body(), EpicTask.class);
    }

    private SubTask createSubtask(UUID epicId) throws IOException, InterruptedException {
        SubTask subtask = new SubTask("Sub", "Desc", epicId, Duration.ofMinutes(30), LocalDateTime.now());
        HttpResponse<String> resp = sendPost("http://localhost:8080/subtasks", gson.toJson(subtask));
        assertEquals(200, resp.statusCode());
        return gson.fromJson(resp.body(), SubTask.class);
    }

    private HttpResponse<String> sendGet(String url) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> sendPost(String url, String body) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> sendDelete(String url) throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).DELETE().build();
        return client.send(req, HttpResponse.BodyHandlers.ofString());
    }

    private String randomUUIDUrl(String base) {
        return base + "/" + UUID.randomUUID();
    }

    @Test
    @Order(1)
    void testGetTasks200() throws Exception {
        createTask();
        HttpResponse<String> resp = sendGet("http://localhost:8080/tasks");
        assertEquals(200, resp.statusCode());
    }

    @Test
    @Order(2)
    void testGetTaskById404() throws Exception {
        HttpResponse<String> resp = sendGet(randomUUIDUrl("http://localhost:8080/tasks"));
        assertEquals(404, resp.statusCode());
    }

    @Test
    @Order(3)
    void testPostTask201And406() throws Exception {
        Task t1 = new Task(null, "T1", "D1", Status.NEW, Duration.ofMinutes(10), LocalDateTime.now());
        HttpResponse<String> resp1 = sendPost("http://localhost:8080/tasks", gson.toJson(t1));
        assertEquals(200, resp1.statusCode());

        Task t2 = new Task(null, "T2", "D2", Status.NEW, Duration.ofMinutes(5), t1.getStartTime().get());
        HttpResponse<String> resp2 = sendPost("http://localhost:8080/tasks", gson.toJson(t2));
        assertEquals(406, resp2.statusCode());
    }

    @Test
    @Order(4)
    void testDeleteTask200and404() throws Exception {
        Task t = createTask();
        HttpResponse<String> resp1 = sendDelete("http://localhost:8080/tasks/" + t.getId());
        assertEquals(200, resp1.statusCode());

        HttpResponse<String> resp2 = sendDelete(randomUUIDUrl("http://localhost:8080/tasks"));
        assertEquals(404, resp2.statusCode());
    }


    @Test
    @Order(5)
    void testGetSubtasks200() throws Exception {
        EpicTask epic = createEpic();
        createSubtask(epic.getId());
        HttpResponse<String> resp = sendGet("http://localhost:8080/subtasks");
        assertEquals(200, resp.statusCode());
    }

    @Test
    @Order(6)
    void testGetSubtaskById404() throws Exception {
        HttpResponse<String> resp = sendGet(randomUUIDUrl("http://localhost:8080/subtasks"));
        assertEquals(404, resp.statusCode());
    }

    @Test
    @Order(7)
    void testPostSubtask201And406() throws Exception {
        EpicTask epic = createEpic();

        SubTask s1 = new SubTask("S1", "D1", epic.getId(), Duration.ofMinutes(10), LocalDateTime.now());
        HttpResponse<String> resp1 = sendPost("http://localhost:8080/subtasks", gson.toJson(s1));
        System.out.println(resp1.statusCode());

        SubTask s2 = new SubTask("S2", "D2", epic.getId(), Duration.ofMinutes(5), s1.getStartTime().get());
        HttpResponse<String> resp2 = sendPost("http://localhost:8080/subtasks", gson.toJson(s2));
        assertEquals(406, resp2.statusCode());
    }

    @Test
    @Order(8)
    void testDeleteSubtask200and404() throws Exception {
        EpicTask epic = createEpic();
        SubTask s = createSubtask(epic.getId());
        HttpResponse<String> resp1 = sendDelete("http://localhost:8080/subtasks/" + s.getId());
        assertEquals(200, resp1.statusCode());

        HttpResponse<String> resp2 = sendDelete(randomUUIDUrl("http://localhost:8080/subtasks"));
        assertEquals(404, resp2.statusCode());
    }

    @Test
    @Order(9)
    void testGetEpics200() throws Exception {
        createEpic();
        HttpResponse<String> resp = sendGet("http://localhost:8080/epics");
        assertEquals(200, resp.statusCode());
    }

    @Test
    @Order(10)
    void testGetEpicById404() throws Exception {
        HttpResponse<String> resp = sendGet(randomUUIDUrl("http://localhost:8080/epics"));
        assertEquals(404, resp.statusCode());
    }

    @Test
    @Order(11)
    void testGetEpicSubtasks200and404() throws Exception {
        EpicTask epic = createEpic();
        createSubtask(epic.getId());
        HttpResponse<String> resp1 = sendGet("http://localhost:8080/epics/" + epic.getId() + "/subtasks");
        assertEquals(200, resp1.statusCode());

        HttpResponse<String> resp2 = sendGet(randomUUIDUrl("http://localhost:8080/epics") + "/subtasks");
        assertEquals(404, resp2.statusCode());
    }

    @Test
    @Order(12)
    void testDeleteEpic200and404() throws Exception {
        EpicTask epic = createEpic();
        HttpResponse<String> resp1 = sendDelete("http://localhost:8080/epics/" + epic.getId());
        assertEquals(200, resp1.statusCode());

        HttpResponse<String> resp2 = sendDelete(randomUUIDUrl("http://localhost:8080/epics"));
        assertEquals(404, resp2.statusCode());
    }

    @Test
    @Order(13)
    void testGetHistory200() throws Exception {
        createTask();
        HttpResponse<String> resp = sendGet("http://localhost:8080/history");
        assertEquals(200, resp.statusCode());
    }

    @Test
    @Order(14)
    void testGetPrioritizedTasks200() throws Exception {
        createTask();
        HttpResponse<String> resp = sendGet("http://localhost:8080/prioritized");
        assertEquals(200, resp.statusCode());
    }

    @Test
    @Order(15)
    void testUpdateTaskSuccess200() throws Exception {
        Task task = createTask();

        Task updatedTask = new Task(
                task.getId(),
                "Updated Task",
                "Updated Desc",
                Status.IN_PROGRESS,
                Duration.ofHours(1),
                LocalDateTime.now().plusDays(1)
        );

        HttpResponse<String> response = sendPost("http://localhost:8080/tasks", gson.toJson(updatedTask));

        assertEquals(200, response.statusCode());

        Task receivedTask = gson.fromJson(response.body(), Task.class);
        assertEquals("Updated Task", receivedTask.getName());
        assertEquals("Updated Desc", receivedTask.getDescription());
        assertEquals(Status.IN_PROGRESS, receivedTask.getStatus());
        assertEquals(Duration.ofHours(1), receivedTask.getDuration().orElse(null));
    }

    @Test
    @Order(16)
    void testUpdateNonExistentTask404() throws Exception {
        Task nonExistentTask = new Task(
                UUID.randomUUID(),
                "Non-existent",
                "Task",
                Status.NEW,
                null,
                null
        );

        HttpResponse<String> response = sendPost("http://localhost:8080/tasks", gson.toJson(nonExistentTask));

        assertEquals(404, response.statusCode());
    }

    @Test
    @Order(17)
    void testUpdateSubTaskSuccess200() throws Exception {
        EpicTask epic = createEpic();
        SubTask subTask = createSubtask(epic.getId());

        SubTask updatedSubTask = new SubTask(
                subTask.getId(),
                "Updated Sub",
                "Updated Desc",
                Status.DONE,
                epic.getId(),
                Duration.ofHours(2),
                LocalDateTime.now().plusDays(2)
        );

        HttpResponse<String> response = sendPost("http://localhost:8080/subtasks", gson.toJson(updatedSubTask));

        assertEquals(200, response.statusCode());

        SubTask receivedSubTask = gson.fromJson(response.body(), SubTask.class);
        assertEquals("Updated Sub", receivedSubTask.getName());
        assertEquals("Updated Desc", receivedSubTask.getDescription());
        assertEquals(Duration.ofHours(2), receivedSubTask.getDuration().get());
    }

    @Test
    @Order(18)
    void testUpdateNonExistentSubTask404() throws Exception {
        EpicTask epic = createEpic();

        SubTask nonExistentSubTask = new SubTask(
                UUID.randomUUID(),
                "Non-existent",
                "SubTask",
                Status.NEW,
                epic.getId(),
                null,
                null
        );

        HttpResponse<String> response = sendPost("http://localhost:8080/subtasks", gson.toJson(nonExistentSubTask));

        assertEquals(404, response.statusCode());
    }

    @Test
    @Order(19)
    void testUpdateNonExistentEpic404() throws Exception {
        EpicTask nonExistentEpic = new EpicTask(
                "Non-existent",
                "Epic"
        );
        nonExistentEpic.setId(UUID.randomUUID());

        HttpResponse<String> response = sendPost("http://localhost:8080/epics", gson.toJson(nonExistentEpic));

        assertEquals(404, response.statusCode());
    }

    @Test
    @Order(19)
    void testGetUpdatedEpic() throws Exception {
        EpicTask epic = createEpic();
        SubTask subTask = createSubtask(epic.getId());

        HttpResponse<String> response = sendPost("http://localhost:8080/subtasks", gson.toJson(subTask));
        assertEquals(200, response.statusCode());

        subTask.setStatus(Status.DONE);
        response = sendPost("http://localhost:8080/subtasks", gson.toJson(subTask));

        assertEquals(200, response.statusCode());
        SubTask receivedSubTask = gson.fromJson(response.body(), SubTask.class);
        assertEquals(subTask, receivedSubTask);

        response = sendGet("http://localhost:8080/epics/" + epic.getId());
        assertEquals(200, response.statusCode());
        EpicTask retrievedEpic = gson.fromJson(response.body(), EpicTask.class);
        assertEquals(retrievedEpic, epic);
    }

    @Test
    @Order(20)
    public void getEpicsSubTasks() throws IOException, InterruptedException {
        EpicTask epicTask = createEpic();
        SubTask s1 = new SubTask("S1", "D1", epicTask.getId(), Duration.ofMinutes(10), LocalDateTime.now());
        SubTask s2 = new SubTask("S2", "D2", epicTask.getId(), Duration.ofMinutes(10), LocalDateTime.now().plusDays(1));

        HttpResponse<String> response = sendPost("http://localhost:8080/subtasks", gson.toJson(s1));
        assertEquals(200, response.statusCode());

        SubTask retrievedSubtask1 = gson.fromJson(response.body(), SubTask.class);

        response = sendPost("http://localhost:8080/subtasks", gson.toJson(s2));
        assertEquals(200, response.statusCode());

        SubTask retrievedSubtask2 = gson.fromJson(response.body(), SubTask.class);

        response = sendGet("http://localhost:8080/epics/" + epicTask.getId() + "/subtasks");
        assertEquals(200, response.statusCode());

        List<SubTask> subtasks = gson.fromJson(response.body(), new TypeToken<List<SubTask>>() {
        }.getType());
        assertEquals(2, subtasks.size());
        assertEquals(retrievedSubtask1, subtasks.get(0));
        assertEquals(retrievedSubtask2, subtasks.get(1));
    }
}