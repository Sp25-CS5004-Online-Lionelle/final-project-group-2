package skillzhunter.view;

import skillzhunter.controller.IController;

public class SavedJobTab extends JobView {
    private IController controller;

    public SavedJobTab(IController controller) {

        super();
        //setJobsList(JobRecordGenerator.generateDummyRecords(10));
        super.initView();
    }




    public static void main(String[] args) {
        System.out.println("hello");
    }

}
