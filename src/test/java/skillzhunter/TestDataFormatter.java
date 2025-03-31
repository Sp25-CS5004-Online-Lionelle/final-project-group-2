package skillzhunter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;

import skillzhunter.model.JobRecord;
import skillzhunter.model.formatters.DataFormatter;
import skillzhunter.model.formatters.Formats;

public class TestDataFormatter {
    private List<JobRecord> jobRecords;

    @TempDir
    static Path tempDir;

    @BeforeEach
    public void setUp() {
        JobRecord record1 = new JobRecord(
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

        JobRecord record2 = new JobRecord(
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

        JobRecord record3 = new JobRecord(
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
            "Eager to contribute to global development through data analysis."
        );

        jobRecords = List.of(record1, record2, record3);
    }

    /**
     * Test to check if there are 3 records found in the before each.
     */
    @Test
    public void testJobRecordCount() {
        assertEquals(3, jobRecords.size());
    }

    /**
     * Test the pretty format.
     * Will come up as pretty format in the Debug Console.
     */
    @Test
    public void testPrettyFormat() {
        DataFormatter.write(jobRecords, Formats.PRETTY, System.out);
    }


    /**
     * Test the json format.
     * Will come up as json format in the Debug Console.
     */
    @Test
    public void testJson() {
        DataFormatter.write(jobRecords, Formats.JSON, System.out);
    }

    /**
     * Test the xml format.
     * Will come up as xml format in the Debug Console.
     */
    @Test
    public void testXml() {
        DataFormatter.write(jobRecords, Formats.XML, System.out);
    }

    /**
     * Test the csv format.
     * Will come up as csv format in the Debug Console.
     */
    @Test
    public void testCsv() {
        DataFormatter.write(jobRecords, Formats.CSV, System.out);
    }

    /**
     * Test writing to file csv
     * Will come up as default format in the Debug Console.
     */
    @Test
    public void testWriteToFile(){
        OutputStream fout = System.out;
        try {
            Path outputCsvPath = tempDir.resolve("output.csv");
            fout = new FileOutputStream(outputCsvPath.toFile());
        } catch (Exception e) {
            System.err.print(e);
        }
        DataFormatter.write(jobRecords, Formats.CSV, fout);
    }

    //test writing to a file json
    /**
     * Test writing to file json
     */
    @Test
    public void testWriteToFileJson(){
        //may do this in argument parser
        OutputStream fout=System.out;
        try {
            Path outputJsonPath = tempDir.resolve("output.json");
            fout = new FileOutputStream(outputJsonPath.toFile());
        } catch (Exception e) {
            System.err.print(e);
        }
        DataFormatter.write(jobRecords, Formats.JSON, fout);
    }

    //test writing to a file xml
    /**
     * Test writing to file xml
     */
    @Test
    public void testWriteToFileXml(){
        OutputStream fout=System.out;
        try {
            Path outputXmlPath = tempDir.resolve("output.xml");
            fout = new FileOutputStream(outputXmlPath.toFile());
        } catch (Exception e) {
            System.err.print(e);
        }
        DataFormatter.write(jobRecords, Formats.XML, fout);
    }
}
