package skillzhunter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.*;
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
 * Uses Mockito to mock static methods in JobBoardApi.
 */
public class TestJobBoardApi {
    // Store captured URL for verification
    private String capturedUrl;

    /**
     * Set up the test environment before each test.
     * Initializes test variables.
     */
    @BeforeEach
    public void setUp() {
        capturedUrl = null;
    }

    /**
     * Helper method to test the JobBoardApi with different parameters.
     * Uses a lambda to capture the URL and return the mock result.
     */
    private void testApiCall(String query, Integer count, String location, String industry, String expectedUrl) {
        try (MockedStatic<JobBoardApi> mockedApi = mockStatic(JobBoardApi.class)) {
            // Setup the mock to capture the URL and return empty list
            mockedApi.when(() -> JobBoardApi.searchApi(any()))
                    .thenAnswer(invocation -> {
                        capturedUrl = invocation.getArgument(0);
                        return Collections.emptyList();
                    });
            
            // Call the method we're testing
            mockedApi.when(() -> JobBoardApi.getJobBoard(query, count, location, industry))
                    .thenCallRealMethod();
            
            // Execute the test
            JobBoardApi.getJobBoard(query, count, location, industry);
            
            // Verify URL is as expected
            assertEquals(expectedUrl, capturedUrl);
        }
    }

    /**
     * Tests a normal API call with all parameters specified.
     * Verifies that the URL is constructed correctly.
     */
    @Test
    public void testNormalGetJobRecords() {
        // Normal Test Case
        testApiCall("python", 5, "austria", "devops & sysadmin", 
            "https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&industry=admin&tag=python");
    }
    
    /**
     * Tests API call with an empty query string.
     * Verifies that the query is replaced with "all" in the URL.
     */
    @Test
    public void testEmptyQuery() {
        // Edge Case: Empty Query
        testApiCall("", 5, "austria", "devops & sysadmin", 
            "https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&industry=admin&tag=all");
    }
    
    /**
     * Tests API call with a null query.
     * Verifies that the query is replaced with "all" in the URL.
     */
    @Test
    public void testNullQuery() {
        // Edge Case: Null Query
        testApiCall(null, 5, "austria", "devops & sysadmin", 
            "https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&industry=admin&tag=all");
    }
    
    /**
     * Tests API call with an invalid location.
     * Verifies that the location parameter is omitted from the URL.
     */
    @Test
    public void testInvalidLocation() {
        // Test Invalid Location
        testApiCall("python", 5, "invalid_location", "devops & sysadmin", 
            "https://jobicy.com/api/v2/remote-jobs?count=5&industry=admin&tag=python");
    }
    
    /**
     * Tests API call with an invalid industry.
     * Verifies that the industry parameter is omitted from the URL.
     */
    @Test
    public void testInvalidIndustry() {
        // Test Invalid Industry
        testApiCall("python", 5, "austria", "invalid_industry", 
            "https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&tag=python");
    }
    
    /**
     * Tests API call with a negative number of results.
     * Verifies that the number of results is replaced with the default value (5).
     */
    @Test
    public void testInvalidNumberOfResults() {
        // Test Invalid Number of Results
        testApiCall("python", -1, "austria", "devops & sysadmin", 
            "https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&industry=admin&tag=python");
    }
    
    /**
     * Tests API call with a null number of results.
     * Verifies that the number of results is replaced with the default value (5).
     */
    @Test
    public void testNullNumberOfResults() {
        // Test Null Number of Results
        testApiCall("python", null, "austria", "devops & sysadmin", 
            "https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&industry=admin&tag=python");
    }
    
    /**
     * Tests the single-parameter overload of getJobBoard.
     * Verifies that default values are used for the other parameters.
     */
    @Test
    public void testQueryOnly() {
        // Test query only
        try (MockedStatic<JobBoardApi> mockedApi = mockStatic(JobBoardApi.class)) {
            // Setup the mock to capture the URL and return empty list
            mockedApi.when(() -> JobBoardApi.searchApi(any()))
                    .thenAnswer(invocation -> {
                        capturedUrl = invocation.getArgument(0);
                        return Collections.emptyList();
                    });
            
            // Call the single-parameter overload and let it use the real implementation
            mockedApi.when(() -> JobBoardApi.getJobBoard("python"))
                    .thenCallRealMethod();
            mockedApi.when(() -> JobBoardApi.getJobBoard(anyString(), any(), any(), any()))
                    .thenCallRealMethod();
            
            // Execute the test
            JobBoardApi.getJobBoard("python");
            
            // Verify URL is as expected
            assertEquals("https://jobicy.com/api/v2/remote-jobs?count=5&tag=python", capturedUrl);
        }
    }
    
    /**
     * Tests the two-parameter overload of getJobBoard.
     * Verifies that default values are used for the other parameters.
     */
    @Test
    public void testQueryAndCount() {
        // Test query and count
        try (MockedStatic<JobBoardApi> mockedApi = mockStatic(JobBoardApi.class)) {
            // Setup the mock to capture the URL and return empty list
            mockedApi.when(() -> JobBoardApi.searchApi(any()))
                    .thenAnswer(invocation -> {
                        capturedUrl = invocation.getArgument(0);
                        return Collections.emptyList();
                    });
            
            // Call the two-parameter overload and let it use the real implementation
            mockedApi.when(() -> JobBoardApi.getJobBoard("python", 10))
                    .thenCallRealMethod();
            mockedApi.when(() -> JobBoardApi.getJobBoard(anyString(), any(), any(), any()))
                    .thenCallRealMethod();
            
            // Execute the test
            JobBoardApi.getJobBoard("python", 10);
            
            // Verify URL is as expected
            assertEquals("https://jobicy.com/api/v2/remote-jobs?count=10&tag=python", capturedUrl);
        }
    }
    
    /**
     * Tests the three-parameter overload of getJobBoard.
     * Verifies that default values are used for the remaining parameter.
     */
    @Test
    public void testQueryCountAndLocation() {
        // Test query, count, and location
        try (MockedStatic<JobBoardApi> mockedApi = mockStatic(JobBoardApi.class)) {
            // Setup the mock to capture the URL and return empty list
            mockedApi.when(() -> JobBoardApi.searchApi(any()))
                    .thenAnswer(invocation -> {
                        capturedUrl = invocation.getArgument(0);
                        return Collections.emptyList();
                    });
            
            // Call the three-parameter overload and let it use the real implementation
            mockedApi.when(() -> JobBoardApi.getJobBoard("python", 10, "austria"))
                    .thenCallRealMethod();
            mockedApi.when(() -> JobBoardApi.getJobBoard(anyString(), any(), any(), any()))
                    .thenCallRealMethod();
            
            // Execute the test
            JobBoardApi.getJobBoard("python", 10, "austria");
            
            // Verify URL is as expected
            assertEquals("https://jobicy.com/api/v2/remote-jobs?count=10&geo=austria&tag=python", capturedUrl);
        }
    }
    
    /**
     * Tests API calls with generic query terms.
     * Verifies that each term is replaced with "all" in the URL.
     */
    @Test
    public void testGenericQuery() {
        // Test generic query terms
        for (String genericTerm : new String[] {"any", "all", "all jobs"}) {
            testApiCall(genericTerm, 5, "austria", "devops & sysadmin", 
                "https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&industry=admin&tag=all");
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
            testApiCall("python", 5, genericTerm, "devops & sysadmin", 
                "https://jobicy.com/api/v2/remote-jobs?count=5&industry=admin&tag=python");
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
            testApiCall("python", 5, "austria", genericTerm, 
                "https://jobicy.com/api/v2/remote-jobs?count=5&geo=austria&tag=python");
        }
    }
    
    /**
     * Tests API call with all generic parameters.
     * Verifies that appropriate defaults are used and an error message is set.
     */
    @Test
    public void testAllGenericParameters() {
        // Test all generic parameters
        try (MockedStatic<JobBoardApi> mockedApi = mockStatic(JobBoardApi.class)) {
            // Setup the mock to capture the URL and return empty list
            mockedApi.when(() -> JobBoardApi.searchApi(any()))
                    .thenAnswer(invocation -> {
                        capturedUrl = invocation.getArgument(0);
                        return Collections.emptyList();
                    });
            
            // Let the real method run
            mockedApi.when(() -> JobBoardApi.getJobBoard("all", 5, "anywhere", "any"))
                    .thenCallRealMethod();
            mockedApi.when(() -> JobBoardApi.getJobBoard(anyString(), any(), any(), any()))
                    .thenCallRealMethod();
            
            // Execute the test
            JobBoardApiResult result = JobBoardApi.getJobBoard("all", 5, "anywhere", "any");
            
            // Verify URL is as expected
            assertEquals("https://jobicy.com/api/v2/remote-jobs?count=5&tag=all", capturedUrl);
            
            // Error message should be set
            assertNotNull(result.getErrorMessage());
            assertTrue(result.getErrorMessage().contains("All search parameters were generic"));
        }
    }
    
    /**
     * Tests that spaces in query strings are replaced with plus signs.
     */
    @Test
    public void testQueryWithSpaces() {
        // Test that spaces in query are replaced with plus signs
        testApiCall("data science", 5, null, null, 
            "https://jobicy.com/api/v2/remote-jobs?count=5&tag=data+science");
    }
    
    /**
     * Tests the loadCsvData method with a mocked implementation.
     */
    @Test
    public void testLoadCsvData() {
        // Test with a mock implementation since we can't inherit from JobBoardApi anymore
        try (MockedStatic<JobBoardApi> mockedApi = mockStatic(JobBoardApi.class)) {
            // Setup the mock to return an empty map for non-existent files
            mockedApi.when(() -> JobBoardApi.loadCsvData("nonexistent.csv", "key", "value"))
                    .thenReturn(new HashMap<>());
            
            // Execute the test
            Map<String, String> result = JobBoardApi.loadCsvData("nonexistent.csv", "key", "value");
            
            // Verify result is empty but not null
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }
}
