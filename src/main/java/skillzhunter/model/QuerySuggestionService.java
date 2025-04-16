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
    private static final int MIN_QUERY_LENGTH = 2;
    
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
     * Checks for:
     * - Missing letters (jav -> java)
     * - Extra letters (javaa -> java)
     * - Swapped letters (jvaa -> java)
     * - Wrong first/last letter (Yava -> java, javz -> java)
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
        String queryLower = query.toLowerCase();
        if (commonTerms.contains(queryLower)) {
            return null;
        }
        
        // Don't suggest if the query is already a previously successful query
        if (recentQueries.contains(queryLower)) {
            return null;
        }
        
        // First try matching with common terms (higher priority)
        String commonTermSuggestion = findBestMatch(queryLower, commonTerms);
        
        // If found a good match in common terms, return it
        if (commonTermSuggestion != null) {
            return commonTermSuggestion;
        }
        
        // If no match in common terms, try with recent queries
        return findBestMatch(queryLower, recentQueries);
    }
    
    /**
     * Finds the best matching term from a list of words.
     * 
     * @param query The query to find matches for
     * @param words The list of words to search
     * @return The best matching term, or null if no good match
     */
    private String findBestMatch(String query, List<String> words) {
        String bestSuggestion = null;
        int minDistance = MAX_EDIT_DISTANCE + 1; // Start higher than our max threshold
        
        // Check each candidate
        for (String word : words) {
            String wordLower = word.toLowerCase();
            
            // Skip exact matches
            if (query.equals(wordLower)) {
                continue;
            }
            
            // Special case: missing first letter ("ython" -> "python")
            if (wordLower.length() > query.length() && 
                wordLower.substring(1).equals(query)) {
                return word;
            }
            
            // Special case: missing last letter ("pytho" -> "python")
            if (wordLower.length() > query.length() && 
                wordLower.startsWith(query)) {
                return word;
            }
            
            // Special case: wrong first letter ("qython" -> "python")
            if (wordLower.length() == query.length() && 
                wordLower.substring(1).equals(query.substring(1))) {
                return word;
            }
            
            // Standard Levenshtein distance for missing middle letters ("pthn" -> "python")
            int distance = calculateLevenshteinDistance(query, wordLower);
            
            if (distance <= MAX_EDIT_DISTANCE && distance < minDistance) {
                minDistance = distance;
                bestSuggestion = word;
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
