package normative_control.output;

import java.io.FileWriter;
import java.io.IOException;

public class FileLogger {
    public static void writeValidationLogs(String text) {
        try(FileWriter writer = new FileWriter("validation_logs.txt", false)){
            writer.write(text);
            writer.append('\n');
            writer.flush();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
