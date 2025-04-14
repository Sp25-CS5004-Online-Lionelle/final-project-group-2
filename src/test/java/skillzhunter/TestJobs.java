package skillzhunter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import skillzhunter.controller.IController;
import skillzhunter.model.JobRecord;
import skillzhunter.model.Jobs;
import skillzhunter.model.net.JobBoardApi;
import skillzhunter.model.net.JobBoardApiResult;

/**
 * Comprehensive test class for Jobs model functionality.
 * Covers CRUD operations, search functionality, file operations, and edge cases.
 */
public class TestJobs {
    private Jobs jobList;
    private JobRecord jobRecord1;
    private JobRecord jobRecord2;
    private JobRecord jobRecord3;
    private IController mockController;
    
    @TempDir
    static Path tempDir;

    /**
     * Fake JobBoardApi implementation for testing.
     * This class returns controlled results instead of making real API calls.
     * 
     * Using a fake/mock API is important when testing classes that depend on 
     * external services, like our Jobs class depends on JobBoardApi. This way our
     * tests remain fast, reliable, and focused on testing just the Jobs class behavior.
     */
    class FakeJobBoardApi extends JobBoardApi {
        private List<JobRecord> results;
        private String errorMessage;
        
        // This will be used for testing search
        public FakeJobBoardApi(List<JobRecord> results, String errorMessage) {
            this.results = results;
            this.errorMessage = errorMessage;
        }
        
        @Override
        public JobBoardApiResult getJobBoard(String query, Integer numberOfResults, String location, String industry) {
            // Track the parameters for verification in tests if needed
            this.lastQuery = query;
            this.lastNumberOfResults = numberOfResults;
            this.lastLocation = location;
            this.lastIndustry = industry;
            
            return new JobBoardApiResult(results, errorMessage);
        }
        
        // Fields to record parameters for verification
        private String lastQuery;
        private Integer lastNumberOfResults;
        private String lastLocation;
        private String lastIndustry;
        
        // Getters for verification
        public String getLastQuery() { return lastQuery; }
        public Integer getLastNumberOfResults() { return lastNumberOfResults; }
        public String getLastLocation() { return lastLocation; }
        public String getLastIndustry() { return lastIndustry; }
    }

    /**
     * Sets up the test data for job records.
     * This method initializes three job records with various attributes.
     * The records are then added to the jobList for later use in tests.
     */
    @BeforeEach
    public void setUp() {
        // Creates mock controller using Mockito but doesn't have real implementation.
        // This mock will record method calls to verify them later.
        mockController = mock(IController.class);
        
        // Create regular Jobs instance for most tests
        jobList = new Jobs();
        jobList.setController(mockController);
        
        // Initialize test data
        jobRecord1 = new JobRecord(
            0,
            "https://www.jpmorgan.com/jobs/ai-senior-python-engineer",
            "ai-senior-python-engineer",
            "AI Senior Python Engineer",
            "JP Morgan",
            "https://logo.clearbit.com/jpmorgan.com",
            List.of("Finance", "Technology"),
            List.of("Full-time", "Remote"),
            "New York, NY",
            "Senior",
            "Join a leading financial institution as an AI Engineer.",
            "Work on state-of-the-art Python solutions in a top-tier banking environment.",
            "2025-03-30",
            120000,
            150000,
            "USD",
            4,
            "Big fan of Python and AI. Looking for a challenging role in finance."
        );

        jobRecord2 = new JobRecord(
            1,
            "https://www.meta.com/careers/frontend-react-engineer",
            "frontend-react-engineer",
            "Frontend React Engineer",
            "Meta",
            "https://logo.clearbit.com/meta.com",
            List.of("Technology", "Social Media"),
            List.of("Full-time", "Hybrid"),
            "Menlo Park, CA",
            "Mid",
            "Work on the frontend team behind Meta products.",
            "Build scalable, beautiful interfaces using React and modern web tech.",
            "2025-03-25",
            110000,
            140000,
            "USD",
            3,
            "Passionate about user experience and modern web technologies."
        );

        jobRecord3 = new JobRecord(
            2,
            "https://careers.un.org/jobs/data-policy-analyst",
            "data-policy-analyst",
            "Data & Policy Analyst",
            "United Nations",
            "https://logo.clearbit.com/un.org",
            List.of("Public Sector", "Data Analysis"),
            List.of("Contract", "On-site"),
            "Geneva, Switzerland",
            "Entry",
            "Help shape data policy for international development.",
            "Analyze data and draft reports to support UN development goals.",
            "2025-03-15",
            70000,
            85000,
            "CHF",
            5,
            "Excited to work on data policy for global impact."
        );

        // Adding job records to the Jobs list
        jobList.addJob(jobRecord1);
        jobList.addJob(jobRecord2);
        jobList.addJob(jobRecord3);
    }

    /**
     * Test setup to ensure the list is initialized correctly.
     */
    @Test
    public void testSetup() {
        assertEquals(3, jobList.getJobRecords().size());
    }

    /**
     * Test retrieval of a job by title.
     */
    @Test
    public void testGetJob() {
        JobRecord fetchedJob = jobList.getJobRecord("Frontend React Engineer");
        assertEquals(jobRecord2.id(), fetchedJob.id());
        assertEquals(jobRecord2.jobTitle(), fetchedJob.jobTitle());
        assertEquals(jobRecord2.companyName(), fetchedJob.companyName());
    }
    
    /**
     * Test retrieval of a job by title that doesn't exist.
     */
    @Test
    public void testGetJobNotFound() {
        JobRecord fetchedJob = jobList.getJobRecord("Non-existent Job");
        assertNull(fetchedJob);
    }

    /**
     * Test retrieval of all job records.
     */
    @Test
    public void testGetJobRecords() {
        List<JobRecord> allJobs = jobList.getJobRecords();
        assertEquals(3, allJobs.size());
    }
    
    /**
     * Test retrieval of all job records when list is empty.
     */
    @Test
    public void testGetJobRecordsEmpty() {
        Jobs emptyJobs = new Jobs();
        List<JobRecord> allJobs = emptyJobs.getJobRecords();
        assertTrue(allJobs.isEmpty());
    }

    /**
     * Test removing a job by ID.
     */
    @Test
    public void testRemoveJob() {
        // Ensure remove job works as expected
        boolean removed = jobList.removeJob(1);  // Trying to remove job with ID 1
        assertTrue(removed);
        assertEquals(2, jobList.getJobRecords().size());  // After removal, there should be 2 jobs left
    }
    
    /**
     * Test removing a job that doesn't exist.
     */
    @Test
    public void testRemoveJobNotFound() {
        // Try to remove a job with an ID that doesn't exist
        boolean removed = jobList.removeJob(999);
        assertFalse(removed);
        assertEquals(3, jobList.getJobRecords().size());  // No jobs should be removed
    }

    /**
     * Test updating a job's comments and rating.
     */
    @Test
    public void testUpdateJob() {
        // Update job with ID 0
        jobList.updateJob(0, "Updated comments", 5);
        JobRecord updatedJob = jobList.getJobRecord("AI Senior Python Engineer");
        assertEquals("Updated comments", updatedJob.comments());
        assertEquals(5, updatedJob.rating());
    }
    
    /**
     * Test updating a job that doesn't exist.
     */
    @Test
    public void testUpdateJobNotFound() {
        // Try to update a job with an ID that doesn't exist
        jobList.updateJob(999, "Updated comments", 5);
        // No exception should be thrown, and the job list should remain unchanged
        assertEquals(3, jobList.getJobRecords().size());
        // Verify no jobs were changed
        assertEquals(4, jobList.getJobRecord("AI Senior Python Engineer").rating());
    }
    
    /**
     * Test adding a job.
     */
    @Test
    public void testAddJob() {
        JobRecord newJob = new JobRecord(
            3,
            "https://www.example.com/jobs/software-engineer",
            "software-engineer",
            "Software Engineer",
            "Example Company",
            "https://logo.clearbit.com/example.com",
            List.of("Technology"),
            List.of("Full-time"),
            "Remote",
            "Mid",
            "Develop and maintain software applications.",
            "Collaborate with cross-functional teams to define, design, and ship new features.",
            "2025-04-01",
            90000,
            120000,
            "USD",
            4,
            "Looking for a challenging role in software development."
        );

        jobList.addJob(newJob);
        assertEquals(4, jobList.getJobRecords().size());
        
        // Verify the job was added correctly
        JobRecord fetchedJob = jobList.getJobRecord("Software Engineer");
        assertNotNull(fetchedJob);
        assertEquals(newJob.id(), fetchedJob.id());
        assertEquals(newJob.jobTitle(), fetchedJob.jobTitle());
    }
    
    /**
     * Test adding a job with null or empty fields to ensure proper defaults.
     */
    @Test
    public void testAddJobWithNullFields() {
        JobRecord nullJob = new JobRecord(
            4,
            null,  // url
            null,  // jobSlug
            null,  // jobTitle
            null,  // companyName
            null,  // companyLogo
            null,  // jobIndustry
            null,  // jobType
            null,  // jobGeo
            null,  // jobLevel
            null,  // jobExcerpt
            null,  // jobDescription
            null,  // pubDate
            0,     // annualSalaryMin
            0,     // annualSalaryMax
            null,  // salaryCurrency
            0,     // rating
            null   // comments
        );
        
        jobList.addJob(nullJob);
        assertEquals(4, jobList.getJobRecords().size());
        
        // Since we can't retrieve by null title, get all jobs and find the one with ID 4
        List<JobRecord> allJobs = jobList.getJobRecords();
        JobRecord addedJob = null;
        for (JobRecord job : allJobs) {
            if (job.id() == 4) {
                addedJob = job;
                break;
            }
        }
        
        assertNotNull(addedJob);
        assertEquals("", addedJob.jobTitle());
        assertEquals("", addedJob.companyName());
        assertTrue(addedJob.jobIndustry().isEmpty());
        assertTrue(addedJob.jobType().isEmpty());
        assertEquals("", addedJob.jobGeo());
        assertEquals("", addedJob.jobLevel());
        assertEquals("", addedJob.pubDate());
        assertEquals("", addedJob.salaryCurrency());
        assertEquals("No comments provided", addedJob.comments());
    }
    
    /**
     * Test adding a job with empty lists.
     */
    @Test
    public void testAddJobWithEmptyLists() {
        JobRecord emptyListsJob = new JobRecord(
            5,
            "https://www.example.com/job",
            "job-slug",
            "Test Job",
            "Test Company",
            "https://logo.example.com",
            Collections.emptyList(),  // Empty jobIndustry
            Collections.emptyList(),  // Empty jobType
            "Remote",
            "Entry",
            "Excerpt",
            "Description",
            "2025-04-01",
            50000,
            60000,
            "USD",
            3,
            "Comments"
        );
        
        jobList.addJob(emptyListsJob);
        assertEquals(4, jobList.getJobRecords().size());
        
        JobRecord fetchedJob = jobList.getJobRecord("Test Job");
        assertNotNull(fetchedJob);
        assertTrue(fetchedJob.jobIndustry().isEmpty());
        assertTrue(fetchedJob.jobType().isEmpty());
    }
    
    /**
     * Test adding a job with lists containing blank strings.
     */
    @Test
    public void testAddJobWithBlanksInLists() {
        List<String> listWithBlanks = new ArrayList<>();
        listWithBlanks.add("");
        listWithBlanks.add("  ");
        
        JobRecord blankListsJob = new JobRecord(
            6,
            "https://www.example.com/job",
            "job-slug",
            "Blank Lists Job",
            "Test Company",
            "https://logo.example.com",
            listWithBlanks,  // jobIndustry with blanks
            listWithBlanks,  // jobType with blanks
            "Remote",
            "Entry",
            "Excerpt",
            "Description",
            "2025-04-01",
            50000,
            60000,
            "USD",
            3,
            "Comments"
        );
        
        jobList.addJob(blankListsJob);
        assertEquals(4, jobList.getJobRecords().size());
        
        JobRecord fetchedJob = jobList.getJobRecord("Blank Lists Job");
        assertNotNull(fetchedJob);
        // Lists with blanks should be replaced with empty lists
        assertTrue(fetchedJob.jobIndustry().isEmpty());
        assertTrue(fetchedJob.jobType().isEmpty());
    }
    
    /**
     * Test searching for jobs with a fake API.
     * 
     * Here we're testing the searchJobs method which depends on the JobBoardApi.
     * Instead of using a real API that would make actual network calls, we use
     * our fake implementation that returns controlled results.
     */
    @Test
    public void testSearchJobs() {
        // Create a fake JobBoardApi with controlled results
        final FakeJobBoardApi fakeApi = new FakeJobBoardApi(
            List.of(jobRecord1, jobRecord2),
            null
        );
        
        // Create a Jobs instance that uses our fake API
        // This is a technique called "dependency injection via subclassing"
        // We override the createJobBoardApi method to return our fake API
        Jobs testableJobs = new Jobs() {
            @Override
            protected JobBoardApi createJobBoardApi() {
                return fakeApi;
            }
        };
        testableJobs.setController(mockController);
        
        // Perform the search
        List<JobRecord> results = testableJobs.searchJobs("python", 2, "New York", "Technology");
        
        // Verify the results
        assertEquals(2, results.size());
        assertEquals(jobRecord1.id(), results.get(0).id());
        assertEquals(jobRecord2.id(), results.get(1).id());
        
        // Verify parameters were passed correctly to the API
        assertEquals("python", fakeApi.getLastQuery());
        assertEquals(Integer.valueOf(2), fakeApi.getLastNumberOfResults());
        assertEquals("New York", fakeApi.getLastLocation());
        assertEquals("Technology", fakeApi.getLastIndustry());
    }
    
    /**
     * Test searching for jobs with an error message.
     * 
     * In this test, we're checking how the Jobs class handles errors from the API.
     * We configure our fake API to return an error message and verify that 
     * the Jobs class properly forwards that error to the controller.
     */
    @Test
    public void testSearchJobsWithError() {
        // Create a fake JobBoardApi that returns an error
        String errorMessage = "Error searching for jobs";
        final FakeJobBoardApi fakeApi = new FakeJobBoardApi(
            Collections.emptyList(),
            errorMessage
        );
        
        // Create a Jobs instance that uses our fake API
        Jobs testableJobs = new Jobs() {
            @Override
            protected JobBoardApi createJobBoardApi() {
                return fakeApi;
            }
        };
        testableJobs.setController(mockController);
        
        // Perform the search
        List<JobRecord> results = testableJobs.searchJobs("python", 2, "New York", "Technology");
        
        // Verify the results are empty because of the error
        assertTrue(results.isEmpty());
        
        // Verify the error message was sent to the controller
        // This is where Mockito helps us - we can verify that sendAlert was called
        // with the exact error message we set up
        verify(mockController).sendAlert(errorMessage);
    }
    
    /**
     * Test saving jobs to CSV.
     */
    @Test
    public void testSaveJobsToCsv() throws IOException {
        // Create a temporary file for the CSV
        Path csvPath = tempDir.resolve("jobs.csv");
        
        // Save the jobs to CSV
        jobList.saveJobsToCsv(csvPath.toString());
        
        // Verify the file was created
        File csvFile = csvPath.toFile();
        assertTrue(csvFile.exists());
        assertTrue(csvFile.length() > 0);
        
        // Read the file content to verify structure
        List<String> lines = Files.readAllLines(csvPath);
        assertTrue(lines.size() > 1, "CSV should have at least a header row and data rows");
        
        // Check that header contains expected columns
        String header = lines.get(0);
        assertTrue(header.contains("jobTitle"), "Header should contain jobTitle column");
        
        // Verify that the file contains data for all our jobs
        String fileContent = String.join("\n", lines);
        assertTrue(fileContent.contains("AI Senior Python Engineer"), "CSV should contain first job title");
        assertTrue(fileContent.contains("Frontend React Engineer"), "CSV should contain second job title");
        assertTrue(fileContent.contains("Data & Policy Analyst"), "CSV should contain third job title");
    }
    
    /**
     * Test exporting jobs to different formats.
     */
    @Test
    public void testExportSavedJobs() throws IOException {
        // Create temporary files for each format
        Path csvPath = tempDir.resolve("jobs.csv");
        Path jsonPath = tempDir.resolve("jobs.json");
        Path xmlPath = tempDir.resolve("jobs.xml");
        
        // Export the jobs in different formats
        jobList.exportSavedJobs(jobList.getJobRecords(), "CSV", csvPath.toString());
        jobList.exportSavedJobs(jobList.getJobRecords(), "JSON", jsonPath.toString());
        jobList.exportSavedJobs(jobList.getJobRecords(), "XML", xmlPath.toString());
        
        // Verify the files were created
        assertTrue(csvPath.toFile().exists());
        assertTrue(jsonPath.toFile().exists());
        assertTrue(xmlPath.toFile().exists());
        
        // Verify each file has content
        assertTrue(csvPath.toFile().length() > 0);
        assertTrue(jsonPath.toFile().length() > 0);
        assertTrue(xmlPath.toFile().length() > 0);
    }
    
    /**
     * Test exporting jobs with an invalid format.
     */
    @Test
    public void testExportSavedJobsInvalidFormat() {
        // Attempt to export with an invalid format
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            jobList.exportSavedJobs(jobList.getJobRecords(), "INVALID", tempDir.resolve("invalid.txt").toString());
        });
        
        // Verify the exception message
        assertEquals("Unsupported format: INVALID", exception.getMessage());
    }
    
    /**
     * Test getting the list of available industries.
     * 
     * Based on the industries.csv data provided, we can now test for specific
     * industries that we know should be in the list.
     */
    @Test
    public void testGetIndustries() {
        List<String> industries = jobList.getIndustries();
        assertNotNull(industries);
        
        // Test specific industries from the CSV file
        assertTrue(industries.contains("programming"), "Industries should contain 'programming'");
        assertTrue(industries.contains("engineering"), "Industries should contain 'engineering'");
        assertTrue(industries.contains("data science"), "Industries should contain 'data science'");
        assertTrue(industries.contains("devops & sysadmin"), "Industries should contain 'devops & sysadmin'");
        
        // Test for the "any" industry which is special
        assertTrue(industries.contains("any"), "Industries should contain 'any'");
    }
    
    /**
     * Test getting the list of available locations.
     * 
     * Based on the locations.csv data provided, we can now test for specific
     * locations that we know should be in the list.
     */
    @Test
    public void testGetLocations() {
        List<String> locations = jobList.getLocations();
        assertNotNull(locations);
        
        // Test specific locations from the CSV file
        assertTrue(locations.contains("usa"), "Locations should contain 'usa'");
        assertTrue(locations.contains("australia"), "Locations should contain 'australia'");
        assertTrue(locations.contains("china"), "Locations should contain 'china'");
        
        // Test for the "anywhere" location which is special
        assertTrue(locations.contains("anywhere"), "Locations should contain 'anywhere'");
    }
    
    /**
     * Test sending an alert through the controller.
     * 
     * Here we use Mockito to verify that the controller's sendAlert method
     * was called with the expected message.
     */
    @Test
    public void testSendAlert() {
        String alertMessage = "Test alert message";
        jobList.sendAlert(alertMessage);
        
        // Verify the controller's sendAlert method was called with the correct message
        // This is the power of Mockito - we can verify exactly how our mocked object was used
        verify(mockController).sendAlert(alertMessage);
    }
    
    /**
     * Test sending multiple alerts through the controller.
     * 
     * This test verifies that multiple alerts can be sent through the controller
     * and that Mockito properly tracks all of them.
     */
    @Test
    public void testMultipleAlerts() {
        // Send first alert
        String alert1 = "First alert";
        jobList.sendAlert(alert1);
        
        // Send second alert
        String alert2 = "Second alert";
        jobList.sendAlert(alert2);
        
        // Verify both alerts were sent
        // Mockito keeps track of all method calls to our mock
        verify(mockController).sendAlert(alert1);
        verify(mockController).sendAlert(alert2);
        
        // Verify exactly 2 calls were made to sendAlert
        verify(mockController, times(2)).sendAlert(any());
    }
}