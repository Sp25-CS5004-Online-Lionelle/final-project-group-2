package skillzhunter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import skillzhunter.controller.MainController;

import skillzhunter.view.IView;
import skillzhunter.model.IModel;


public class SkillsHunterApp {
    static final String DEFAULT_DATA_FILE = "data\\temp_data.xml";

    private SkillsHunterApp() {
        // empty
    }

    //Removing anything to do with console - will add back in if we decide to use it but currently uneccessary

    /**
     * Main method to run the application.
     * Will add in args if we decide to use that for parseArgs in future but right now straight to GUI.
     */
    public static void main(String[] args) {
        MainController mainController = new MainController();
        //create model and set based off of input file in data if there is a database otherwise
        IModel model = mainController.getModel();
        IView mainView = mainController.getView();
        mainView.run();
        //have controller start the application
    }

}