package skillzhunter.view;


import static skillzhunter.view.JobsLoader.getData;

import java.util.List;
import skillzhunter.model.JobRecord;

public class FindJobView extends JobView {

    public FindJobView(){
        super();
        setJobsList(JobRecordGenerator.generateDummyRecords(10));
    }


    public static void main(String[] args) {
        System.out.println("hello");
    }

}