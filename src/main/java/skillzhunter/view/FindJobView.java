package skillzhunter.view;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import skillzhunter.controller.FindJobController;



public class FindJobView extends JPanel {
    private JButton searchButton;
    private JTextArea recordText;

    public FindJobView() {
        searchButton = new JButton("Find Jobs");
        add(searchButton);

        recordText = new JTextArea("click to find jobs");
        add(recordText);
    }
    public void setRecordText(String text) {
        this.recordText.setText(text);
    }


    public void addFeatures(FindJobController controller) {
        searchButton.addActionListener(e -> controller.dummyFindJobMethod());
    }

    public static void main(String[] args) {
        System.out.println("hello");
    }
    
}
