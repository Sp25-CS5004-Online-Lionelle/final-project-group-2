package skillzhunter;

import skillzhunter.controller.IController;
import skillzhunter.controller.MainController;
import skillzhunter.model.IModel;
import skillzhunter.model.Jobs;
import skillzhunter.view.IView;
import skillzhunter.view.MainView;


public final class SkillsHunterApp {

    private SkillsHunterApp() {
        // empty
    }

    /**
     * Main method to run the application.
     * @param args command line arguments (ignored)
     */
    public static void main(String[] args) {
        // Create MVC components
        IModel model = new Jobs();
        IController controller = new MainController();
        IView view = new MainView();
        
        // Connect components using dependency injection
        controller.setModel(model);
        controller.setView(view);
        
        // Start the application
        view.run();
    }
}
