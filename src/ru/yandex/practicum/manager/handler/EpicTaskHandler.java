package ru.yandex.practicum.manager.handler;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.practicum.manager.task.TaskManager;
import ru.yandex.practicum.model.EpicTask;
import ru.yandex.practicum.model.SubTask;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class EpicTaskHandler extends BaseHttpHandler<EpicTask> {
    private static final int REQUEST_LENGTH_WITH_ID_PARAMETER_FOR_EPICS_SUBTASKS = 4;

    public EpicTaskHandler(TaskManager taskManager) {
        super(EpicTask.class, taskManager);
    }

    @Override
    protected void handleGetRequest(HttpExchange exchange) throws IOException {
        String[] components = this.parsePath(exchange);

        if (components.length == REQUEST_LENGTH_WITHOUT_ID_PARAMETER) {
            this.getTasks(exchange, this.gson, EpicTask.class);
        } else if (components.length == REQUEST_LENGTH_WITH_ID_PARAMETER) {
            this.getTaskById(exchange, components, this.gson);
        } else if (components.length == REQUEST_LENGTH_WITH_ID_PARAMETER_FOR_EPICS_SUBTASKS) {
            this.getEpicsSubtasks(exchange);
        }
    }

    private void getEpicsSubtasks(HttpExchange exchange) throws IOException {
        String[] components = this.parsePath(exchange);

        if (components.length == REQUEST_LENGTH_WITH_ID_PARAMETER_FOR_EPICS_SUBTASKS) {
            UUID uuid = UUID.fromString(components[2]);
            List<SubTask> epicsSubtasks = ((EpicTask) taskManager
                    .getTaskByUUID(uuid))
                    .getSubTasksIdList().stream()
                    .map(subtaskId -> (SubTask) taskManager.getTaskByUUID(subtaskId))
                    .collect(Collectors.toList());

            String response = this.gson.toJson(epicsSubtasks);
            this.sendText(exchange, response);
        }
    }
}
