package skillzhunter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import skillzhunter.model.QuerySuggestionService;

/**
 * Test class for the QuerySuggestionService.
 * Tests query suggestions, Levenshtein distance calculation, and special cases.
 */
public class TestQuerySuggestionService {
    private QuerySuggestionService service;

    @BeforeEach
    public void setUp() {
        service = new QuerySuggestionService();
        
        // Add some common terms to work with
        service.addSuccessfulQuery("python");
        service.addSuccessfulQuery("javascript");
        service.addSuccessfulQuery("developer");
        service.addSuccessfulQuery("engineer");
    }

    /**
     * Test Levenshtein distance calculation in QuerySuggestionService.
     */
    @Test
    public void testLevenshteinDistance() throws Exception {
        // Use reflection to access private method
        Method levenshteinMethod = QuerySuggestionService.class.getDeclaredMethod(
            "calculateLevenshteinDistance", String.class, String.class);
        levenshteinMethod.setAccessible(true);
        
        // Test different edit distances
        assertEquals(0, (int)levenshteinMethod.invoke(service, "python", "python"));
        assertEquals(1, (int)levenshteinMethod.invoke(service, "python", "pythn"));  // deletion
        assertEquals(1, (int)levenshteinMethod.invoke(service, "python", "pythoon")); // insertion
        assertEquals(1, (int)levenshteinMethod.invoke(service, "python", "pytjon"));  // substitution
        assertEquals(2, (int)levenshteinMethod.invoke(service, "python", "pithn"));  // two substitutions
    }

    /**
     * Test the special case handling in QuerySuggestionService.
     */
    @Test
    public void testQuerySuggestionSpecialCases() {
        // Test missing first letter
        assertEquals("python", service.suggestCorrection("ython", 0));
        
        // Test missing last letter
        assertEquals("python", service.suggestCorrection("pytho", 0));
        
        // Test wrong first letter
        assertEquals("python", service.suggestCorrection("rython", 0));
        
        // Test multiple missing letters but still within edit distance
        assertEquals("javascript", service.suggestCorrection("javscript", 0));
        
        // Test no suggestion for completely different word
        assertNull(service.suggestCorrection("automobile", 0));
    }
    
    /**
     * Tests that no suggestion is returned when there are search results.
     */
    @Test
    public void testNoSuggestionWhenHasResults() {
        assertNull(service.suggestCorrection("pythn", 5), 
                  "No suggestion should be returned when search has results");
    }

    /**
     * Tests that no suggestion is provided for queries that are already in the common terms.
     */
    @Test
    public void testNoSuggestionForCommonTerms() {
        // This test requires reflection to access the private commonTerms field
        // Alternatively, we can test this indirectly
        assertNull(service.suggestCorrection("python", 0), 
                  "No suggestion should be returned for queries that are already common terms");
        assertNull(service.suggestCorrection("java", 0), 
                  "No suggestion should be returned for queries that are already common terms");
    }

    /**
     * Test that suggestions come from common terms first, then from recent queries.
     */
    @Test
    public void testSuggestionPriority() throws Exception {
        // We need to test common terms vs. recent queries
        QuerySuggestionService testService = new QuerySuggestionService();
        
        // Add a successful query that isn't already in common terms
        // First, we need to get the commonTerms list through reflection
        Field commonTermsField = QuerySuggestionService.class.getDeclaredField("commonTerms");
        commonTermsField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> commonTerms = (List<String>)commonTermsField.get(testService);
        
        // Find a term not in common terms
        String uniqueTerm = "uniqueterm";
        while (commonTerms.contains(uniqueTerm)) {
            uniqueTerm += "x";
        }
        
        // Add a misspelled version to recent queries
        testService.addSuccessfulQuery(uniqueTerm);
        
        // Now test a typo - it should suggest from recent queries since it's not in common terms
        String typo = uniqueTerm.substring(0, uniqueTerm.length() - 1); // Remove last char
        assertEquals(uniqueTerm, testService.suggestCorrection(typo, 0),
                    "Should suggest from recent queries for terms not in common terms");
        
        // Now add a common term with a similar typo and test again
        // The suggestion should now come from common terms instead
        String commonTerm = "developer";
        assertTrue(commonTerms.contains(commonTerm), "The term 'developer' should be in common terms");
        
        // Test a typo that could match both
        String commonTypo = "develope"; // Missing last char
        assertEquals(commonTerm, testService.suggestCorrection(commonTypo, 0),
                    "Should prioritize suggestion from common terms over recent queries");
    }

    /**
     * Test that the service can handle empty strings.
     */
    @Test
    public void testEmptyString() {
        // Test empty string input
        assertNull(service.suggestCorrection("", 0), 
                  "Empty string should return null");
    }
}