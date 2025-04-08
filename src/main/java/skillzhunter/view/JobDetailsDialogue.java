package skillzhunter.view;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

import skillzhunter.model.JobRecord;
import skillzhunter.controller.IController;

public class JobDetailsDialogue {

    public static void showJobDetails(Component parent, JobRecord job, List<JobRecord> savedJobs, IController controller) {
        if (job == null || controller == null) {
            JOptionPane.showMessageDialog(parent, "Job or controller not provided.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean jobPresent = savedJobs != null && savedJobs.contains(job);
        String dialogMsg = jobPresent ? "Remove this Job?" : "Save this Job?";

        // Use helper method to make non-editable JTextAreas
        JTextArea jobTitle = readOnlyArea(job.jobTitle());
        JTextArea jobCompany = readOnlyArea(job.companyName());
        JTextArea jobIndustry = readOnlyArea(
            job.jobIndustry() != null ? 
                String.join(", ", job.jobIndustry().stream()
                    .map(industry -> industry.replace("&amp;", ",").replace("&", ",").trim())
                    .toList()) 
                : "N/A"
        );
        JTextArea jobType = readOnlyArea(
            job.jobType() != null ?
                String.join(", ", job.jobType().stream()
                    .map(type -> {
                        String[] parts = type.split("-");
                        for (int i = 0; i < parts.length; i++) {
                            parts[i] = parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1);
                        }
                        return String.join("-", parts);
                    }).toList())
                : "N/A"
        );
        JTextArea jobGeo = readOnlyArea(job.jobGeo());
        JTextArea jobLevel = readOnlyArea(job.jobLevel());
        JTextArea jobSalaryRange = readOnlyArea(
            job.annualSalaryMin() == 0 && job.annualSalaryMax() == 0 
                ? "N/A" 
                : String.format("%s - %s",
                    job.annualSalaryMin() == 0 ? "N/A" : String.format("%,d", job.annualSalaryMin()),
                    job.annualSalaryMax() == 0 ? "N/A" : String.format("%,d", job.annualSalaryMax())
                )
        );
        JTextArea jobCurrency = readOnlyArea(
            job.salaryCurrency() == null || job.salaryCurrency().isEmpty() ? "N/A" : job.salaryCurrency()
        );
        JTextArea jobPubDate = readOnlyArea(job.pubDate());

        // Rating Slider
        JSlider jobRating = new JSlider(0, 5, 2);
        jobRating.setMajorTickSpacing(5);
        jobRating.setMinorTickSpacing(1);
        jobRating.setPaintTicks(true);
        jobRating.setPaintLabels(true);

        jobRating.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int ratingValue = jobRating.getValue();
                controller.updateRating(job.id(), ratingValue);
            }
        });

        // Comments Box
        JTextArea comments = new JTextArea(5, 20);
        comments.setLineWrap(true);
        comments.setWrapStyleWord(true);
        comments.setBorder(BorderFactory.createTitledBorder("Your Comments"));

        comments.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String commentText = comments.getText();
                controller.updateComments(job.id(), commentText); //telling controller to tell model to update this job
                System.out.printf("JOBDETAILSDIALOGUE - focus lost and updated comment with controller\n\t%s\n", job); //TESTING

                /*
                this view object has a local JobRecord that it cannot change
                the view tells the controller to update this job, but the local JobRecord doesn't reflect this
                so when we get to YES_OPTION and SavedJobsLists.addSavedJob(job) the JobRecord we're giving it doesn't have any of our updates

                - check if controller.updateComments() is doing what we want.
                    maybe we are trying to update a job too early? has this job even been added to the model yet?

                - possible idea: have the controller/model return a JobRecord after it updates a job so the view has an accurate JobRecord
                */
            }
        });

        Object[] obj = {
            "Job Title: ", jobTitle, "Company: ", jobCompany, "Industry: ", jobIndustry,
            "Type: ", jobType, "Location: ", jobGeo, "Level: ", jobLevel, "Salary: ", jobSalaryRange,
            "Currency: ", jobCurrency, "Published: ", jobPubDate, "Rate this Job: ", jobRating,
            "Comments:", comments, dialogMsg
        };

        int result = JOptionPane.showConfirmDialog(parent, obj, "Job Details: ", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            System.out.printf("JOBDETAILSDIALOGUE - detected yes option\n\t%s\n", job); //TESTING
            if (jobPresent) {
                SavedJobsLists.removeSavedJob(job);
            } else {
                SavedJobsLists.addSavedJob(job);
            }
        }
    }

    // 👇 This helper method must be inside the class, and static
    private static JTextArea readOnlyArea(String text) {
        JTextArea area = new JTextArea(text != null ? text : "N/A");
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        area.setBorder(null);
        return area;
    }
}
