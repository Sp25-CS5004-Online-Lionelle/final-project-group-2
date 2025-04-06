package skillzhunter.view;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import skillzhunter.controller.IController;

public class SavedJobTab extends JobView {
    private IController controller;

    public SavedJobTab(IController controller) {
        super();
        super.initView();
    }

    @Override
    public JPanel makeTopButtonPanel() {

        //make the panel & set layout
        JPanel topRow = new JPanel();

        //create fields, buttons, and combos
        JButton loadButton = new JButton("Load Job List from File");


        //add fields, buttons, labels, combos, and spaces
        topRow.add(loadButton);

        //set listeners
        loadButton.addActionListener(e -> {
            Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(topRow);
            FileDialog fd = new FileDialog(parentFrame, "Pick file to load", FileDialog.LOAD);
            String path = null;
            //fd.setDirectory(); need to decide what to set for default
            fd.setVisible(true);

            if (fd.getFile() == null) {
                path = null;
            } else {
                File file = new File(fd.getDirectory(), fd.getFile());
                path = file.getPath();
            }
            System.out.println(path); //just prints now, needs to be wired up to model
           }
        );

        //return the panel
        return topRow;

    }

    @Override
    public JPanel makeBottomButtonPanel() {

        //make the panel & set layout
        JPanel bottomRow = new JPanel();

        //create fields, buttons, and combos
        JButton saveButton = new JButton("Save Job List to a File");


        //add fields, buttons, labels, combos, and spaces
        bottomRow.add(saveButton);

        //set listeners
        saveButton.addActionListener(e -> {
                Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(bottomRow);
                FileDialog fd = new FileDialog(parentFrame, "Pick a Save Location", FileDialog.SAVE);
                String path = null;
                //fd.setDirectory(); need to decide what to set for default
                fd.setVisible(true);

                if (fd.getFile() == null) {
                    path = null;
                } else {
                    File file = new File(fd.getDirectory(), fd.getFile());
                    path = file.getPath();
                }
                System.out.println(path); //just prints now, needs to be wired up to model
            }
        );

        //return the panel
        return bottomRow;
    }

    public static void main(String[] args) {
        System.out.println("hello");
    }

}
