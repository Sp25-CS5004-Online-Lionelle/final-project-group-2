package skillzhunter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import skillzhunter.model.JobRecord;
import skillzhunter.model.JobBean;
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

    /**
     * Test custom export of CSV.
     */
    @Test
    public void testExportCustomCSV() throws IOException {
        // Create a temporary file path for our test CSV
        Path outputCsvPath = tempDir.resolve("custom_export.csv");
        String filePath = outputCsvPath.toString();
        
        // Export the data
        DataFormatter.exportCustomCSV(jobRecords, filePath);
        
        // Verify the file exists
        assertTrue(Files.exists(outputCsvPath), "Exported CSV file should exist");
        
        // Read the content and verify it
        String content = Files.readString(outputCsvPath);
        
        // Verify header is present
        assertTrue(content.startsWith("id,url,jobSlug,jobTitle,companyName,companyLogo,"), 
                  "CSV should have the expected header");
        
        // Verify data for all three records is present
        assertTrue(content.contains("\"JP Morgan\""), "First record company name should be in the CSV");
        assertTrue(content.contains("\"Meta\""), "Second record company name should be in the CSV");
        assertTrue(content.contains("\"United Nations\""), "Third record company name should be in the CSV");
        
        // Verify all records are included (header + 3 records = 4 lines)
        long lineCount = Files.lines(outputCsvPath).count();
        assertEquals(4, lineCount, "CSV should contain exactly 4 lines (header + 3 records)");
    }

    /**
     * Test strips HTML tags from a string.
     */
    @Test
    public void testStripHTML() {
        // Test with HTML content
        String html = "<p>This is a <strong>bold</strong> statement.</p>";
        String expected = "This is a bold statement.";
        assertEquals(expected, DataFormatter.stripHTML(html), "HTML tags should be removed");
        
        // Test with HTML content and multiple spaces
        html = "<div>Multiple    spaces   should be   <span>normalized</span>.</div>";
        expected = "Multiple spaces should be normalized.";
        assertEquals(expected, DataFormatter.stripHTML(html), "HTML tags should be removed and spaces normalized");
        
        // Test with no HTML content
        html = "Plain text with no HTML.";
        expected = "Plain text with no HTML.";
        assertEquals(expected, DataFormatter.stripHTML(html), "Plain text should remain unchanged");
        
        // Test with null input
        assertEquals("", DataFormatter.stripHTML(null), "Null input should return empty string");
        
        // Test with complex nested HTML
        html = "<div class='container'><header>Header</header><main><p>Main <em>content</em> here.</p></main></div>";
        expected = "Header Main content here.";
        assertEquals(expected, DataFormatter.stripHTML(html), "Complex nested HTML should be handled correctly");
    }

    /**
     * Test extract first sentence from a text string.
     */
    @Test
    public void testExtractFirstSentence() {
        // Test with a simple sentence ending with a period
        String text = "This is the first sentence. This is the second sentence.";
        String expected = "This is the first sentence.";
        assertEquals(expected, DataFormatter.extractFirstSentence(text), "Should extract first sentence ending with period");
        
        // Test with a sentence ending with an exclamation mark
        text = "Wow! This is the second sentence.";
        expected = "Wow!";
        assertEquals(expected, DataFormatter.extractFirstSentence(text), "Should extract first sentence ending with exclamation mark");
        
        // Test with a sentence ending with a question mark
        text = "Is this working? I hope so.";
        expected = "Is this working?";
        assertEquals(expected, DataFormatter.extractFirstSentence(text), "Should extract first sentence ending with question mark");
        
        // Test with no sentence terminators
        text = "This is a text without sentence terminators";
        expected = text; // should return the whole text if under 100 chars
        assertEquals(expected, DataFormatter.extractFirstSentence(text), "Should return the same text if no sentence terminators and under 100 chars");
        
        // Test with a very long text without sentence terminators (over 100 chars)
        text = "This is a very long text without any proper sentence terminator and it should be truncated after one hundred characters with ellipsis";
        expected = "This is a very long text without any proper sentence terminator and it should be truncated after ...";
        assertEquals(expected, DataFormatter.extractFirstSentence(text), "Should truncate after 97 chars with ellipsis");
        
        // Test with null or empty input
        assertEquals("", DataFormatter.extractFirstSentence(null), "Null input should return empty string");
        assertEquals("", DataFormatter.extractFirstSentence(""), "Empty input should return empty string");
    }

    /**
     * Tests escaping special characters for CSV.
     */
    @Test
    public void testEscapeCSV() {
        // Test with a string containing double quotes
        String value = "Text with \"quotes\" inside";
        String expected = "\"Text with \"\"quotes\"\" inside\"";
        assertEquals(expected, DataFormatter.escapeCSV(value), "Double quotes should be escaped by doubling them");
        
        // Test with a normal string
        value = "Normal text";
        expected = "\"Normal text\"";
        assertEquals(expected, DataFormatter.escapeCSV(value), "Normal text should be wrapped in double quotes");
        
        // Test with null value
        assertEquals("\"\"", DataFormatter.escapeCSV(null), "Null should be converted to empty quoted string");
        
        // Test with an empty string
        value = "";
        expected = "\"\"";
        assertEquals(expected, DataFormatter.escapeCSV(value), "Empty string should be wrapped in double quotes");
        
        // Test with a string containing commas
        value = "Text with, commas";
        expected = "\"Text with, commas\"";
        assertEquals(expected, DataFormatter.escapeCSV(value), "Commas should be included in the quoted string");
        
        // Test with a string containing both quotes and commas
        value = "Text with \"quotes\" and, commas";
        expected = "\"Text with \"\"quotes\"\" and, commas\"";
        assertEquals(expected, DataFormatter.escapeCSV(value), "Both quotes and commas should be handled correctly");
    }

    /**
     * Tests replacing HTML special characters.
     */
    @Test
    public void testReplaceHTMLSpecialChars() {
        // Test with basic HTML entities
        String html = "This &amp; that";
        String expected = "This & that";
        assertEquals(expected, DataFormatter.replaceHtmlEntities(html), "Basic HTML entities should be replaced");
        
        // Test with multiple entities
        html = "&lt;div&gt;Text&lt;/div&gt;";
        expected = "<div>Text</div>";
        assertEquals(expected, DataFormatter.replaceHtmlEntities(html), "Multiple HTML entities should be replaced");
        
        // Test with nested entities
        html = "This &amp;amp; that";
        expected = "This & that";
        assertEquals(expected, DataFormatter.replaceHtmlEntities(html), "Nested HTML entities should be replaced");
        
        // Test with numeric entities
        html = "Copyright &#169; 2025";
        expected = "Copyright © 2025";
        assertEquals(expected, DataFormatter.replaceHtmlEntities(html), "Numeric HTML entities should be replaced");
        
        // Test with hexadecimal entities
        html = "Smile &#x1F600;";
        // Note: The exact expected result might depend on how your test environment handles unicode characters
        assertNotEquals(html, DataFormatter.replaceHtmlEntities(html), "Hexadecimal HTML entities should be replaced");
        
        // Test with null or empty input
        assertEquals(null, DataFormatter.replaceHtmlEntities(null), "Null input should return null");
        assertEquals("", DataFormatter.replaceHtmlEntities(""), "Empty input should return empty string");
        
        // Test with a mix of entities
        html = "Special &quot;quotes&quot; and &#169; symbols &amp; more";
        expected = "Special \"quotes\" and © symbols & more";
        assertEquals(expected, DataFormatter.replaceHtmlEntities(html), "Mixed HTML entities should be replaced");
    }
    
    /**
     * Test processing job HTML content
     */
    @Test
    public void testProcessJobHtml() {
        // Create a job record with HTML entities
        JobRecord jobWithHtml = new JobRecord(
            42,
            "https://example.com/jobs/test",
            "test-job",
            "Software &amp; Data Engineer",
            "Tech &amp; Co.",
            "https://logo.example.com",
            List.of("Tech", "Data"),
            List.of("Full-time"),
            "Remote, &lt;USA&gt;",
            "Senior &amp; Mid",
            "&lt;p&gt;This is a job excerpt with &amp;quot;quotes&amp;quot;&lt;/p&gt;",
            "&lt;div&gt;Job description with &lt;strong&gt;HTML&lt;/strong&gt; tags.&lt;/div&gt;",
            "2025-04-01",
            130000,
            160000,
            "USD",
            5,
            "Great team &amp; culture!"
        );
        
        // Process the job HTML
        JobRecord processedJob = DataFormatter.processJobHtml(jobWithHtml);
        
        // Verify HTML entities are replaced correctly
        assertEquals("Software & Data Engineer", processedJob.jobTitle(), "Job title should have HTML entities replaced");
        assertEquals("Tech & Co.", processedJob.companyName(), "Company name should have HTML entities replaced");
        assertEquals("Remote, <USA>", processedJob.jobGeo(), "Job location should have HTML entities replaced");
        assertEquals("Senior & Mid", processedJob.jobLevel(), "Job level should have HTML entities replaced");
        assertEquals("<p>This is a job excerpt with \"quotes\"</p>", processedJob.jobExcerpt(), "Job excerpt should have HTML entities replaced");
        assertEquals("<div>Job description with <strong>HTML</strong> tags.</div>", processedJob.jobDescription(), "Job description should have HTML entities replaced");
        assertEquals("Great team & culture!", processedJob.comments(), "Comments should have HTML entities replaced");
        
        // Verify non-HTML fields are unchanged
        assertEquals(42, processedJob.id(), "ID should remain unchanged");
        assertEquals("https://example.com/jobs/test", processedJob.url(), "URL should remain unchanged");
        assertEquals(130000, processedJob.annualSalaryMin(), "Salary min should remain unchanged");
        assertEquals(160000, processedJob.annualSalaryMax(), "Salary max should remain unchanged");
        assertEquals("USD", processedJob.salaryCurrency(), "Currency should remain unchanged");
        assertEquals(5, processedJob.rating(), "Rating should remain unchanged");
        
        // Test with null input
        assertNull(DataFormatter.processJobHtml(null), "Null input should return null");
    }
}
