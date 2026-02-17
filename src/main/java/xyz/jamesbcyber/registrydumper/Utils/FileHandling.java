package xyz.jamesbcyber.registrydumper.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandling {

    public static File writeFile(File dir, String filename, String text){
        File file = new File(dir, filename);
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write(text);
            return file;
        } catch (IOException e) {
            System.err.println("Failed to write to mod log file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static File writeFile(File file, String text){
        file.getParentFile().mkdirs();
        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write(text);
            return file;
        } catch (IOException e) {
            System.err.println("Failed to write to mod log file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
