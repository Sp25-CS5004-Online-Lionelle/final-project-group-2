package skillzhunter.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for providing query suggestions based on similarity.
 * Uses Levenshtein distance to find similar terms when a search returns no results.
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
        "python", "java", "javascript", "react", "node", "angular",
        "remote", "hybrid", "full-time", "part-time", "contract",
        "senior", "junior", "lead", "director", "intern",
        "data", "cloud", "devops", "security", "network", "support",
        "product", "project", "program", "business", "operations",
        "hr", "human resources", "customer", "service", "quality",
        "research", "science", "web", "mobile", "frontend", "backend"
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
     * ONLY suggests corrections when:
     * 1. The search returned NO results (resultCount == 0)
     * 2. The query isn't too short
     * 3. The query isn't exactly a common term or previous successful query
     * 
     * @param query The query to check
     * @param resultCount The number of results found for this query
     * @return A suggested correction, or null if no suggestion
     */
    public String suggestCorrection(String query, int resultCount) {
        // Only suggest when NO results are found
        if (resultCount > 0 || query == null || query.length() < MIN_QUERY_LENGTH) {
            return null;
        }
        
        // Don't suggest if the query is already a common term
        if (commonTerms.contains(query.toLowerCase())) {
            return null;
        }
        
        // Don't suggest if the query is already a previously successful query
        if (recentQueries.contains(query.toLowerCase())) {
            return null;
        }
        
        // First try matching with common terms (higher priority)
        String commonTermSuggestion = findBestMatch(query, commonTerms);
        
        // If found a good match in common terms, return it
        if (commonTermSuggestion != null) {
            return commonTermSuggestion;
        }
        
        // If no match in common terms, try with recent queries
        return findBestMatch(query, recentQueries);
    }
    
    /**
     * Finds the best matching term from a list of candidates.
     * 
     * @param query The query to find matches for
     * @param candidates The list of candidate terms to search
     * @return The best matching term, or null if no good match
     */
    private String findBestMatch(String query, List<String> candidates) {
        String bestSuggestion = null;
        int minDistance = MAX_EDIT_DISTANCE + 1; // Start higher than our max threshold
        
        String queryLower = query.toLowerCase();
        
        // Find the closest term
        for (String term : candidates) {
            // Skip exact matches
            String termLower = term.toLowerCase();
            if (queryLower.equals(termLower)) {
                continue;
            }
            
            int distance = calculateLevenshteinDistance(queryLower, termLower);
            
            if (distance <= MAX_EDIT_DISTANCE && distance < minDistance) {
                minDistance = distance;
                bestSuggestion = term;
            }
        }
        
        return bestSuggestion;
    }
    
    /**
     * Calculates the Levenshtein distance between two strings.
     * This measures the minimum number of single-character edits
     * (insertions, deletions, or substitutions) required to change
     * one string into the other.
     * 
     * @param s1 First string
     * @param s2 Second string
     * @return The edit distance between s1 and s2
     */
    private int calculateLevenshteinDistance(String s1, String s2) {
        // Create two work vectors of integer distances
        int[] costs = new int[s2.length() + 1];
        
        // Initialize the previous row of distances
        for (int i = 0; i <= s2.length(); i++) {
            costs[i] = i;
        }
        
        for (int i = 1; i <= s1.length(); i++) {
            // Calculate current row distances from the previous row
            costs[0] = i;
            
            // Previous cost of current row
            int nw = i - 1;
            
            for (int j = 1; j <= s2.length(); j++) {
                // Calculate current distance
                int cj = Math.min(
                    Math.min(costs[j] + 1, costs[j - 1] + 1),
                    nw + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1)
                );
                
                // Save previous row value and update in place
                nw = costs[j];
                costs[j] = cj;
            }
        }
        
        // Return the final distance
        return costs[s2.length()];
    }
}
