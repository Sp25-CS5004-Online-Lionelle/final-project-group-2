package skillzhunter.view;

import javax.swing.JButton;
import javax.swing.JPanel;

import skillzhunter.controler.IController;



public class FindJobView extends JPanel implements IView {
    private JButton searchButton;
    
    public FindJobView() {
        searchButton = new JButton("Find Jobs");
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
