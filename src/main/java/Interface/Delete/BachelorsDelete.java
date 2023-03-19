package Interface.Delete;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;

public class BachelorsDelete {
    public static void delete(){
        JFileChooser fileChooser = new JFileChooser("C:\\Users\\User\\Desktop\\Assistant\\sveta_vkr\\intelligent_assistant\\target\\classes\\documents\\bachelors");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setMultiSelectionEnabled(true);
        int returnValue = fileChooser.showOpenDialog(null);
        if(returnValue == JFileChooser.APPROVE_OPTION) {
//                        File selectedFile = fileChooser.getSelectedFile();
//                        System.out.println("You selected: " + selectedFile);
//                        selectedFile.delete();

            File[] selectedFile = fileChooser.getSelectedFiles();
            //System.out.println("You selected: " + Arrays.toString(selectedFile));

            Arrays.asList(selectedFile).forEach(x -> {
                if (x.isFile()) {
                    System.out.println("You deleted: " + x.getName());
                    x.delete();
                }
            });

        }
    }
}
