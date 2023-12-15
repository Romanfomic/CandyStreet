import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

class Main {
    public static void main(String[] args) {
        String rootDirectory = System.getProperty("user.dir") + "/root";
        Map<String, String> fileList = new HashMap<>();

        // Создание списка файлов
        listFiles(rootDirectory, fileList);
        // Сортировка списка
        fileList = sortFiles(fileList);
        // Объединение контента файлов
        MergeFiles(fileList);
    }

    // Функция получения списка файлов
    private static void listFiles(String directory, Map<String, String> fileList) {
        File root = new File(directory);
        File[] files = root.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                // Рекурсия, если файл оказался папкой
                listFiles(file.getAbsolutePath(), fileList);
            } else {
                fileList.put(file.getName(), file.getAbsolutePath());
            }
        }
    }

    // Функция сортировки
    private static Map<String, String> sortFiles(Map<String, String> fileList) {
        // Сортировка по названию файла
        Map<String, String> sortedFileMap = new TreeMap<>(fileList);
        Map<String, String> newSortedMap = sortedFileMap;

        for (Map.Entry<String, String> entry : sortedFileMap.entrySet()) {
            try{
                File file = new File(entry.getValue());

                Scanner scanner = new Scanner(file);

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.contains("require")) {
                        // Получаем название файла из строки require 'FileName'
                        String message = line.replaceAll(".*'(.*)'.*", "$1");
                        int lastIndex = message.lastIndexOf('/');
                        String fileKey = message.substring(lastIndex + 1);

                        // LinkedHashMap для того, чтобы сохранить порядок
                        Map<String, String> newFileMap = new LinkedHashMap<>();

                        // Переменная, которая указывает, был ли уже добавлен элемент hieghestFile
                        boolean hieghestFile = false;

                        for (Map.Entry<String, String> entry_2 : newSortedMap.entrySet()) {
                            // Добавляем hieghestFile перед файлом, который ниже в списке
                            if (!hieghestFile && entry_2.getKey().equals(entry.getKey())) {
                                newFileMap.put(fileKey, sortedFileMap.get(fileKey));
                                hieghestFile = true;
                            }

                            // Добавляем текущий элемент в новую Map
                            newFileMap.put(entry_2.getKey(), entry_2.getValue());
                            newSortedMap = newFileMap;
                        }
                    }
                }

                scanner.close();
            } catch(FileNotFoundException e) {
                System.out.println("File is not found exeption: " + e.getMessage());
            }
        }
        return newSortedMap;
    }

    // Функция объединения файлов
    private static void MergeFiles(Map<String, String> fileList) {
        try {
            // Создаем или очищаем файл
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
            writer.close();

            // Записываем содержимое файлов в указанном порядке
            for (Map.Entry<String, String> entry : fileList.entrySet()) {
                String filePath = entry.getValue();
                File file = new File(filePath);

                Scanner scanner = new Scanner(file);

                String fileContent = "";
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    fileContent += line + "\n";
                }
                scanner.close();

                // Добавляем содержимое в файл
                appendToFile(fileContent);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Функция записи текста в файл
    private static void appendToFile(String content) throws IOException {
        // Добавление содержимого в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt", true))) {
            writer.write(content);
            writer.newLine();
        }
    }
}