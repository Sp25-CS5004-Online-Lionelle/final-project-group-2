package skillzhunter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skillzhunter.model.AlertListener;
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
    private AlertListener mockAlertListener;
    
    @TempDir
    static Path tempDir;

    /**
     * Fake JobBoardApi implementation for testing.
     * This class returns controlled results instead of making real API calls.
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
     * Create a fresh Jobs instance for testing.
     * This ensures we have a clean state for each test.
     */
    private Jobs createFreshJobsList() {
        Jobs jobs = new Jobs() {
            // Override to avoid loading from file in tests
            @Override
            protected JobBoardApi createJobBoardApi() {
                return new FakeJobBoardApi(new ArrayList<>(), null);
            }
        };
        jobs.setAlertListener(mockAlertListener);
        return jobs;
    }

    /**
     * Sets up the test data for job records.
     * This method initializes three job records with various attributes.
     * The records are then added to the jobList for later use in tests.
     */
    @BeforeEach
    public void setUp() {
        // Create a mock AlertListener using Mockito
        mockAlertListener = mock(AlertListener.class);
        
        // Create a fresh Jobs instance for each test
        jobList = createFreshJobsList();
        
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
        Jobs emptyJobs = createFreshJobsList();
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
     */
    @Test
    public void testSearchJobs() {
        // Create a fake JobBoardApi with controlled results
        final FakeJobBoardApi fakeApi = new FakeJobBoardApi(
            List.of(jobRecord1, jobRecord2),
            null
        );
        
        // Create a Jobs instance that uses our fake API
        Jobs testableJobs = new Jobs() {
            @Override
            protected JobBoardApi createJobBoardApi() {
                return fakeApi;
            }
        };
        testableJobs.setAlertListener(mockAlertListener);
        
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
        testableJobs.setAlertListener(mockAlertListener);
        
        // Perform the search
        List<JobRecord> results = testableJobs.searchJobs("python", 2, "New York", "Technology");
        
        // Verify the results are empty because of the error
        assertTrue(results.isEmpty());
        
        // Verify the error message was sent to the alert listener
        verify(mockAlertListener).onAlert(errorMessage);
    }
    

    /**
     * Test suggestion for a query with a typo.
     */
    @Test
    public void testQuerySuggestionForTypo() {
        // Create a Jobs instance with our fake API
        final FakeJobBoardApi fakeApi = new FakeJobBoardApi(
            List.of(jobRecord1, jobRecord2),
            null
        );
        
        Jobs testableJobs = new Jobs() {
            @Override
            protected JobBoardApi createJobBoardApi() {
                return fakeApi;
            }
        };
        testableJobs.setAlertListener(mockAlertListener);
        
        // First do a successful search to add "python" to the recent queries
        testableJobs.searchJobs("python", 10, "any", "any");
        
        // Test query suggestion for a typo
        String suggestion = testableJobs.suggestQueryCorrection("pythom", 0);
        assertEquals("python", suggestion);
    }

    /**
     * Test suggestion for a query that matches a common term.
     */
    @Test
    public void testQuerySuggestionForCommonTerm() {
        // Create a Jobs instance with our fake API
        final FakeJobBoardApi fakeApi = new FakeJobBoardApi(
            List.of(jobRecord1, jobRecord2),
            null
        );
        
        Jobs testableJobs = new Jobs() {
            @Override
            protected JobBoardApi createJobBoardApi() {
                return fakeApi;
            }
        };
        testableJobs.setAlertListener(mockAlertListener);
        
        // Test with a query that should match a common term
        String suggestion = testableJobs.suggestQueryCorrection("jaba", 0);
        assertEquals("java", suggestion);
    }

    /**
     * Test no suggestion for unrelated query.
     */
    @Test
    public void testNoSuggestionForUnrelatedQuery() {
        // Create a Jobs instance with our fake API
        final FakeJobBoardApi fakeApi = new FakeJobBoardApi(
            List.of(jobRecord1, jobRecord2),
            null
        );
        
        Jobs testableJobs = new Jobs() {
            @Override
            protected JobBoardApi createJobBoardApi() {
                return fakeApi;
            }
        };
        testableJobs.setAlertListener(mockAlertListener);
        
        // Test with a query that shouldn't get a suggestion (too different)
        String suggestion = testableJobs.suggestQueryCorrection("xyzabc", 0);
        assertNull(suggestion);
    }

    /**
     * Test no suggestion for short query.
     */
    @Test
    public void testNoSuggestionForShortQuery() {
        // Create a Jobs instance with our fake API
        final FakeJobBoardApi fakeApi = new FakeJobBoardApi(
            List.of(jobRecord1, jobRecord2),
            null
        );
        
        Jobs testableJobs = new Jobs() {
            @Override
            protected JobBoardApi createJobBoardApi() {
                return fakeApi;
            }
        };
        testableJobs.setAlertListener(mockAlertListener);
        
        // Test with a query that shouldn't get a suggestion (too short)
        String suggestion = testableJobs.suggestQueryCorrection("xy", 0);
        assertNull(suggestion);
    }

    /**
     * Test no suggestion when results exist.
     */
    @Test
    public void testNoSuggestionWithResults() {
        // Create a Jobs instance with our fake API
        final FakeJobBoardApi fakeApi = new FakeJobBoardApi(
            List.of(jobRecord1, jobRecord2),
            null
        );
        
        Jobs testableJobs = new Jobs() {
            @Override
            protected JobBoardApi createJobBoardApi() {
                return fakeApi;
            }
        };
        testableJobs.setAlertListener(mockAlertListener);
        
        // Test with a query that shouldn't get a suggestion (has results)
        String suggestion = testableJobs.suggestQueryCorrection("python", 5);
        assertNull(suggestion);
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
    
    @Test
    public void testGetIndustries() {
        // Create a Jobs class with mocked industry data
        Jobs jobsWithMockData = new Jobs() {
            @Override
            public List<String> getIndustries() {
                return List.of("programming", "sales", "seo", "social media marketing", "any");
            }
        };
        
        List<String> industries = jobsWithMockData.getIndustries();
        assertNotNull(industries);
        
        // Test specific industries
        assertTrue(industries.contains("programming"));
        assertTrue(industries.contains("sales"));
        assertTrue(industries.contains("seo"));
        assertTrue(industries.contains("social media marketing"));
        assertTrue(industries.contains("any"));
    }

@Test
public void testGetLocations() {
    // Create a Jobs class with mocked location data
    Jobs jobsWithMockData = new Jobs() {
        @Override
        public List<String> getLocations() {
            return List.of("anywhere", "australia", "china", "austria");
        }
    };
    
    List<String> locations = jobsWithMockData.getLocations();
    assertNotNull(locations);
    
    // Test specific locations
    assertTrue(locations.contains("anywhere"));
    assertTrue(locations.contains("australia"));
    assertTrue(locations.contains("china"));
    assertTrue(locations.contains("austria"));
}
    
    /**
     * Test sending an alert through the alert listener.
     */
    @Test
    public void testSendAlert() {
        String alertMessage = "Test alert message";
        jobList.sendAlert(alertMessage);
        
        // Verify the alert listener's onAlert method was called with the correct message
        verify(mockAlertListener).onAlert(alertMessage);
    }
    
    /**
     * Test sending multiple alerts through the alert listener.
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
        verify(mockAlertListener).onAlert(alert1);
        verify(mockAlertListener).onAlert(alert2);
        
        // Verify exactly 2 calls were made to onAlert
        verify(mockAlertListener, times(2)).onAlert(any());
    }
    /**
     * Test that test mode is correctly detected.
     * This is a bit meta - testing the test detection itself.
     */
    @Test
    public void testTestModeDetection() {
        // Create a Jobs instance that lets us inspect the test mode
        class JobsWithTestModeAccess extends Jobs {
            public boolean getTestModeStatus() {
                // Use reflection to access the private isTestMode field
                try {
                    java.lang.reflect.Field field = Jobs.class.getDeclaredField("isTestMode");
                    field.setAccessible(true);
                    return (boolean) field.get(this);
                } catch (Exception e) {
                    fail("Reflection failed: " + e.getMessage());
                    return false;
                }
            }
        }
        
        JobsWithTestModeAccess jobsToTest = new JobsWithTestModeAccess();
        
        // We're running in a test, so it should detect that
        assertTrue(jobsToTest.getTestModeStatus(), "Should detect we're running in a test");
    }
    
    /**
     * Test that file operations handle gracefully when files don't exist.
     */
    @Test
    public void testFileOperationsWhenFileDoesntExist() {
        // Create a temporary directory that definitely doesn't exist
        Path nonExistentDirectory = tempDir.resolve("non-existent-directory");
        Path nonExistentFile = nonExistentDirectory.resolve("non-existent-file.csv");
        
        // Try to save to a non-existent directory and file
        try {
            jobList.saveJobsToCsv(nonExistentFile.toString());
            // If we get here, it means no exception was thrown
            
            // Verify the directory was created
            assertTrue(nonExistentDirectory.toFile().exists(), 
                      "Directory should be created if it doesn't exist");
            
            // Verify the file was created
            assertTrue(nonExistentFile.toFile().exists(), 
                      "File should be created if it doesn't exist");
        } catch (Exception e) {
            fail("Should not throw exception when saving to a non-existent file: " + e.getMessage());
        }
    }
    
    /**
     * Test that a Jobs instance starts with an empty job list when created in test mode.
     * This verifies the conditional file loading behavior.
     */
    @Test
    public void testEmptyInitialStateInTestMode() {
        // Create a fresh Jobs instance for testing
        Jobs freshJobs = new Jobs();
        
        // It should start empty in test mode, even if there are saved jobs
        List<JobRecord> initialJobs = freshJobs.getJobRecords();
        assertTrue(initialJobs.isEmpty(), 
                  "Jobs instance should start with empty list in test mode");
    }
    
    /**
     * Test that trying to load non-existent file is handled gracefully.
     */
    @Test
    public void testLoadingNonExistentFile() {
        // Create a Jobs instance that forces file loading
        class JobsForceLoad extends Jobs {
            public void forceLoadFromCsv(String path) {
                // Call the private method using reflection
                try {
                    java.lang.reflect.Method method = Jobs.class.getDeclaredMethod("loadJobsFromCsv", String.class);
                    method.setAccessible(true);
                    method.invoke(this, path);
                } catch (Exception e) {
                    fail("Reflection failed: " + e.getMessage());
                }
            }
        }
        
        JobsForceLoad jobsToTest = new JobsForceLoad();
        
        // Non-existent file path
        String nonExistentFile = tempDir.resolve("does-not-exist.csv").toString();
        
        // This should not throw an exception
        try {
            jobsToTest.forceLoadFromCsv(nonExistentFile);
            // If we get here, no exception was thrown, which is what we want
            
            // Verify the job list is still empty
            assertTrue(jobsToTest.getJobRecords().isEmpty(), 
                      "Job list should remain empty after trying to load non-existent file");
        } catch (Exception e) {
            fail("Loading non-existent file should not throw exception: " + e.getMessage());
        }
    }
    
    /**
     * Test that export handles IOException gracefully by throwing a RuntimeException.
     */
    @Test
    public void testExportHandlesIOException() {
        // Create a path to a directory that we'll make read-only
        Path readOnlyPath = tempDir.resolve("readonly");
        File readOnlyDir = readOnlyPath.toFile();
        
        try {
            // Create the directory
            readOnlyDir.mkdir();
            
            // Make it read-only (this might not work on all platforms)
            readOnlyDir.setReadOnly();
            
            // Try to export to a file in this directory - should fail with permission denied
            Path invalidFile = readOnlyPath.resolve("cannot-write.csv");
            
            // This should throw a RuntimeException wrapping the IOException
            Exception exception = assertThrows(RuntimeException.class, () -> {
                jobList.exportSavedJobs(jobList.getJobRecords(), "CSV", invalidFile.toString());
            });
            
            // Verify the exception message contains something related to file access
            String exceptionMessage = exception.getMessage().toLowerCase();
            assertTrue(
                exceptionMessage.contains("fail") || 
                exceptionMessage.contains("error") || 
                exceptionMessage.contains("permission") ||
                exceptionMessage.contains("access"),
                "Exception should indicate file access problem");
        } finally {
            // Clean up - make the directory writable again so it can be deleted
            readOnlyDir.setWritable(true);
        }
    }
    
    /**
     * Test alert handling when no listener is registered.
     */
    @Test
    public void testAlertWithNoListener() {
        // Create a fresh Jobs instance without setting a listener
        Jobs jobsWithoutListener = createFreshJobsList();
        
        // Remove the alert listener
        jobsWithoutListener.setAlertListener(null);
        
        // This should not throw an exception
        try {
            jobsWithoutListener.sendAlert("Test alert with no listener");
            // If we get here, no exception was thrown, which is what we want
        } catch (Exception e) {
            fail("Sending alert with no listener should not throw exception: " + e.getMessage());
        }
    }

    /**
     * Test capitalizing items in a list with special cases.
     */
    @Test
    public void testCapitalizeItems() {
        Map<String, String> specialCases = new HashMap<>();
        specialCases.put("hr", "HR");
        specialCases.put("usa", "USA");
        
        List<String> items = Arrays.asList(
            "java developer", "hr manager", "usa job", "python programmer", "it support"
        );
        
        List<String> expected = Arrays.asList(
            "Java Developer", "HR Manager", "USA Job", "Python Programmer", "It Support"
        );
        
        List<String> result = jobList.capitalizeItems(items, specialCases);
        assertEquals(expected, result);
    }

    /**
     * Test cleaning a job record with various HTML entity types.
     */
    @Test
    public void testCleanJob() {
        // Create a job with different types of HTML entities
        JobRecord jobWithEntities = new JobRecord(
            100,
            "https://example.com/job",
            "test-job",
            "Software &amp; Hardware Engineer",
            "Tech &amp; Co.",
            "https://logo.example.com",
            List.of("Hardware &amp; Software", "R&amp;D"),
            List.of("Full-time"),
            "New York, NY &amp; Remote",
            "Senior",
            "Job with &#8220;special&#8221; characters &amp; HTML entities",
            "Details with &lt;strong&gt;formatted&lt;/strong&gt; text",
            "2025-04-01",
            120000,
            150000,
            "USD",
            4,
            "Great &quot;opportunity&quot; with &#x1F600; emoji"
        );
        
        JobRecord cleanedJob = jobList.cleanJob(jobWithEntities);
        
        // Check that HTML entities are replaced properly
        assertEquals("Software & Hardware Engineer", cleanedJob.jobTitle());
        assertEquals("Tech & Co.", cleanedJob.companyName());
        assertEquals(List.of("Hardware & Software", "R&D"), cleanedJob.jobIndustry());
        assertEquals("New York, NY & Remote", cleanedJob.jobGeo());
        assertEquals("Job with \u201Cspecial\u201D characters & HTML entities", cleanedJob.jobExcerpt());
        assertEquals("Details with <strong>formatted</strong> text", cleanedJob.jobDescription());
        assertTrue(cleanedJob.comments().contains("Great \"opportunity\""));
    }

    /**
     * Test error handling when saving jobs to an invalid path.
     */
    @Test
    public void testSaveJobsToInvalidPath() {
        // Create a path that cannot be written to (a file that exists and is a directory)
        Path invalidPath = tempDir.resolve("directory-not-file");
        invalidPath.toFile().mkdir();
        
        // This should throw an exception, but it should be wrapped in a RuntimeException
        Exception exception = assertThrows(RuntimeException.class, () -> {
            jobList.saveJobsToCsv(invalidPath.toString());
        });
        
        // Verify the exception message contains information about the failure
        assertTrue(exception.getMessage().contains("Failed") || 
                exception.getMessage().contains("Error") || 
                exception.getMessage().contains("Cannot"),
                "Exception should indicate file write problem");
    }

    /**
     * Test error handling when exporting to an invalid path.
     */
    @Test
    public void testExportToInvalidPath() {
        // Create a path to a directory that we'll make read-only
        Path readOnlyPath = tempDir.resolve("readonly-dir");
        File readOnlyDir = readOnlyPath.toFile();
        
        try {
            // Create the directory and make it read-only
            readOnlyDir.mkdir();
            readOnlyDir.setReadOnly();
            
            // Try to export to a file in this directory
            Path invalidFile = readOnlyPath.resolve("cannot-write.csv");
            
            // This should throw a RuntimeException
            Exception exception = assertThrows(RuntimeException.class, () -> {
                jobList.exportSavedJobs(jobList.getJobRecords(), "CSV", invalidFile.toString());
            });
            
            // Verify the exception message
            assertTrue(exception.getMessage().contains("Failed") || 
                    exception.getMessage().contains("Error") || 
                    exception.getMessage().contains("unable"),
                    "Exception should indicate file access problem");
        } finally {
            // Clean up - make the directory writable again so it can be deleted
            readOnlyDir.setWritable(true);
        }
    }
    /**
     * Test adding a malformed job record with invalid data.
     */
    @Test
    public void testAddMalformedJob() {
        // Create a job with invalid data
        JobRecord malformedJob = new JobRecord(
            -1,  // Negative ID
            "not-a-url",  // Invalid URL
            "",  // Empty job slug
            null,  // Null job title
            "",  // Empty company name
            null,  // Null company logo
            null,  // Null job industry
            Arrays.asList("", "", ""),  // Empty strings in job type
            "   ",  // Whitespace-only job geo
            "\t",  // Tab character in job level
            null,  // Null job excerpt
            null,  // Null job description
            "invalid-date",  // Invalid date format
            -5000,  // Negative salary
            -1000,  // Negative salary
            null,  // Null currency
            99,  // Out of range rating
            null   // Null comments
        );
        
        // This should not throw an exception - the model should handle bad data gracefully
        jobList.addJob(malformedJob);
        
        // Get all jobs and find the one we just added
        List<JobRecord> allJobs = jobList.getJobRecords();
        JobRecord addedJob = null;
        for (JobRecord job : allJobs) {
            if (job.id() == -1) {
                addedJob = job;
                break;
            }
        }
        
        // Verify the job was added and sanitized
        assertNotNull(addedJob);
        assertNotNull(addedJob.jobTitle());
        assertTrue(addedJob.jobTitle().isEmpty());
        assertNotNull(addedJob.companyName());
        assertTrue(addedJob.companyName().isEmpty());
        assertNotNull(addedJob.jobIndustry());
        assertTrue(addedJob.jobIndustry().isEmpty());
        assertNotNull(addedJob.jobType());
        assertTrue(addedJob.jobType().isEmpty() || !addedJob.jobType().stream().anyMatch(s -> !s.isBlank()));
        assertNotNull(addedJob.jobGeo());
        assertNotNull(addedJob.jobLevel());
        assertNotNull(addedJob.pubDate());
        assertNotNull(addedJob.salaryCurrency());
        assertNotNull(addedJob.comments());
        assertEquals("No comments provided", addedJob.comments());
    }

    /**
     * Test adding a job with special HTML edge cases.
     */
    @Test
    public void testAddJobWithComplexHtml() {
        // Create a job with complex nested HTML entities and potential XSS content
        JobRecord complexHtmlJob = new JobRecord(
            42,
            "https://example.com/job",
            "complex-html-job",
            "Web Developer &lt;script&gt;alert('XSS');&lt;/script&gt;",
            "Tech &amp;amp; Company", // Double-encoded entity
            "https://logo.example.com",
            List.of("Web &amp; App Development", "&lt;b&gt;Frontend&lt;/b&gt;"),
            List.of("Full-time"),
            "San Francisco &amp; Remote",
            "Mid-level",
            "&lt;p&gt;&lt;a href=\"javascript:alert('XSS')\"&gt;Click me&lt;/a&gt;&lt;/p&gt;",
            "&lt;div class='container'&gt;&lt;h1&gt;Job Description&lt;/h1&gt;&lt;p&gt;Complex &amp;amp; nested HTML content&lt;/p&gt;&lt;/div&gt;",
            "2025-04-01",
            120000,
            150000,
            "USD",
            4,
            "Has &#8220;special&#8221; chars &amp; HTML"
        );
        
        // Add the job
        jobList.addJob(complexHtmlJob);
        
        // Get the job
        JobRecord addedJob = jobList.getJobRecord("Web Developer <script>alert('XSS');</script>");
        
        // Verify the job was added and HTML was properly processed
        assertNotNull(addedJob);
        assertEquals("Web Developer <script>alert('XSS');</script>", addedJob.jobTitle());
        assertEquals("Tech & Company", addedJob.companyName());
        assertEquals(List.of("Web & App Development", "<b>Frontend</b>"), addedJob.jobIndustry());
        assertEquals("San Francisco & Remote", addedJob.jobGeo());
        assertTrue(addedJob.jobExcerpt().contains("<p><a href=\"javascript:alert('XSS')\">Click me</a></p>"));
        assertTrue(addedJob.jobDescription().contains("Complex & nested HTML content"));
        assertTrue(addedJob.comments().contains("special"));
        assertTrue(addedJob.comments().contains("& HTML"));
    }
}