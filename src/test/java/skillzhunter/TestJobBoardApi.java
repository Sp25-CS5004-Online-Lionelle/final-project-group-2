package skillzhunter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skillzhunter.model.JobRecord;
import skillzhunter.model.net.JobBoardApi;
import skillzhunter.model.net.JobBoardApiResult;

/**
 * Test class for JobBoardApi functionality.
 * Uses a mock implementation to avoid making actual API calls.
 */
public class TestJobBoardApi {
    private MockJobBoardApi jobBoardApi;
    private JobBoardApiResult jobBoardResultObject;
    private String url;

    /**
     * Mock implementation of JobBoardApi that intercepts the URL
     * and returns empty results to avoid making actual API calls.
     */
    class MockJobBoardApi extends JobBoardApi {
        public String interceptedUrl;
    
        /**
         * Overridden method to capture the URL and avoid making actual API calls.
         * 
         * @param url The URL that would be called in the real implementation
         * @return An empty list of JobRecord objects
         */
        @Override
        protected List<JobRecord> searchApi(String url) {
            this.interceptedUrl = url;
            return Collections.emptyList(); // avoid real call
        }
        
        /**
         * Exposes the replaceHtmlEntities method for testing.
         * 
         * @param text The text containing HTML entities to replace
         * @return The text with HTML entities replaced
         */
        public String testReplaceHtmlEntities(String text) {
            return replaceHtmlEntities(text);
        }
        
        /**
         * Exposes the loadCsvData method for testing.
         * Returns an empty map instead of throwing an exception if the file is not found.
         * 
         * @param filePath The path to the CSV file
         * @param key The column name to use as the key
         * @param value The column name to use as the value
         * @return A map of key-value pairs from the CSV file, or an empty map if the file is not found
         */
        public Map<String, String> testLoadCsvData(String filePath, String key, String value) {
            try {
                return loadCsvData(filePath, key, value);
            } catch (RuntimeException e) {
                // For testing, return empty map instead of throwing exception
                return new HashMap<>();
            }
        }
    }

    /**
     * Set up the test environment before each test.
     * Creates a new MockJobBoardApi instance.
     */
    @BeforeEach
    public void setUp() {
        jobBoardApi = new MockJobBoardApi();
    }

    /**
     * Tests a normal API call with all parameters specified.
     * Verifies that the URL is constructed correctly.
     */
    @Test
    public void testNormalGetJobRecords() {
        // Normal Test Case
        jobBoardResultObject = jobBoardApi.getJobBoard("python", 5, "austria", "devops & sysadmin");
        url = jobBoardApi.interceptedUrl;
        assertEquals("https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&industry=admin&tag=python", url);
    }
    
    /**
     * Tests API call with an empty query string.
     * Verifies that the query is replaced with "all" in the URL.
     */
    @Test
    public void testEmptyQuery() {
        // Edge Case: Empty Query
        jobBoardResultObject = jobBoardApi.getJobBoard("", 5, "austria", "devops & sysadmin");
        url = jobBoardApi.interceptedUrl;
        assertEquals("https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&industry=admin&tag=all", url);
    }
    
    /**
     * Tests API call with a null query.
     * Verifies that the query is replaced with "all" in the URL.
     */
    @Test
    public void testNullQuery() {
        // Edge Case: Null Query
        jobBoardResultObject = jobBoardApi.getJobBoard(null, 5, "austria", "devops & sysadmin");
        url = jobBoardApi.interceptedUrl;
        assertEquals("https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&industry=admin&tag=all", url);
    }
    
    /**
     * Tests API call with an invalid location.
     * Verifies that the location parameter is omitted from the URL.
     */
    @Test
    public void testInvalidLocation() {
        // Test Invalid Location
        jobBoardResultObject = jobBoardApi.getJobBoard("python", 5, "invalid_location", "devops & sysadmin");
        url = jobBoardApi.interceptedUrl;
        // Should drop location from the URL since invalid_location won't be found in LOCATION_MAP
        assertEquals("https://jobicy.com/api/v2/remote-jobs?count=5&industry=admin&tag=python", url);
    }
    
    /**
     * Tests API call with an invalid industry.
     * Verifies that the industry parameter is omitted from the URL.
     */
    @Test
    public void testInvalidIndustry() {
        // Test Invalid Industry
        jobBoardResultObject = jobBoardApi.getJobBoard("python", 5, "austria", "invalid_industry");
        url = jobBoardApi.interceptedUrl;
        // Should drop industry from the URL since invalid_industry won't be found in INDUSTRY_MAP
        assertEquals("https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&tag=python", url);
    }
    
    /**
     * Tests API call with a negative number of results.
     * Verifies that the number of results is replaced with the default value (5).
     */
    @Test
    public void testInvalidNumberOfResults() {
        // Test Invalid Number of Results
        jobBoardResultObject = jobBoardApi.getJobBoard("python", -1, "austria", "devops & sysadmin");
        url = jobBoardApi.interceptedUrl;
        assertEquals("https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&industry=admin&tag=python", url);
    }
    
    /**
     * Tests API call with a null number of results.
     * Verifies that the number of results is replaced with the default value (5).
     */
    @Test
    public void testNullNumberOfResults() {
        // Test Null Number of Results
        jobBoardResultObject = jobBoardApi.getJobBoard("python", null, "austria", "devops & sysadmin");
        url = jobBoardApi.interceptedUrl;
        assertEquals("https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&industry=admin&tag=python", url);
    }
    
    /**
     * Tests the single-parameter overload of getJobBoard.
     * Verifies that default values are used for the other parameters.
     */
    @Test
    public void testQueryOnly() {
        // Test query only
        jobBoardResultObject = jobBoardApi.getJobBoard("python");
        url = jobBoardApi.interceptedUrl;
        assertEquals("https://jobicy.com/api/v2/remote-jobs?count=5&tag=python", url);
    }
    
    /**
     * Tests the two-parameter overload of getJobBoard.
     * Verifies that default values are used for the other parameters.
     */
    @Test
    public void testQueryAndCount() {
        // Test query and count
        jobBoardResultObject = jobBoardApi.getJobBoard("python", 10);
        url = jobBoardApi.interceptedUrl;
        assertEquals("https://jobicy.com/api/v2/remote-jobs?count=10&tag=python", url);
    }
    
    /**
     * Tests the three-parameter overload of getJobBoard.
     * Verifies that default values are used for the remaining parameter.
     */
    @Test
    public void testQueryCountAndLocation() {
        // Test query, count, and location
        jobBoardResultObject = jobBoardApi.getJobBoard("python", 10, "austria");
        url = jobBoardApi.interceptedUrl;
        assertEquals("https://jobicy.com/api/v2/remote-jobs?count=10&geo=austria&tag=python", url);
    }
    
    /**
     * Tests API calls with generic query terms.
     * Verifies that each term is replaced with "all" in the URL.
     */
    @Test
    public void testGenericQuery() {
        // Test generic query terms
        for (String genericTerm : new String[] {"any", "all", "all jobs"}) {
            jobBoardResultObject = jobBoardApi.getJobBoard(genericTerm, 5, "austria", "devops & sysadmin");
            url = jobBoardApi.interceptedUrl;
            assertEquals("https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&industry=admin&tag=all", url);
        }
    }
    
    /**
     * Tests API calls with generic location terms.
     * Verifies that the location parameter is omitted from the URL.
     */
    @Test
    public void testGenericLocation() {
        // Test generic location terms
        for (String genericTerm : new String[] {"any", "all", "anywhere"}) {
            jobBoardResultObject = jobBoardApi.getJobBoard("python", 5, genericTerm, "devops & sysadmin");
            url = jobBoardApi.interceptedUrl;
            assertEquals("https://jobicy.com/api/v2/remote-jobs?count=5&industry=admin&tag=python", url);
        }
    }
    
    /**
     * Tests API calls with generic industry terms.
     * Verifies that the industry parameter is omitted from the URL.
     */
    @Test
    public void testGenericIndustry() {
        // Test generic industry terms
        for (String genericTerm : new String[] {"any", "all"}) {
            jobBoardResultObject = jobBoardApi.getJobBoard("python", 5, "austria", genericTerm);
            url = jobBoardApi.interceptedUrl;
            assertEquals("https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&tag=python", url);
        }
    }
    
    /**
     * Tests API call with all generic parameters.
     * Verifies that appropriate defaults are used and an error message is set.
     */
    @Test
    public void testAllGenericParameters() {
        // Test all generic parameters
        jobBoardResultObject = jobBoardApi.getJobBoard("all", 5, "anywhere", "any");
        url = jobBoardApi.interceptedUrl;
        assertEquals("https://jobicy.com/api/v2/remote-jobs?count=5&tag=all", url);
        
        // Error message should be set
        assertNotNull(jobBoardResultObject.getErrorMessage());
        assertTrue(jobBoardResultObject.getErrorMessage().contains("All search parameters were generic"));
    }
    
    /**
     * Tests that spaces in query strings are replaced with plus signs.
     */
    @Test
    public void testQueryWithSpaces() {
        // Test that spaces in query are replaced with plus signs
        jobBoardResultObject = jobBoardApi.getJobBoard("data science", 5);
        url = jobBoardApi.interceptedUrl;
        assertEquals("https://jobicy.com/api/v2/remote-jobs?count=5&tag=data+science", url);
    }
    
    /**
     * Tests the replaceHtmlEntities method with various HTML entities.
     * Verifies that entities are correctly replaced with their character equivalents.
     */
    @Test
    public void testReplaceHtmlEntities() {
        // Test basic HTML entities
        assertEquals("&", jobBoardApi.testReplaceHtmlEntities("&amp;"));
        assertEquals("<", jobBoardApi.testReplaceHtmlEntities("&lt;"));
        assertEquals(">", jobBoardApi.testReplaceHtmlEntities("&gt;"));
        assertEquals("\"", jobBoardApi.testReplaceHtmlEntities("&quot;"));
        
        // Test nested entities
        assertEquals("&", jobBoardApi.testReplaceHtmlEntities("&amp;amp;"));
        
        // Test numeric entities
        assertEquals("©", jobBoardApi.testReplaceHtmlEntities("&#169;"));
        
        // Test multiple entities in a string
        assertEquals("This & that <div>", 
            jobBoardApi.testReplaceHtmlEntities("This &amp; that &lt;div&gt;"));
        
        // Test with null and empty input
        assertNull(jobBoardApi.testReplaceHtmlEntities(null));
        assertEquals("", jobBoardApi.testReplaceHtmlEntities(""));
    }
    
    /**
     * Tests the loadCsvData method with an invalid file path.
     * Verifies that the method handles errors gracefully in the test environment.
     */
    @Test
    public void testLoadCsvData() {
        // This is a limited test since we don't want to create actual files
        // Test with invalid file path (should not throw exception in our mock)
        Map<String, String> result = jobBoardApi.testLoadCsvData("nonexistent.csv", "key", "value");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
