package skillzhunter.controller;

import skillzhunter.model.Jobs;
import skillzhunter.view.IView;

public interface IController {
    /** sets View */
    void setView(IView view);
    /** sets Model */
    void setModel(Jobs model);
    /** gets View */
    IView getView();
    /** gets Model */
    Jobs getModel();


}
