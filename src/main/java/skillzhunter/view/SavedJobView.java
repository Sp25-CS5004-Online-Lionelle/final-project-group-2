package skillzhunter.view;

import javax.swing.JButton;
import javax.swing.JPanel;

import skillzhunter.controler.IController;



public class SavedJobView extends JPanel implements IView {
    private JButton searchButton;
    
    public SavedJobView() {
        searchButton = new JButton("Saved Jobs");
        add(searchButton);
    }



    @Override
    public void addFeatures(IController controller) {
        throw new UnsupportedOperationException("Unimplemented method 'addFeatures'");
    }
    public static void main(String[] args) {
        System.out.println("hello");
    }

}
