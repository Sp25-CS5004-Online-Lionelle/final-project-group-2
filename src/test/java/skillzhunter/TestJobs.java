package skillzhunter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import skillzhunter.model.JobRecord;
import skillzhunter.model.formatters.DataFormatter;
import skillzhunter.model.formatters.Formats;

public class TestJobs {
    private HashMap<Integer, JobRecord> jobRecords = new HashMap<Integer, JobRecord>();
    private JobRecord jobRecord1;
    private JobRecord jobRecord2;
    private JobRecord jobRecord3;

    @BeforeEach
    /**
     * Sets up the test data for job records.
     * This method initializes three job records with various attributes.
     * The records are then stored in a HashMap for later use in tests.
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

        jobRecords.put(1, jobRecord1);
        jobRecords.put(2, jobRecord2);
        jobRecords.put(3, jobRecord3);
    }

    /*
     * Test each jobrecord with methods in the bean.
     */
    @Test
    public void testJobRecord() {
        assertEquals(jobRecord1.id(), 0);
        assertEquals(jobRecord1.url(), "https://www.jpmorgan.com/jobs/ai-senior-python-engineer");
        assertEquals(jobRecord1.jobSlug(), "ai-senior-python-engineer");
        assertEquals(jobRecord1.jobTitle(), "AI Senior Python Engineer");
        assertEquals(jobRecord1.companyName(), "JP Morgan");
        assertEquals(jobRecord1.companyLogo(), "https://logo.clearbit.com/jpmorgan.com");
        assertEquals(jobRecord1.jobIndustry(), List.of("Finance", "Technology"));
        assertEquals(jobRecord1.jobType(), List.of("Full-time", "Remote"));
        assertEquals(jobRecord1.jobGeo(), "New York, NY");
        assertEquals(jobRecord1.jobLevel(), "Senior");
        assertEquals(jobRecord1.jobExcerpt(), "Join a leading financial institution as an AI Engineer.");
        assertEquals(jobRecord1.jobDescription(), "Work on state-of-the-art Python solutions in a top-tier banking environment.");
        assertEquals(jobRecord1.pubDate(), "2025-03-30");
        assertEquals(jobRecord1.annualSalaryMin(), 120000);
        assertEquals(jobRecord1.annualSalaryMax(), 150000);
        assertEquals(jobRecord1.salaryCurrency(), "USD");
        assertEquals(jobRecord1.rating(), 4);
        assertEquals(jobRecord1.comments(), "Big fan of Python and AI. Looking for a challenging role in finance.");

    }
}