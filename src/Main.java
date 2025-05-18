package Main;

import Enums.Status;
import TaskManager.TaskManager;
import Tasks.EpicTask;
import Tasks.SubTask;
import Tasks.Task;

public class Main {

    public static void main(String[] args) {
        Task task1 = new Task();
        Task task2 = new Task();

        TaskManager taskManager = new TaskManager();

        EpicTask epicTask1 = new EpicTask();
        SubTask subTask1 = new SubTask(epicTask1.getId());
        SubTask subTask2 = new SubTask(epicTask1.getId());


//        EpicTask epicTask2 = new EpicTask();
//        SubTask subTask3 = new SubTask(epicTask2);

//        taskManager.createTask(task1);
//        taskManager.createTask(task2);
        taskManager.createTask(epicTask1);
        taskManager.createTask(subTask1);
        taskManager.createTask(subTask2);

        taskManager.updateTask(subTask1, Status.DONE);
        taskManager.updateTask(subTask2, Status.IN_PROGRESS);
//        taskManager.createTask(epicTask2);

//        taskManager.deleteTaskByUUID(epicTask1.getId());
        System.out.println(epicTask1.getStatus());

    }
}
