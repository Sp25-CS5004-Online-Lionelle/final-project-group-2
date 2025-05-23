package skillzhunter.view;

import javax.swing.*;
import java.awt.*;

import skillzhunter.model.JobRecord;

/**
 * Abstract base class for job detail dialogs.
 * Contains common UI elements and behavior shared by different job detail dialog types.
 * Provides dynamic sizing based on content, particularly for job titles.
 */
public abstract class BaseJobDetailsDialogue {
    
    /** Default logo width */
    protected static final int LOGO_WIDTH = 64;
    /** Default logo height */
    protected static final int LOGO_HEIGHT = 64;
    /** Default dialog width */
    protected static final int DEFAULT_DIALOG_WIDTH = 450;
    /** Default dialog height */
    protected static final int DEFAULT_DIALOG_HEIGHT = 580;
    /** Minimum width for dialogs */
    protected static final int MIN_DIALOG_WIDTH = 450;
    /** Extra width per character for long titles */
    protected static final double CHAR_WIDTH_FACTOR = 6.0;
    /** Maximum dialog width as percentage of screen width */
    protected static final double MAX_WIDTH_SCREEN_PERCENT = 0.75;

    /**
     * Creates a dialog with job details, sized appropriately for content.
     * 
     * @param parent The parent component
     * @param job The job record from the controller
     * @param title The dialog title
     * @return The created dialog
     */
    protected static JDialog createBaseDialog(Component parent, JobRecord job, String title) {
        // Create the dialog - properly get the parent window
        Window parentWindow = SwingUtilities.getWindowAncestor(parent);
        JDialog dialog;
        
        if (parentWindow instanceof java.awt.Frame frame) {
            dialog = new JDialog(frame, title, true);
        } else if (parentWindow instanceof java.awt.Dialog dialogParent) {
            dialog = new JDialog(dialogParent, title, true);
        } else {
            dialog = new JDialog(new JFrame(), title, true);
        }
        
        dialog.setLayout(new BorderLayout(0, 10));
        return dialog;
    }
    
    /**
     * Creates the main content panel for job details.
     * 
     * @param job The job to display, retrieved from the controller
     * @return The created panel
     */
    protected static JPanel createMainContentPanel(JobRecord job) {
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        // Create header panel
        JPanel headerPanel = new JPanel(new BorderLayout(5, 5));
        
        // Add logo at top, centered
        ImageIcon companyLogo = IconLoader.loadCompanyLogo(job.companyLogo(),
        LOGO_WIDTH, LOGO_HEIGHT, "images/idea.png");
        JLabel logoLabel = new JLabel(companyLogo);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(logoLabel, BorderLayout.CENTER);
        
        // Add header panel to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Create content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Add company name
        JLabel companyNameLabel = new JLabel(job.companyName(), SwingConstants.CENTER);
        companyNameLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        companyNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(companyNameLabel);
        contentPanel.add(Box.createVerticalStrut(10));
        
        // Add job title
        JLabel titleLabel = new JLabel("<html><h2>" + job.jobTitle() + "</h2></html>", SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(15));
        
        // Create a simple panel for job details with 2 columns
        JPanel detailsPanel = new JPanel(new GridLayout(0, 2, 10, 5));
        
        // Add job details
        if (job.jobGeo() != null && !job.jobGeo().isEmpty()) {
            addDetailRow(detailsPanel, "Location:", job.jobGeo());
        }
        
        if (job.jobIndustry() != null && !job.jobIndustry().isEmpty()) {
            String industries = String.join(", ", job.jobIndustry());
            addDetailRow(detailsPanel, "Industry:", industries);
        }
        
        if (job.jobType() != null && !job.jobType().isEmpty()) {
            String types = String.join(", ", job.jobType().stream()
                .map(type -> {
                    String[] parts = type.split("-");
                    for (int i = 0; i < parts.length; i++) {
                        parts[i] = parts[i].substring(0, 1).toUpperCase() + parts[i].substring(1);
                    }
                    return String.join("-", parts);
                }).toList());
            addDetailRow(detailsPanel, "Type:", types);
        }
        
        if (job.jobLevel() != null && !job.jobLevel().isEmpty()) {
            addDetailRow(detailsPanel, "Level:", job.jobLevel());
        }
        
        if ((job.annualSalaryMin() != 0 || job.annualSalaryMax() != 0)) {
            String salaryText = String.format("%s - %s",
                job.annualSalaryMin() == 0 ? "N/A" : String.format("%,d", job.annualSalaryMin()),
                job.annualSalaryMax() == 0 ? "N/A" : String.format("%,d", job.annualSalaryMax())
            );
            
            if (job.salaryCurrency() != null && !job.salaryCurrency().isEmpty()) {
                salaryText += " " + job.salaryCurrency();
            }
            
            addDetailRow(detailsPanel, "Salary:", salaryText);
        }
        
        if (job.pubDate() != null && !job.pubDate().isEmpty()) {
            addDetailRow(detailsPanel, "Published:", job.pubDate());
        }
        
        // Center the details panel
        JPanel detailsWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        detailsWrapper.add(detailsPanel);
        contentPanel.add(detailsWrapper);
        
        // Add content panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    /**
     * Calculates appropriate dialog dimensions based on content.
     * Adjusts dialog width based on job title length.
     * 
     * @param dialog The dialog to configure
     * @param job The job record containing the title
     */
    protected static void calculateAndSetDialogSize(JDialog dialog, JobRecord job) {
        // Start with default dimensions
        int width = DEFAULT_DIALOG_WIDTH;
        int height = DEFAULT_DIALOG_HEIGHT;
        
        // Adjust width based on job title length
        if (job.jobTitle() != null) {
            int titleLength = job.jobTitle().length();
            // Add extra width for longer titles
            if (titleLength > 30) {
                // Calculate dynamic width based on title length
                width = MIN_DIALOG_WIDTH + (int)((titleLength - 30) * CHAR_WIDTH_FACTOR);
                
                // Get screen size to ensure we don't exceed reasonable width
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int maxWidth = (int)(screenSize.width * MAX_WIDTH_SCREEN_PERCENT);
                
                // Cap width at max percentage of screen width
                if (width > maxWidth) {
                    width = maxWidth;
                }
            }
        }
        
        // Set dialog size
        dialog.setSize(width, height);
    }
    
    /**
     * Helper method to add detail rows to the details panel.
     * @param panel The panel to add the detail row to
     * @param label The label for the detail
     * @param value The value for the detail
     */
    protected static void addDetailRow(JPanel panel, String label, String value) {
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(labelComp.getFont().deriveFont(Font.BOLD));
        panel.add(labelComp);
        
        JLabel valueComp = new JLabel(value);
        panel.add(valueComp);
    }
}
