package initiate_interface.Delete;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;

public class BachelorsDelete {
    public static void delete(){
        JFileChooser fileChooser = new JFileChooser("C:\\Users\\User\\Desktop\\helper\\hepler\\target\\classes\\documents\\bachelors\\4th_course");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setMultiSelectionEnabled(true);
        int returnValue = fileChooser.showOpenDialog(null);
        if(returnValue == JFileChooser.APPROVE_OPTION) {

            File[] selectedFile = fileChooser.getSelectedFiles();

            Arrays.asList(selectedFile).forEach(x -> {
                if (x.isFile()) {
                    System.out.println("You deleted document: " + x.getName()); // Вы удалили документ
                    x.delete();
                }
            });

        }
    }
}
