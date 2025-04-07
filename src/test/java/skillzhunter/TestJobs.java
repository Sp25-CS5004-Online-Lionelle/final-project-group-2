package skillzhunter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;

import skillzhunter.model.JobRecord;
import skillzhunter.model.Jobs;

public class TestJobs {
    private Jobs jobList = new Jobs();  // Changed to use Jobs, which internally uses a List
    private JobRecord jobRecord1;
    private JobRecord jobRecord2;
    private JobRecord jobRecord3;

    @BeforeEach
    /**
     * Sets up the test data for job records.
     * This method initializes three job records with various attributes.
     * The records are then added to the jobList for later use in tests.
     */
    public void setUp() {
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

        // Adding job records directly to the Jobs list instead of using addJob method
        jobList.addJob(jobRecord1);
        jobList.addJob(jobRecord2);
        jobList.addJob(jobRecord3);
    }

    // Test setup to ensure the list is initialized correctly
    @Test
    public void testSetup() {
        assertEquals(3, jobList.getJobRecords().size());
    }

    @Test
    public void testGetJob() {
        JobRecord fetchedJob = jobList.getJobRecord("Frontend React Engineer");
        assertEquals(jobRecord2, fetchedJob);
    }

    @Test
    public void testGetJobRecords() {
        List<JobRecord> allJobs = jobList.getJobRecords();
        assertEquals(3, allJobs.size());
    }

    @Test
    public void testRemoveJob() {
        // Ensure remove job works as expected
        boolean removed = jobList.removeJob(1);  // Trying to remove job with ID 1
        assertTrue(removed);
        assertEquals(2, jobList.getJobRecords().size());  // After removal, there should be 2 jobs left
    }
    @Test
    public void testSearchByQuery() {
        List<JobRecord> results = jobList.searchByQuery("AI");
        assertEquals(1, results.size());
        assertEquals(jobRecord1, results.get(0));
    }
    @Test
    public void testSearchByLocation() {
        List<JobRecord> results = jobList.searchByLocation("New York");
        assertEquals(1, results.size());
        assertEquals(jobRecord1, results.get(0));
    }
    @Test
    public void testSearchByIndustry() {
        List<JobRecord> results = jobList.searchByIndustry("Technology");
        assertEquals(2, results.size());
        assertTrue(results.contains(jobRecord1));
        assertTrue(results.contains(jobRecord2));
    }
    @Test
    public void testSearchByIndustryNotFound() {
        List<JobRecord> results = jobList.searchByIndustry("Nonexistent Industry");
        assertEquals(0, results.size());
    }

    //add test for update jobs
    @Test
    public void testUpdateJob() {
        // Update job with ID 0
        jobList.updateJob(0, "Updated comments", 5);
        JobRecord updatedJob = jobList.getJobRecord("AI Senior Python Engineer");
        assertEquals("Updated comments", updatedJob.comments());
        assertEquals(5, updatedJob.rating());
    }
    
}
