package src.ru.yandex.practicum.manager.task.file;

import src.ru.yandex.practicum.manager.savemode.SaveMode;
import src.ru.yandex.practicum.model.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
//import ru.yandex.practicum.model.Task;
//import ru.yandex.practicum.status.Status;


public class FileBackedTaskManager {
    private static final String FILE_PATH = "result.txt";

    public static void save(SaveMode saveMode, Task task) {
        saveMode.save(FILE_PATH, task);
    }

    public static ArrayList<String> loadFromFile() {
        ArrayList<String> tasks = new ArrayList<>();
        Path path = Paths.get(FILE_PATH);
        if (!Files.exists(path)) {
            return new ArrayList<>();
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            while (reader.ready()) {
                String str = reader.readLine();
                tasks.add(str);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>();

//        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
//            Object obj = ois.readObject();
//            if (obj instanceof List) {
//                return (List<Task>) obj;
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        return new ArrayList<>();
    }
}
