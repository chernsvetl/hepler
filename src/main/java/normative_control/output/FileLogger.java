package normative_control.output;

import java.io.FileWriter;
import java.io.IOException;

public class FileLogger {
    public static void writeValidationLogs(String text, String fileName) {
        try(FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(text);
            writer.flush();
        } catch (IOException e){
            System.out.println("Error writing to log file: " + e.getMessage());
        }
    }
}
