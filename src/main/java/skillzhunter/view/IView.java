package skillzhunter.view;

import skillzhunter.controller.IController;

public interface IView {
    /**
     * Sets the controller for this view.
     * This allows the view to communicate back to the controller.
     * 
     * @param controller The controller to set
     */
    void setController(IController controller);
    
    /**
     * Runs the view.
     * This method is used to start the view and display it to the user.
     * It should be called after the view has been set up and configured.
     */
    void run();

    /**
     * This method is used when an alert from another part
     * of the program needs to be sent to the user.
     * @param message The message the user needs to see.
     */
    void notifyUser(String message);
}