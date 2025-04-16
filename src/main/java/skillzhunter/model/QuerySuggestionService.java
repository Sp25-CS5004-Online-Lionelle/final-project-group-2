package skillzhunter.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for providing query suggestions based on similarity.
 */
public class QuerySuggestionService {
    
    /**
     * Maximum edit distance to consider a term similar.
     * Lower values are more strict (fewer suggestions but more accurate).
     */
    private static final int MAX_EDIT_DISTANCE = 2;
    
    /**
     * Minimum length for a search term to be considered for suggestions.
     * Prevents suggestions for very short queries.
     */
    private static final int MIN_QUERY_LENGTH = 3;
    
    /**
     * Recent successful queries to use as suggestions.
     */
    private final List<String> recentQueries = new ArrayList<>();
    
    /**
     * Common job search terms that can be used for suggestions.
     */
    private final List<String> commonTerms = List.of(
        "developer", "engineer", "manager", "assistant", "analyst",
        "designer", "marketing", "sales", "finance", "accounting",
        "python", "java", "javascript", "react", "node"
    );
    
    /**
     * Adds a successful query to the recent queries list.
     * 
     * @param query The query to add
     */
    public void addSuccessfulQuery(String query) {
        if (query != null && !query.isEmpty() && !recentQueries.contains(query)) {
            // Keep the list from growing too large
            if (recentQueries.size() >= 50) {
                recentQueries.remove(0);
            }
            recentQueries.add(query);
        }
    }
    
    /**
     * Checks if the query might be a typo and suggests alternatives.
     * 
     * @param query The query to check
     * @param results The number of results found for this query
     * @return A suggested correction, or null if no suggestion
     */
    public String suggestCorrection(String query, int results) {
        // Only suggest for queries with few results
        if (results > 0 || query == null || query.length() < MIN_QUERY_LENGTH) {
            return null;
        }
        
        // Combine recent queries with common terms for the suggestion pool
        List<String> suggestionPool = new ArrayList<>(recentQueries);
        suggestionPool.addAll(commonTerms);
        
        String bestSuggestion = null;
        int minDistance = Integer.MAX_VALUE;
        
        // Find the closest term
        for (String term : suggestionPool) {
            int distance = calculateLevenshteinDistance(query.toLowerCase(), term.toLowerCase());
            
            if (distance <= MAX_EDIT_DISTANCE && distance < minDistance) {
                minDistance = distance;
                bestSuggestion = term;
            }
        }
        
        return bestSuggestion;
    }
    
    /**
     * Calculates the Levenshtein distance between two strings.
     * 
     * @param s1 First string
     * @param s2 Second string
     * @return The edit distance between s1 and s2
     */
    private int calculateLevenshteinDistance(String s1, String s2) {
        int[] costs = new int[s2.length() + 1];
        
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else if (j > 0) {
                    int newValue = costs[j - 1];
                    if (s1.charAt(i - 1) != s2.charAt(j - 1)) {
                        newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                    }
                    costs[j - 1] = lastValue;
                    lastValue = newValue;
                }
            }
            if (i > 0) {
                costs[s2.length()] = lastValue;
            }
        }
        return costs[s2.length()];
    }
}