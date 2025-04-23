package skillzhunter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import skillzhunter.controller.AlertObserver;
import skillzhunter.controller.MainController;
import skillzhunter.model.IModel;
import skillzhunter.model.JobRecord;
import skillzhunter.view.IView;

public class TestMainController {
    private MainController controller;
    private IModel mockModel;
    private IView mockView;
    private AlertObserver mockAlertObserver;
    private JobRecord testJob1;
    private JobRecord testJob2;
    private List<JobRecord> testJobList;

    @TempDir
    static Path tempDir;

    @BeforeEach
    public void setUp() {
        // Create mocks
        mockModel = mock(IModel.class);
        mockView = mock(IView.class);
        mockAlertObserver = mock(AlertObserver.class);
        
        // Create controller and connect components
        controller = new MainController();
        controller.setModel(mockModel);
        controller.setView(mockView);
        controller.registerAlertObserver(mockAlertObserver);
        
        // Create test jobs
        testJob1 = new JobRecord(
            1,
            "https://example.com/job/1",
            "job-slug-1",
            "Test Job 1",
            "Test Company 1",
            "https://example.com/logo1.png",
            List.of("Technology"),
            List.of("Full-time"),
            "Remote",
            "Mid",
            "Test job excerpt 1",
            "Test job description 1",
            "2025-04-10",
            100000,
            120000,
            "USD",
            4,
            "Test comments 1"
        );

        testJob2 = new JobRecord(
            2,
            "https://example.com/job/2",
            "job-slug-2",
            "Test Job 2",
            "Test Company 2",
            "https://example.com/logo2.png",
            List.of("Finance"),
            List.of("Part-time"),
            "New York",
            "Senior",
            "Test job excerpt 2",
            "Test job description 2",
            "2025-04-12",
            130000,
            150000,
            "USD",
            5,
            "Test comments 2"
        );

        testJobList = Arrays.asList(testJob1, testJob2);
    }

    /**
     * Test controller initialization.
     */
    @Test
    public void testControllerInitialization() {
        assertEquals(mockModel, controller.getModel(), "Controller should have the correct model");
        assertEquals(mockView, controller.getView(), "Controller should have the correct view");
        
        // Verify the controller registered as the model's alert listener
        verify(mockModel).setAlertListener(controller);
        
        // Verify the controller set itself as the view's controller
        verify(mockView).setController(controller);
    }
    
    /**
     * Test getLocations method.
     */
    @Test
    public void testGetLocations() {
        List<String> testLocations = Arrays.asList("New York", "Remote", "San Francisco");
        when(mockModel.getLocations()).thenReturn(testLocations);
        
        List<String> result = controller.getLocations();
        
        assertEquals(testLocations, result, "Controller should return locations from the model");
        verify(mockModel).getLocations();
    }
    
    /**
     * Test getIndustries method.
     */
    @Test
    public void testGetIndustries() {
        List<String> testIndustries = Arrays.asList("Technology", "Finance", "Healthcare");
        when(mockModel.getIndustries()).thenReturn(testIndustries);
        
        List<String> result = controller.getIndustries();
        
        assertEquals(testIndustries, result, "Controller should return industries from the model");
        verify(mockModel).getIndustries();
    }
    
    /**
     * Test getApiCall method.
     */
    @Test
    public void testGetApiCall() {
        String query = "java";
        Integer numberOfResults = 10;
        String location = "Remote";
        String industry = "Technology";
        
        when(mockModel.searchJobs(query, numberOfResults, location, industry))
            .thenReturn(testJobList);
        
        List<JobRecord> result = controller.getApiCall(query, numberOfResults, location, industry);
        
        assertEquals(testJobList, result, "Controller should return API results from the model");
        verify(mockModel).searchJobs(query, numberOfResults, location, industry);
    }
    
    /**
     * Test getSavedJobs method.
     */
    @Test
    public void testGetSavedJobs() {
        when(mockModel.getJobRecords()).thenReturn(testJobList);
        
        List<JobRecord> result = controller.getSavedJobs();
        
        assertEquals(testJobList, result, "Controller should return saved jobs from the model");
        verify(mockModel).getJobRecords();
    }
    
    /**
     * Test setSavedJobs method.
     */
    @Test
    public void testSetSavedJobs() {
        when(mockModel.getJobRecords()).thenReturn(testJobList);
        
        List<JobRecord> result = controller.setSavedJobs(testJobList);
        
        assertEquals(testJobList, result, "Controller should return saved jobs from the model");
        verify(mockModel).addJob(testJob1);
        verify(mockModel).addJob(testJob2);
        verify(mockModel).getJobRecords();
    }
    
    /**
     * Test isJobAlreadySaved method when job is saved.
     */
    @Test
    public void testIsJobAlreadySavedWhenJobIsSaved() {
        when(mockModel.getJobRecords()).thenReturn(testJobList);
        
        boolean result = controller.isJobAlreadySaved(testJob1);
        
        assertTrue(result, "Controller should detect that job is already saved");
        verify(mockModel).getJobRecords();
    }
    
    /**
     * Test isJobAlreadySaved method when job is not saved.
     */
    @Test
    public void testIsJobAlreadySavedWhenJobIsNotSaved() {
        when(mockModel.getJobRecords()).thenReturn(testJobList);
        
        JobRecord newJob = new JobRecord(
            3,
            "https://example.com/job/3",
            "job-slug-3",
            "Test Job 3",
            "Test Company 3",
            "https://example.com/logo3.png",
            List.of("Healthcare"),
            List.of("Contract"),
            "Chicago",
            "Junior",
            "Test job excerpt 3",
            "Test job description 3",
            "2025-04-15",
            80000,
            90000,
            "USD",
            3,
            "Test comments 3"
        );
        
        boolean result = controller.isJobAlreadySaved(newJob);
        
        assertFalse(result, "Controller should detect that job is not already saved");
        verify(mockModel).getJobRecords();
    }
    
    /**
     * Test jobToSavedList method when job is not already saved.
     */
    @Test
    public void testJobToSavedListWhenJobIsNotAlreadySaved() {
        // Set up the model to return a list that doesn't contain testJob1
        when(mockModel.getJobRecords()).thenReturn(Collections.singletonList(testJob2));
        
        controller.jobToSavedList(testJob1);
        
        // Verify the job was added to the model
        verify(mockModel).addJob(testJob1);
    }
    
    /**
     * Test jobToSavedList method when job is already saved.
     */
    @Test
    public void testJobToSavedListWhenJobIsAlreadySaved() {
        // Set up the model to return a list that contains testJob1
        when(mockModel.getJobRecords()).thenReturn(testJobList);
        
        controller.jobToSavedList(testJob1);
        
        // Verify the job was NOT added to the model again
        verify(mockModel, never()).addJob(testJob1);
    }
    
    /**
     * Test removeJobFromList method.
     */
    @Test
    public void testRemoveJobFromList() {
        int jobId = 1;
        
        controller.removeJobFromList(jobId);
        
        verify(mockModel).removeJob(jobId);
    }
    
    /**
     * Test pathToCSV method.
     */
    @Test
    public void testPathToCSV() {
        String filePath = tempDir.resolve("test.csv").toString();
        
        controller.pathToCSV(filePath);
        
        verify(mockModel).saveJobsToCsv(filePath);
    }
    
    /**
     * Test exportToFileType method.
     */
    @Test
    public void testExportToFileType() {
        String formatStr = "JSON";
        String filePath = tempDir.resolve("test.json").toString();
        
        controller.exportToFileType(testJobList, formatStr, filePath);
        
        verify(mockModel).exportSavedJobs(testJobList, formatStr, filePath);
    }
    
    /**
     * Test getUpdateJob method when job is found.
     */
    @Test
    public void testGetUpdateJobWhenJobIsFound() {
        int jobId = 1;
        String newComments = "Updated comments";
        int newRating = 5;
        
        // Create an updated version of testJob1
        JobRecord updatedJob = new JobRecord(
            jobId,
            testJob1.url(),
            testJob1.jobSlug(),
            testJob1.jobTitle(),
            testJob1.companyName(),
            testJob1.companyLogo(),
            testJob1.jobIndustry(),
            testJob1.jobType(),
            testJob1.jobGeo(),
            testJob1.jobLevel(),
            testJob1.jobExcerpt(),
            testJob1.jobDescription(),
            testJob1.pubDate(),
            testJob1.annualSalaryMin(),
            testJob1.annualSalaryMax(),
            testJob1.salaryCurrency(),
            newRating,
            newComments
        );
        
        // Mock the model to return the updated job
        when(mockModel.getJobRecords()).thenReturn(Arrays.asList(updatedJob, testJob2));
        
        JobRecord result = controller.getUpdateJob(jobId, newComments, newRating);
        
        // Verify the model was called to update the job
        verify(mockModel).updateJob(jobId, newComments, newRating);
        
        // Verify the correct job was returned
        assertNotNull(result, "Controller should return the updated job");
        assertEquals(jobId, result.id(), "Returned job should have the correct ID");
        assertEquals(newComments, result.comments(), "Returned job should have updated comments");
        assertEquals(newRating, result.rating(), "Returned job should have updated rating");
    }
    
    /**
     * Test getUpdateJob method when job is not found.
     */
    @Test
    public void testGetUpdateJobWhenJobIsNotFound() {
        int jobId = 99; // Non-existent job ID
        String newComments = "Updated comments";
        int newRating = 5;
        
        when(mockModel.getJobRecords()).thenReturn(testJobList);
        
        JobRecord result = controller.getUpdateJob(jobId, newComments, newRating);
        
        // Verify the model was called to update the job
        verify(mockModel).updateJob(jobId, newComments, newRating);
        
        // Verify null is returned when job is not found
        assertNull(result, "Controller should return null when job is not found");
    }
    
    /**
     * Test onAlert method.
     */
    @Test
    public void testOnAlert() {
        String alertMessage = "Test alert message";
        
        controller.onAlert(alertMessage);
        
        // Verify the alert was forwarded to the registered observer
        verify(mockAlertObserver).onAlert(alertMessage);
    }
    
    /**
     * Test sendAlert method with multiple observers.
     */
    @Test
    public void testSendAlertWithMultipleObservers() {
        String alertMessage = "Test alert message";
        
        // Register a second alert observer
        AlertObserver mockAlertObserver2 = mock(AlertObserver.class);
        controller.registerAlertObserver(mockAlertObserver2);
        
        controller.sendAlert(alertMessage);
        
        // Verify the alert was sent to both observers
        verify(mockAlertObserver).onAlert(alertMessage);
        verify(mockAlertObserver2).onAlert(alertMessage);
    }
    
    /**
     * Test unregisterAlertObserver method.
     */
    @Test
    public void testUnregisterAlertObserver() {
        String alertMessage = "Test alert message";
        
        // Unregister the observer
        controller.unregisterAlertObserver(mockAlertObserver);
        
        // Send an alert
        controller.sendAlert(alertMessage);
        
        // Verify the unregistered observer did not receive the alert
        verify(mockAlertObserver, never()).onAlert(alertMessage);
    }
    
    /**
     * Test cleanJobRecord method.
     */
    @Test
    public void testCleanJobRecord() {
        // Create a job with HTML entities
        JobRecord jobWithHtml = new JobRecord(
            1,
            "https://example.com/job/1",
            "job-slug-1",
            "Test Job &amp; Company",
            "Test &lt;Company&gt;",
            "https://example.com/logo1.png",
            List.of("Technology &amp; Software"),
            List.of("Full-time"),
            "Remote",
            "Mid-level",
            "&lt;p&gt;Test job excerpt&lt;/p&gt;",
            "&lt;div&gt;Test job description&lt;/div&gt;",
            "2025-04-10",
            100000,
            120000,
            "USD",
            4,
            "Test &quot;comments&quot;"
        );
        
        // Create a clean version of the job
        JobRecord cleanJob = new JobRecord(
            1,
            "https://example.com/job/1",
            "job-slug-1",
            "Test Job & Company",
            "Test <Company>",
            "https://example.com/logo1.png",
            List.of("Technology & Software"),
            List.of("Full-time"),
            "Remote",
            "Mid-level",
            "<p>Test job excerpt</p>",
            "<div>Test job description</div>",
            "2025-04-10",
            100000,
            120000,
            "USD",
            4,
            "Test \"comments\""
        );
        
        when(mockModel.cleanJob(jobWithHtml)).thenReturn(cleanJob);
        
        JobRecord result = controller.cleanJobRecord(jobWithHtml);
        
        verify(mockModel).cleanJob(jobWithHtml);
        assertEquals(cleanJob, result, "Controller should return the cleaned job from the model");
    }
    
    /**
     * Test suggestQueryCorrection method.
     */
    @Test
    public void testSuggestQueryCorrection() {
        String query = "pythom";
        int resultCount = 0;
        String suggestion = "python";
        
        when(mockModel.suggestQueryCorrection(query, resultCount)).thenReturn(suggestion);
        
        String result = controller.suggestQueryCorrection(query, resultCount);
        
        assertEquals(suggestion, result, "Controller should return the suggestion from the model");
        verify(mockModel).suggestQueryCorrection(query, resultCount);
    }
    
    /**
     * Test null model handling.
     */
    @Test
    public void testNullModelHandling() {
        // Create a controller with a null model
        MainController nullModelController = new MainController();
        nullModelController.setModel(null);
        
        // Test that all methods handle null model gracefully
        assertTrue(nullModelController.getLocations().isEmpty(), "getLocations should return empty list with null model");
        assertTrue(nullModelController.getIndustries().isEmpty(), "getIndustries should return empty list with null model");
        assertTrue(nullModelController.getApiCall("test", 10, "test", "test").isEmpty(), "getApiCall should return empty list with null model");
        assertTrue(nullModelController.getSavedJobs().isEmpty(), "getSavedJobs should return empty list with null model");
        assertTrue(nullModelController.setSavedJobs(testJobList).isEmpty(), "setSavedJobs should return empty list with null model");
        assertFalse(nullModelController.isJobAlreadySaved(testJob1), "isJobAlreadySaved should return false with null model");
        
        // These method calls should not throw exceptions with null model
        nullModelController.jobToSavedList(testJob1);
        nullModelController.removeJobFromList(1);
        nullModelController.pathToCSV("test.csv");
        nullModelController.exportToFileType(testJobList, "JSON", "test.json");
        assertNull(nullModelController.getUpdateJob(1, "test", 5), "getUpdateJob should return null with null model");
        assertNull(nullModelController.cleanJobRecord(testJob1), "cleanJobRecord should return null with null model");
        assertNull(nullModelController.suggestQueryCorrection("test", 0), "suggestQueryCorrection should return null with null model");
    }
    
    /**
     * Test null input handling.
     */
    @Test
    public void testNullInputHandling() {
        // Test that controller methods handle null inputs gracefully
        assertFalse(controller.isJobAlreadySaved(null), "isJobAlreadySaved should return false with null job");
        
        // These method calls should not throw exceptions with null inputs
        controller.jobToSavedList(null);
        controller.pathToCSV(null);
        controller.exportToFileType(null, "JSON", "test.json");
        controller.exportToFileType(testJobList, null, "test.json");
        controller.exportToFileType(testJobList, "JSON", null);
        assertNull(controller.cleanJobRecord(null), "cleanJobRecord should return null with null job");
        
        // Verify that no model methods were called with null inputs
        verify(mockModel, never()).addJob(null);
        verify(mockModel, never()).saveJobsToCsv(null);
        
        // Use proper matchers for all parameters in these verifications
        verify(mockModel, never()).exportSavedJobs(isNull(), anyString(), anyString());
        verify(mockModel, never()).exportSavedJobs(any(), isNull(), anyString());
        verify(mockModel, never()).exportSavedJobs(any(), anyString(), isNull());
        
        verify(mockModel, never()).cleanJob(null);
    }
    
    /**
     * Test alert system with no observers.
     */
    @Test
    public void testAlertSystemWithNoObservers() {
        // Create a fresh controller with no observers
        MainController noObserversController = new MainController();
        noObserversController.setModel(mockModel);
        
        // Sending an alert should not throw an exception
        try {
            noObserversController.sendAlert("Test alert");
            assertTrue(true, "No exception should be thrown when sending alert with no observers");
        } catch (Exception e) {
            fail("Exception should not be thrown when sending alert with no observers");
        }
    }
    
    /**
     * Test registering null observer.
     */
    @Test
    public void testRegisteringNullObserver() {
        // Registering a null observer should not throw an exception
        try {
            controller.registerAlertObserver(null);
            assertTrue(true, "No exception should be thrown when registering null observer");
        } catch (Exception e) {
            fail("Exception should not be thrown when registering null observer");
        }
        
        // Send an alert
        controller.sendAlert("Test alert");
        
        // Only the original observer should receive it
        verify(mockAlertObserver).onAlert("Test alert");
    }
    
    /**
     * Test unregistering observer that was never registered.
     */
    @Test
    public void testUnregisteringNonRegisteredObserver() {
        AlertObserver nonRegisteredObserver = mock(AlertObserver.class);
        
        // Unregistering an observer that was never registered should not throw an exception
        try {
            controller.unregisterAlertObserver(nonRegisteredObserver);
            assertTrue(true, "No exception should be thrown when unregistering non-registered observer");
        } catch (Exception e) {
            fail("Exception should not be thrown when unregistering non-registered observer");
        }
        
        // Send an alert
        controller.sendAlert("Test alert");
        
        // Original observer should receive it, non-registered one should not
        verify(mockAlertObserver).onAlert("Test alert");
        verify(nonRegisteredObserver, never()).onAlert(anyString());
    }
}