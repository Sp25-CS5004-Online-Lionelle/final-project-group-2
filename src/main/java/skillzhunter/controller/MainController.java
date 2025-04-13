package skillzhunter.controller;

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JTabbedPane;

import skillzhunter.model.IModel;
import skillzhunter.model.JobRecord;
import skillzhunter.model.Jobs;
import skillzhunter.view.IView;
import skillzhunter.view.MainView;
import skillzhunter.view.SavedJobsTab;

public class MainController implements IController {

    /** Model. */
    private IModel model;
    /** View. */
    private IView view;
    /** Saved jobs tab. */
    private final SavedJobsTab savedJobsTab;

    /**
     * Constructor for MainController.
     * Initializes the model and view.
     */
    public MainController() {
        // Model
        model = new Jobs();

        // View
        view = new MainView(this);
        
        // Initialize savedJobsTab, which will be properly created by MainView
        savedJobsTab = getSavedJobsTabFromView();
        
        //Update the saved jobs tab with current data
        if (savedJobsTab != null) {
            savedJobsTab.updateJobsList(model.getJobRecords());
        }
    }

    /**
     * Gets the SavedJobsTab component from the view.
     * 
     * @return The SavedJobsTab instance
     */
    private SavedJobsTab getSavedJobsTabFromView() {
        if (view instanceof MainView mainView) {
            // Find the tabbed pane
            Component contentPane = mainView.getContentPane();
            JTabbedPane tabbedPane = findTabbedPane(contentPane);
            
            if (tabbedPane != null) {
                // Find the "Saved Jobs" tab
                for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                    String title = tabbedPane.getTitleAt(i);
                    if ("Saved Jobs".equals(title)) {
                        Component component = tabbedPane.getComponentAt(i);
                        if (component instanceof SavedJobsTab) {
                            return (SavedJobsTab) component;
                        }
                    }
                }
            }
        }
        // Fallback in case we can't find it
        return new SavedJobsTab(this, model.getJobRecords());
    }

    /**
     * Recursively finds a JTabbedPane in a component hierarchy.
     * 
     * @param component The component to search in
     * @return The JTabbedPane, or null if not found
     */
    private JTabbedPane findTabbedPane(Component component) {
        if (component instanceof JTabbedPane tabbedPane) {
            return tabbedPane;
        } else if (component instanceof java.awt.Container container) {
            for (int i = 0; i < container.getComponentCount(); i++) {
                JTabbedPane found = findTabbedPane(container.getComponent(i));
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    /**
     * Returns the view.
     * 
     * @return The view
     */
    @Override
    public IView getView() {
        return view;
    }

    /**
     * Sets the view and updates the saved jobs tab.
     * 
     * @param view The view to set
     */
    protected void setView(IView view) {
        this.view = view;
    }

    /**
     * Returns the model.
     * @return IModel
     */
    @Override
    public IModel getModel() {
        return model;
    }

    /**
     * Sets the model and updates the saved jobs tab.
     * 
     * @param model The model to set
     */
    protected void setModel(IModel model) {
        this.model = model;
        if (savedJobsTab != null) {
            savedJobsTab.updateJobsList(model.getJobRecords());
        }
    }
    

    /**
     * Gets the locations from the API and capitalizes them appropriately.
     * @return List<String> of locations
     */
    @Override
    public List<String> getLocations() {
        return capitalizeItems(model.getLocations(), Collections.emptyMap());
    }

    /**
     * Gets the industries from the API and capitalizes them appropriately.
     * @return List<String> of industries
     */
    @Override
    public List<String> getIndustries() {
        Map<String, String> specialCases = new HashMap<>();
        specialCases.put("hr", "HR");
        return capitalizeItems(model.getIndustries(), specialCases);
    }

    /**
     * Capitalizes the items in the list.
     * Handles multi-word strings and keeps in mind any special cases for capitalization.
     * @param items List of strings to capitalize
     * @param specialCases Map of special case words and their capitalization
     * @return List of capitalized strings
     */
    private List<String> capitalizeItems(List<String> items, Map<String, String> specialCases) {
        return items.stream()
                .map(item -> {
                    String[] words = item.split(" ");
                    StringBuilder result = new StringBuilder();
                    
                    for (String word : words) {
                        if (!word.isEmpty()) {
                            String lowerWord = word.toLowerCase();
                            String capitalizedWord = specialCases.getOrDefault(lowerWord, capitalizeWord(word));
                            result.append(capitalizedWord).append(" ");
                        }
                    }
                    
                    return result.toString().trim();
                })
                .toList();
    }

    /**
     * Capitalizes a single word.
     * @param word The word to capitalize
     * @return The capitalized word
     */
    private String capitalizeWord(String word) {
        if (word.isEmpty()) {
            return word;
        }
        return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
    }

    /**
     * Gets the API call for jobicy api search.
     * 
     * @param query The search query
     * @param numberOfResults The number of results to return (Nullable)
     * @param location The location to filter by (Nullable)
     * @param industry The industry to filter by (Nullable)
     * @return List of JobRecord objects
     */
    @Override
    public List<JobRecord> getApiCall(String query, Integer numberOfResults, String location, String industry) {
        return model.searchJobs(query, numberOfResults, location, industry);
    }

    /**
     * Gets the saved jobs.
     * 
     * @return List of JobRecord objects
     */
    @Override
    public List<JobRecord> getSavedJobs() {
        return model.getJobRecords();
    }

    /**
     * Sets the saved jobs.
     * 
     * @param savedJobs List of JobRecord objects to set
     * @return List of JobRecord objects
     */
    public List<JobRecord> setSavedJobs(List<JobRecord> savedJobs) {
        for (JobRecord job : savedJobs) {
            model.addJob(job);
        }
        List<JobRecord> savedJobsList = model.getJobRecords();
        if (savedJobsTab != null) {
            savedJobsTab.updateJobsList(savedJobsList);
        }
        return savedJobsList;
    }

    /**
     * Checks if a job is already in the saved jobs list.
     * 
     * @param jobRecord The job record to check
     * @return true if the job is already saved, false otherwise
     */
    @Override
    public boolean isJobAlreadySaved(JobRecord jobRecord) {
        List<JobRecord> savedJobs = getSavedJobs();
        
        // Check if the job is already in the list
        return savedJobs.stream()
                .anyMatch(job -> job.id() == jobRecord.id());
    }

    /**
     * Adds a job to the saved jobs list.
     * 
     * @param jobRecord The JobRecord object to add
     */
    @Override
    public void job2SavedList(JobRecord jobRecord) {
        model.addJob(jobRecord);
    }
    
    /**
     * Try to add a job to the saved jobs list, checking for duplicates.
     * 
     * @param jobRecord The JobRecord object to add
     * @return true if the job was added successfully, false if it was already in the list
     */
    public boolean tryAddJobToSavedList(JobRecord jobRecord) {
        // Check if the job is already in the list
        if (isJobAlreadySaved(jobRecord)) {
            return false;
        }
        
        // Add the job to the list
        model.addJob(jobRecord);
        return true;
    }

    /**
     * Removes a job from the saved jobs list.
     * 
     * @param index The index of the job to remove
     */
    @Override
    public void removeJobFromList(int index) {
        model.removeJob(index);
    }

    
    /**
     * Saves the job records to a CSV file using a custom CSV writer
     * that properly handles collections and HTML content.
     * 
     * @param filePath The file path to save the CSV file
     */
    @Override
    public void path2CSV(String filePath) {
        // Get all job records
        List<JobRecord> jobs = model.getJobRecords();
        
        // Use our custom CSV export method that properly handles HTML content
        exportToCSV(jobs, filePath);
    }
    
    /**
     * Custom method to export jobs to CSV format with proper handling of HTML and collections.
     * 
     * @param jobs The job records to export
     * @param filePath The file path to save the CSV file
     */
    private void exportToCSV(List<JobRecord> jobs, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write header
            writer.write("id,url,jobSlug,jobTitle,companyName,companyLogo,jobIndustry,jobType,jobGeo,jobLevel,jobExcerpt,jobDescription,pubDate,annualSalaryMin,annualSalaryMax,salaryCurrency,rating,comments");
            writer.newLine();
            
            // Write each job record
            for (JobRecord job : jobs) {
                StringBuilder line = new StringBuilder();
                
                // ID
                line.append(job.id()).append(",");
                
                // URL
                line.append(escapeCSV(job.url())).append(",");
                
                // jobSlug
                line.append(escapeCSV(job.jobSlug())).append(",");
                
                // jobTitle
                line.append(escapeCSV(job.jobTitle())).append(",");
                
                // companyName
                line.append(escapeCSV(job.companyName())).append(",");
                
                // companyLogo
                line.append(escapeCSV(job.companyLogo())).append(",");
                
                // jobIndustry - join with commas and handle null/empty
                if (job.jobIndustry() != null && !job.jobIndustry().isEmpty()) {
                    String industries = job.jobIndustry().stream().collect(Collectors.joining(", "));
                    line.append(escapeCSV(industries));
                }
                line.append(",");
                
                // jobType - join with commas and handle null/empty
                if (job.jobType() != null && !job.jobType().isEmpty()) {
                    String types = job.jobType().stream().collect(Collectors.joining(", "));
                    line.append(escapeCSV(types));
                }
                line.append(",");
                
                // jobGeo
                line.append(escapeCSV(job.jobGeo())).append(",");
                
                // jobLevel
                line.append(escapeCSV(job.jobLevel())).append(",");
                
                // jobExcerpt - extract first sentence without HTML
                String excerpt = "";
                if (job.jobExcerpt() != null && !job.jobExcerpt().isEmpty()) {
                    excerpt = extractFirstSentence(stripHTML(job.jobExcerpt()));
                }
                line.append(escapeCSV(excerpt)).append(",");
                
                // jobDescription - simplified version without HTML
                String description = "Position at " + job.companyName() + " - " + job.jobTitle();
                line.append(escapeCSV(description)).append(",");
                
                // pubDate
                line.append(escapeCSV(job.pubDate())).append(",");
                
                // annualSalaryMin
                line.append(job.annualSalaryMin()).append(",");
                
                // annualSalaryMax
                line.append(job.annualSalaryMax()).append(",");
                
                // salaryCurrency
                line.append(escapeCSV(job.salaryCurrency())).append(",");
                
                // rating
                line.append(job.rating()).append(",");
                
                // comments
                line.append(escapeCSV(job.comments()));
                
                // Write the line
                writer.write(line.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }
    
    /**
     * Strips HTML tags from a string.
     * 
     * @param html The string with HTML tags
     * @return The string without HTML tags
     */
    private String stripHTML(String html) {
        if (html == null) return "";
        return html.replaceAll("<[^>]*>", "").replaceAll("\\s+", " ").trim();
    }
    
    /**
     * Extracts the first sentence from a text.
     * 
     * @param text The text to extract from
     * @return The first sentence, or the whole text if no sentence end is found
     */
    private String extractFirstSentence(String text) {
        if (text == null || text.isEmpty()) return "";
        
        // Find first sentence end
        int endPos = -1;
        for (String endMark : new String[]{".", "!", "?"}) {
            int pos = text.indexOf(endMark);
            if (pos >= 0 && (endPos == -1 || pos < endPos)) {
                endPos = pos + 1;
            }
        }
        
        // If found, return just the first sentence
        if (endPos > 0) {
            return text.substring(0, endPos).trim();
        }
        
        // If no sentence end, limit length
        if (text.length() > 100) {
            return text.substring(0, 97) + "...";
        }
        
        return text;
    }
    
    /**
     * Properly escapes a string for CSV format.
     * 
     * @param value The string to escape
     * @return The escaped string
     */
    private String escapeCSV(String value) {
        if (value == null) return "\"\"";
        
        // Replace any double quotes with two double quotes and wrap in quotes
        String escaped = value.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
    
    /**
     * Exports the saved jobs to a specified format and file path.
     * @param jobs The list of JobRecord objects to export
     * @param formatStr The format string (e.g., "csv", "json")
     * @param filePath The file path to save the exported file
     */
    @Override
    public void export2FileType(List<JobRecord> jobs, String formatStr, String filePath) {
        if ("CSV".equalsIgnoreCase(formatStr)) {
            // Use our custom CSV export for better control
            exportToCSV(jobs, filePath);
        } else {
            // Use the model's export for other formats
            model.exportSavedJobs(jobs, formatStr, filePath);
        }
    }

    /**
     * Gets the saved jobs tab.
     * @return The saved jobs tab
     */
    public SavedJobsTab getSavedJobsTab() {
        return savedJobsTab;
    }
    
    /**
     * Updates a job with new comments and rating.
     * 
     * @param id The ID of the job to update
     * @param comments The comments to set
     * @param rating The rating to set
     * @return The updated JobRecord
     */
    public JobRecord getUpdateJob(int id, String comments, int rating) {
        // Debug output to verify values
        System.out.println("Updating job " + id + " with rating: " + rating + " and comments: " + comments);
        
        // Call model to update the job
        model.updateJob(id, comments, rating);
        
        // Update the view with the latest data
        if (savedJobsTab != null) {
            savedJobsTab.updateJobsList(model.getJobRecords());
        }
        
        // Return the updated job record so the view can use it
        for (JobRecord job : model.getJobRecords()) {
            if (job.id() == id) { // FIXED: Compare with parameter id instead of job.id()
                return job;
            }
        }
        
        // Job not found
        System.err.println("Job with ID " + id + " not found after update");
        return null;
    }
}