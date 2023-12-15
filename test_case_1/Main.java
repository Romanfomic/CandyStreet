import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

class Main {
    public static void main(String[] args) {
        String rootDirectory = System.getProperty("user.dir") + "/root";
        Map<String, String> fileList = new HashMap<>();

        listFiles(rootDirectory, fileList);
        fileList = sortFiles(fileList);

        for (Map.Entry<String, String> entry : fileList.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    private static void listFiles(String directory, Map<String, String> fileList) {
        File root = new File(directory);
        File[] files = root.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                listFiles(file.getAbsolutePath(), fileList);
            } else {
                fileList.put(file.getName(), file.getAbsolutePath());
            }
        }
    }
    private static Map<String, String> sortFiles(Map<String, String> fileList) {
        Map<String, String> sortedFileMap = new TreeMap<>(fileList);
        Map<String, String> newSortedMap = sortedFileMap;

        for (Map.Entry<String, String> entry : sortedFileMap.entrySet()) {
            try{
                File file = new File(entry.getValue());

                Scanner scanner = new Scanner(file);

                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.contains("require")) {
                        String message = line.replaceAll(".*'(.*)'.*", "$1");
                        int lastIndex = message.lastIndexOf('/');
                        String fileKey = message.substring(lastIndex + 1);

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
}