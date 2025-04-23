package skillzhunter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import skillzhunter.model.AlertListener;
import skillzhunter.model.Jobs;

/**
 * Tests for the AlertListener mechanism in the Jobs model.
 * This focuses on the direct alert functionality rather than 
 * attempting to test complex interactions with JobBoardApi.
 */
public class TestAlertListener {

    private Jobs jobs;
    private AlertListener mockAlertListener;
    
    @BeforeEach
    public void setUp() {
        // Create a real Jobs instance and a mock AlertListener
        mockAlertListener = mock(AlertListener.class);
        jobs = new Jobs();
        jobs.setAlertListener(mockAlertListener);
    }
    
    /**
     * Basic test that the alert mechanism works when sending a direct alert.
     */
    @Test
    public void testSendAlertDirectly() {
        String testMessage = "Test alert message";
        jobs.sendAlert(testMessage);
        verify(mockAlertListener).onAlert(testMessage);
    }
    
    /**
     * Test that multiple alerts are properly forwarded to the listener.
     */
    @Test
    public void testMultipleAlerts() {
        jobs.sendAlert("First alert");
        jobs.sendAlert("Second alert");
        jobs.sendAlert("Third alert");
        
        verify(mockAlertListener).onAlert("First alert");
        verify(mockAlertListener).onAlert("Second alert");
        verify(mockAlertListener).onAlert("Third alert");
        verify(mockAlertListener, times(3)).onAlert(anyString());
    }
    
    /**
     * Test that the Jobs class can handle a null AlertListener without exceptions.
     */
    @Test
    public void testAlertWithNullListener() {
        jobs.setAlertListener(null);
        
        try {
            jobs.sendAlert("Test message with null listener");
            assertTrue(true); // If we get here, no exception was thrown
        } catch (Exception e) {
            fail("Exception should not be thrown when alert listener is null");
        }
    }
    
    /**
     * Test handling of alerts for file operation errors.
     */
    @Test
    public void testAlertForFileOperationError() {
        String errorMessage = "Failed to save jobs: Permission denied";
        jobs.sendAlert(errorMessage);
        verify(mockAlertListener).onAlert(errorMessage);
    }
    
    /**
     * Test handling of alerts for malformed data.
     */
    @Test
    public void testAlertForMalformedData() {
        String warningMessage = "Warning: Invalid job data detected";
        jobs.sendAlert(warningMessage);
        verify(mockAlertListener).onAlert(warningMessage);
    }
    
    /**
     * Test handling of alerts for network errors.
     */
    @Test
    public void testAlertForNetworkError() {
        String networkErrorMessage = "Network error: Unable to connect to job board API";
        jobs.sendAlert(networkErrorMessage);
        verify(mockAlertListener).onAlert(networkErrorMessage);
    }
    
    /**
     * Test that the AlertListener is properly registered and used.
     */
    @Test
    public void testAlertListenerRegistration() {
        // Create a new Jobs instance and verify it starts with no listener
        Jobs newJobs = new Jobs();
        
        // Create a test message
        String testMessage = "Alert listener registration test";
        
        // First verify no exception when no listener is registered
        try {
            newJobs.sendAlert(testMessage);
            assertTrue(true); // No exception thrown
        } catch (Exception e) {
            fail("Should not throw exception with no listener");
        }
        
        // Now register our mock listener
        newJobs.setAlertListener(mockAlertListener);
        
        // Send alert and verify it was received
        newJobs.sendAlert(testMessage);
        verify(mockAlertListener).onAlert(testMessage);
    }
}