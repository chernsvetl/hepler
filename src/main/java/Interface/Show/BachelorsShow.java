package Interface.Show;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;

import static Interface.WorkMenu.frame4;

public class BachelorsShow {
    public static void show() {
        JFileChooser fileChooser = new JFileChooser("C:\\Users\\User\\Desktop\\Assistant\\sveta_vkr\\intelligent_assistant\\target\\classes\\documents\\bachelors\\4th_course");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "DOCX & DOC files", "docx", "doc");
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setMultiSelectionEnabled(true);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("You selected: " + selectedFile);
            try {
                Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler " + selectedFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        frame4.setVisible(true);
    }
}