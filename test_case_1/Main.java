import java.io.File;
import java.util.HashMap;
import java.util.Map;

class Main {
    public static void main(String[] args) {
        String rootDirectory = System.getProperty("user.dir") + "/root";
        Map<String, String> fileList = new HashMap<>();

        listFiles(rootDirectory, fileList);

        for (Map.Entry<String, String> entry : fileList.entrySet()) {
            System.out.println("File: " + entry.getKey() + ", Path: " + entry.getValue());
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
}