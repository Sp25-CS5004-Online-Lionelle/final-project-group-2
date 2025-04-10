package skillzhunter.view;

import skillzhunter.controller.IController;
import skillzhunter.model.JobRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * A panel that visualizes job salaries as a line graph with interactive tooltips.
 */
public class SalaryVisualizationPanel extends JPanel {
    
    private List<JobRecord> jobs;
    private final int PADDING = 40;
    private final int POINT_SIZE = 6;
    private final Color POINT_COLOR = new Color(0, 120, 215);
    private final Color POINT_HOVER_COLOR = new Color(255, 69, 0);
    private final Color LINE_COLOR = new Color(0, 120, 215, 150);
    private List<Point2D> dataPoints = new ArrayList<>();
    private List<Rectangle2D> hitBoxes = new ArrayList<>();
    private int hoveredIndex = -1;
    private JToolTip tooltip;
    private ColorTheme theme = ColorTheme.LIGHT; // Default theme
    
    /**
     * Creates a new salary visualization panel.
     * 
     * @param jobs The list of jobs to visualize
     */
    public SalaryVisualizationPanel(List<JobRecord> jobs) {
        this.jobs = new ArrayList<>(jobs);
        setPreferredSize(new Dimension(800, 200));
        setBackground(Color.WHITE);
        
        // Set up tooltip
        tooltip = new JToolTip();
        tooltip.setComponent(this);
        setToolTipText(""); // Enable tooltips
        
        // Sort jobs by average salary
        sortJobsBySalary();
        
        // Add mouse motion listener for hover effects
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int oldHoveredIndex = hoveredIndex;
                hoveredIndex = -1;
                
                // Check if the mouse is over any data point
                for (int i = 0; i < hitBoxes.size(); i++) {
                    if (hitBoxes.get(i).contains(e.getPoint())) {
                        hoveredIndex = i;
                        
                        // Show custom tooltip
                        JobRecord job = jobs.get(i);
                        String tooltipText = createTooltipText(job);
                        setToolTipText(tooltipText);
                        break;
                    }
                }
                
                // Repaint only if hover state changed
                if (oldHoveredIndex != hoveredIndex) {
                    repaint();
                }
            }
        });
    }
    
    /**
     * Creates tooltip text for the specified job.
     */
    private String createTooltipText(JobRecord job) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<b>").append(job.jobTitle()).append("</b><br>");
        sb.append("Company: ").append(job.companyName()).append("<br>");
        sb.append("Level: ").append(job.jobLevel()).append("<br>");
        
        // Format salary numbers with commas
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        String minSalary = job.annualSalaryMin() > 0 ? nf.format(job.annualSalaryMin()) : "N/A";
        String maxSalary = job.annualSalaryMax() > 0 ? nf.format(job.annualSalaryMax()) : "N/A";
        
        sb.append("Salary Range: ").append(minSalary).append(" - ").append(maxSalary);
        if (job.salaryCurrency() != null && !job.salaryCurrency().isEmpty()) {
            sb.append(" ").append(job.salaryCurrency());
        }
        sb.append("</html>");
        
        return sb.toString();
    }
    
    /**
     * Filters and sorts jobs by their average salary.
     * Only keeps jobs with valid salary data for visualization.
     */
    private void sortJobsBySalary() {
        // Filter out jobs with no salary data
        List<JobRecord> jobsWithSalary = new ArrayList<>();
        for (JobRecord job : jobs) {
            if (calculateAverageSalary(job) > 0) {
                jobsWithSalary.add(job);
            }
        }
        
        // Sort by average salary
        Collections.sort(jobsWithSalary, Comparator.comparingInt(job -> {
            return calculateAverageSalary(job);
        }));
        
        // Replace the jobs list with filtered and sorted jobs
        this.jobs = jobsWithSalary;
    }
    
    /**
     * Updates the list of jobs and repaints the visualization.
     * 
     * @param jobs The new job list
     */
    public void updateJobs(List<JobRecord> jobs) {
        this.jobs = new ArrayList<>(jobs);
        sortJobsBySalary();
        repaint();
    }
    
    /**
     * Applies the specified theme to this panel.
     * 
     * @param theme The theme to apply
     */
    public void applyTheme(ColorTheme theme) {
        this.theme = theme;
        setBackground(theme.fieldBackground);
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (jobs == null || jobs.isEmpty()) {
            drawNoDataMessage(g);
            return;
        }
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Calculate max salary to determine scale
        int maxSalary = 0;
        for (JobRecord job : jobs) {
            maxSalary = Math.max(maxSalary, job.annualSalaryMax());
        }
        
        // Add 10% padding to the max value
        maxSalary = (int) (maxSalary * 1.1);
        
        // Clear previous points and hitboxes
        dataPoints.clear();
        hitBoxes.clear();
        
        // Draw axes
        drawAxes(g2d, width, height, maxSalary);
        
        // Draw data points and lines
        drawDataPoints(g2d, width, height, maxSalary);
        
        g2d.dispose();
    }
    
    /**
     * Draws a message when no data is available.
     */
    private void drawNoDataMessage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(theme == ColorTheme.DARK ? Color.WHITE : Color.BLACK);
        g2d.setFont(new Font("Dialog", Font.BOLD, 14));
        String message = "No salary data available to visualize.";
        FontMetrics fm = g2d.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(message)) / 2;
        int y = getHeight() / 2;
        g2d.drawString(message, x, y);
        g2d.dispose();
    }
    
    /**
     * Draws the X and Y axes with labels.
     */
    private void drawAxes(Graphics2D g2d, int width, int height, int maxSalary) {
        // Set color based on theme
        g2d.setColor(theme == ColorTheme.DARK ? Color.WHITE : Color.BLACK);
        
        // Draw X axis
        g2d.draw(new Line2D.Double(PADDING, height - PADDING, width - PADDING, height - PADDING));
        
        // Draw Y axis
        g2d.draw(new Line2D.Double(PADDING, PADDING, PADDING, height - PADDING));
        
        // Add Y axis labels (salary increments)
        int numYLabels = 4; // Reduced number of labels for smaller panel
        g2d.setFont(new Font("Dialog", Font.PLAIN, 10));
        
        for (int i = 0; i <= numYLabels; i++) {
            int salary = (maxSalary * i) / numYLabels;
            int y = height - PADDING - ((height - 2 * PADDING) * i) / numYLabels;
            
            // Draw tick mark
            g2d.draw(new Line2D.Double(PADDING - 5, y, PADDING, y));
            
            // Draw label with formatted salary
            String label = formatSalaryLabel(salary);
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(label, PADDING - fm.stringWidth(label) - 7, y + fm.getHeight() / 3);
            
            // Draw horizontal grid line
            g2d.setColor(new Color(200, 200, 200, 100));
            g2d.draw(new Line2D.Double(PADDING, y, width - PADDING, y));
            g2d.setColor(theme == ColorTheme.DARK ? Color.WHITE : Color.BLACK);
        }
        
        // Draw title
        g2d.setFont(new Font("Dialog", Font.BOLD, 12));
        String title = "Job Salary Comparison";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(title, (width - fm.stringWidth(title)) / 2, PADDING / 2);
    }
    
    /**
     * Formats a salary value for display on the Y axis.
     */
    private String formatSalaryLabel(int salary) {
        if (salary >= 1000000) {
            return String.format("$%.1fM", salary / 1000000.0);
        } else if (salary >= 1000) {
            return String.format("$%dK", salary / 1000);
        } else {
            return "$" + salary;
        }
    }
    
    /**
     * Draws the data points and connecting lines.
     */
    private void drawDataPoints(Graphics2D g2d, int width, int height, int maxSalary) {
        int graphWidth = width - (2 * PADDING);
        int graphHeight = height - (2 * PADDING);
        int numJobs = jobs.size();
        
        if (numJobs < 2) {
            // Need at least 2 jobs for a meaningful line graph
            return;
        }
        
        // Calculate point positions for all jobs first
        List<Point2D> pointPositions = new ArrayList<>();
        for (int i = 0; i < numJobs; i++) {
            JobRecord job = jobs.get(i);
            int salary = calculateAverageSalary(job);
            
            if (salary <= 0) {
                // Skip jobs with no salary data
                continue;
            }
            
            // Calculate point position
            int x = PADDING + (graphWidth * i) / (numJobs - 1);
            int y = height - PADDING - (int)((double)graphHeight * salary / maxSalary);
            
            pointPositions.add(new Point2D.Double(x, y));
        }
        
        // Draw lines connecting points
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(theme == ColorTheme.DARK ? 
                    new Color(0, 183, 195, 150) : // Teal in dark mode
                    LINE_COLOR); // Blue in light mode
        
        for (int i = 0; i < pointPositions.size() - 1; i++) {
            Point2D p1 = pointPositions.get(i);
            Point2D p2 = pointPositions.get(i + 1);
            g2d.draw(new Line2D.Double(p1, p2));
        }
        
        // Store points for hitbox detection
        this.dataPoints = pointPositions;
        
        // Draw points and labels after drawing lines so they appear on top
        for (int i = 0; i < pointPositions.size(); i++) {
            Point2D point = pointPositions.get(i);
            
            // Create hitbox for this point
            int hitBoxSize = POINT_SIZE * 3;
            hitBoxes.add(new Rectangle2D.Double(
                point.getX() - hitBoxSize/2, point.getY() - hitBoxSize/2, 
                hitBoxSize, hitBoxSize));
            
            // Draw point (highlighted if hovered)
            if (i == hoveredIndex) {
                g2d.setColor(theme == ColorTheme.DARK ? 
                            new Color(255, 140, 0) : // Brighter orange in dark mode
                            POINT_HOVER_COLOR); // Normal orange in light mode
                g2d.fill(new Ellipse2D.Double(
                    point.getX() - POINT_SIZE, point.getY() - POINT_SIZE, 
                    POINT_SIZE * 2, POINT_SIZE * 2));
            } else {
                g2d.setColor(theme == ColorTheme.DARK ? 
                            new Color(0, 183, 195) : // Teal in dark mode
                            POINT_COLOR); // Blue in light mode
                g2d.fill(new Ellipse2D.Double(
                    point.getX() - POINT_SIZE/2, point.getY() - POINT_SIZE/2, 
                    POINT_SIZE, POINT_SIZE));
            }
            
            // Draw job title below each point (abbreviated)
            JobRecord job = jobs.get(i);
            String label = truncateString(job.jobTitle(), 15);
            
            // Set color back to text color
            g2d.setColor(theme == ColorTheme.DARK ? Color.WHITE : Color.BLACK);
            g2d.setFont(new Font("Dialog", Font.PLAIN, 9));
            
            // Save the current transform
            AffineTransform originalTransform = g2d.getTransform();
            
            // Rotate and position the text
            g2d.rotate(Math.toRadians(-45), point.getX(), height - PADDING + 5);
            g2d.drawString(label, (int)point.getX(), height - PADDING + 10);
            
            // Restore the original transform
            g2d.setTransform(originalTransform);
        }
    }
    
    /**
     * Calculates the average of min and max salary.
     * Returns 0 if no valid salary data.
     */
    private int calculateAverageSalary(JobRecord job) {
        int min = job.annualSalaryMin();
        int max = job.annualSalaryMax();
        
        if (min <= 0 && max <= 0) {
            return 0; // No valid salary data
        }
        
        if (min <= 0) {
            return max; // Use max if min is invalid
        }
        
        if (max <= 0) {
            return min; // Use min if max is invalid
        }
        
        return (min + max) / 2; // Use average otherwise
    }
    
    /**
     * Truncates a string to a maximum length with an ellipsis.
     */
    private String truncateString(String str, int maxLength) {
        if (str == null || str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}