package skillzhunter.view;

import skillzhunter.controller.IController;

public interface IView {


/**
 * This method is used to set the controller for the view.
 * @param controller The controller to be set for the view.
 */
void addFeatures(IController controller);

}
