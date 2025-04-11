package skillzhunter;

import skillzhunter.controller.MainController;
import skillzhunter.model.IModel;
import skillzhunter.view.IView;


public class SkillsHunterApp {
    static final String DEFAULT_DATA_FILE = "data\\temp_data.xml";

    private SkillsHunterApp() {
        // empty
    }

    //Removing anything to do with console - will add back in if we decide to use it but currently uneccessary

    /**
     * Main method to run the application.
     * @param args command line arguments (ignored)
     */
    public static void main(String[] args) {
        MainController mainController = new MainController();
        //create model and set based off of input file in data if there is a database otherwise
        IModel model = mainController.getModel();
        IView mainView = mainController.getView();
        mainView.run();
    }

}