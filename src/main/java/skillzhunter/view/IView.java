package skillzhunter.view;


import skillzhunter.controller.IController;
public interface IView {
/**
 * Runs the view.
 * This method is used to start the view and display it to the user.
 * It should be called after the view has been set up and configured.
 */
void run();
/**
 * This method is used to set the controller for the view.
 * @param controller The controller to be set for the view.
 */
void addFeatures(IController controller);


}
