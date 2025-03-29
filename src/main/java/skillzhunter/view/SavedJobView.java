package skillzhunter.view;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import skillzhunter.controler.SavedJobController;



public class SavedJobView extends JPanel{
    private JButton searchButton;
    private JTextArea recordText;

    public SavedJobView() {
        searchButton = new JButton("Saved Jobs");
        add(searchButton);
        recordText = new JTextArea("click to find jobs");
        add(recordText);
    }


    public void addFeatures(SavedJobController controller) {
        this.searchButton.addActionListener(e -> controller.dummySavedJobMethod());
    }

    public void setRecordText(String text) {
        this.recordText.setText(text);

    }
    public static void main(String[] args) {
        System.out.println("hello");
    }

}
