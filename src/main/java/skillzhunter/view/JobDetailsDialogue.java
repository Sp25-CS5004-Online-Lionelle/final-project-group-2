package skillzhunter.view;

import javax.swing.*;
import java.awt.Component;

import java.util.List;
import skillzhunter.model.JobRecord;

public class JobDetailsDialogue {

    public static void showJobDetails(Component parent, JobRecord job, List<JobRecord> savedJobs) {
        boolean jobPresent = savedJobs.contains(job);
        String dialogMsg = jobPresent ? "Remove this Job?" : "Save this Job?";

        JTextArea jobTitle = new JTextArea(job.jobTitle());
        JTextArea jobCompany = new JTextArea(job.companyName());
        JTextArea jobIndustry = new JTextArea(String.join(", ", job.jobIndustry()));
        JTextArea jobType = new JTextArea(String.join(", ", job.jobType()));
        JTextArea jobGeo = new JTextArea(job.jobGeo());
        JTextArea jobLevel = new JTextArea(job.jobLevel());
        JTextArea jobSalaryRange = new JTextArea(String.valueOf(job.annualSalaryMin()));
        JTextArea jobCurrency = new JTextArea(job.salaryCurrency());
        JTextArea jobPubDate = new JTextArea(job.pubDate());

        JSlider jobRating = new JSlider(0, 5, 2);
        jobRating.setMajorTickSpacing(5);
        jobRating.setMinorTickSpacing(1);
        jobRating.setPaintTicks(true);
        jobRating.setPaintLabels(true);

        JTextArea comments = new JTextArea(5, 20);
        comments.setLineWrap(true);
        comments.setWrapStyleWord(true);
        comments.setBorder(BorderFactory.createTitledBorder("Your Comments"));

        Object[] obj = {
            "Job Title: ", jobTitle, "Company: ", jobCompany, "Industry: ", jobIndustry,
            "Type: ", jobType, "Location: ", jobGeo, "Level: ", jobLevel, "Salary: ", jobSalaryRange,
            "Currency: ", jobCurrency, "Published: ", jobPubDate, "Rate this Job: ", jobRating,
            "Comments:", comments, dialogMsg
        };

        int result = JOptionPane.showConfirmDialog(parent, obj, "Job Details: ", JOptionPane.INFORMATION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            if (jobPresent) {
                SavedJobsLists.removeSavedJob(job);
            } else {
                SavedJobsLists.addSavedJob(job);
            }
        }
    }
}
