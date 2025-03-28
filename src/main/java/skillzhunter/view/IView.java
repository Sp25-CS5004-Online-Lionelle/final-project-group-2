package skillzhunter.view;

import skillzhunter.controler.IController;

public interface IView {


/**
 * This method is used to set the controller for the view.
 * @param controller The controller to be set for the view.
 */
void addFeedback(IController controller);

}
