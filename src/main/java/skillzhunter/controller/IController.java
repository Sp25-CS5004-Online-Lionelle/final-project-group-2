package skillzhunter.controller;

import skillzhunter.model.IModel;
import skillzhunter.view.IView;

public interface IController {
    /** gets View */
    IView getView();
    /** gets Model */
    IModel getModel();
}
