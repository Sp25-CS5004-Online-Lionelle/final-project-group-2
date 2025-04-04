package skillzhunter.view;


import static skillzhunter.view.JobsLoader.getData;

import java.util.List;
import skillzhunter.model.JobRecord;
import skillzhunter.controller.IController;

public class FindJobView extends JobView {
    private static IController controller;

    public FindJobView(IController controller){
        super();
        setJobsList(JobRecordGenerator.generateDummyRecords(10));
    }


    public static void main(String[] args) {
        System.out.println("hello");
    }

}