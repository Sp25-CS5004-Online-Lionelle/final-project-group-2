package skillzhunter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import skillzhunter.controller.AlertObserver;
import skillzhunter.controller.MainController;
import skillzhunter.model.AlertListener;
import skillzhunter.model.IModel;

public class TestAlertObserver {
    
    private MainController controller;
    private IModel mockModel;
    
    @BeforeEach
    public void setUp() {
        mockModel = mock(IModel.class);
        controller = new MainController();
        controller.setModel(mockModel);
    }
    
    /**
     * Test registration and notification of a single observer.
     */
    @Test
    public void testSingleObserverNotification() {
        // Create a simple observer implementation that tracks if it was notified
        final AtomicBoolean wasNotified = new AtomicBoolean(false);
        final AtomicReference<String> receivedMessage = new AtomicReference<>("");
        
        AlertObserver observer = new AlertObserver() {
            @Override
            public void onAlert(String alertMessage) {
                wasNotified.set(true);
                receivedMessage.set(alertMessage);
            }
        };
        
        // Register the observer
        controller.registerAlertObserver(observer);
        
        // Send an alert
        String alertMessage = "Test alert message";
        controller.sendAlert(alertMessage);
        
        // Verify the observer was notified with the correct message
        assertTrue(wasNotified.get(), "Observer should be notified");
        assertEquals(alertMessage, receivedMessage.get(), "Observer should receive the correct message");
    }
    
    /**
     * Test registration and notification of multiple observers.
     */
    @Test
    public void testMultipleObserversNotification() {
        // Create counters to track notifications
        final AtomicInteger observer1Count = new AtomicInteger(0);
        final AtomicInteger observer2Count = new AtomicInteger(0);
        final AtomicInteger observer3Count = new AtomicInteger(0);
        
        // Create three different observers
        AlertObserver observer1 = alertMessage -> observer1Count.incrementAndGet();
        AlertObserver observer2 = alertMessage -> observer2Count.incrementAndGet();
        AlertObserver observer3 = alertMessage -> observer3Count.incrementAndGet();
        
        // Register all three observers
        controller.registerAlertObserver(observer1);
        controller.registerAlertObserver(observer2);
        controller.registerAlertObserver(observer3);
        
        // Send multiple alerts
        controller.sendAlert("First alert");
        controller.sendAlert("Second alert");
        
        // Verify all observers were notified the correct number of times
        assertEquals(2, observer1Count.get(), "Observer 1 should be notified twice");
        assertEquals(2, observer2Count.get(), "Observer 2 should be notified twice");
        assertEquals(2, observer3Count.get(), "Observer 3 should be notified twice");
    }
    
    /**
     * Test unregistering an observer.
     */
    @Test
    public void testUnregisterObserver() {
        // Create two mock observers
        AlertObserver mockObserver1 = mock(AlertObserver.class);
        AlertObserver mockObserver2 = mock(AlertObserver.class);
        
        // Register both observers
        controller.registerAlertObserver(mockObserver1);
        controller.registerAlertObserver(mockObserver2);
        
        // Send an initial alert to verify both receive it
        controller.sendAlert("Initial alert");
        verify(mockObserver1).onAlert("Initial alert");
        verify(mockObserver2).onAlert("Initial alert");
        
        // Unregister the first observer
        controller.unregisterAlertObserver(mockObserver1);
        
        // Send another alert
        controller.sendAlert("After unregister");
        
        // Verify only the second observer received the second alert
        verify(mockObserver1, never()).onAlert("After unregister");
        verify(mockObserver2).onAlert("After unregister");
    }
    
    /**
     * Test alert forwarding from model to observers.
     */
    @Test
    public void testAlertForwardingFromModel() {
        // Create a mock observer
        AlertObserver mockObserver = mock(AlertObserver.class);
        
        // Register the observer
        controller.registerAlertObserver(mockObserver);
        
        // Get the AlertListener that the controller registered with the model
        ArgumentCaptor<AlertListener> listenerCaptor = ArgumentCaptor.forClass(AlertListener.class);
        verify(mockModel).setAlertListener(listenerCaptor.capture());
        AlertListener listener = listenerCaptor.getValue();
        
        // Simulate the model sending an alert
        String modelAlertMessage = "Alert from model";
        listener.onAlert(modelAlertMessage);
        
        // Verify the observer received the forwarded alert
        verify(mockObserver).onAlert(modelAlertMessage);
    }
    
    /**
     * Test registering the same observer multiple times.
     */
    @Test
    public void testRegisteringSameObserverMultipleTimes() {
        // Create a mock observer
        AlertObserver mockObserver = mock(AlertObserver.class);
        
        // Register the same observer multiple times
        controller.registerAlertObserver(mockObserver);
        controller.registerAlertObserver(mockObserver);
        controller.registerAlertObserver(mockObserver);
        
        // Send an alert
        controller.sendAlert("Test alert");
        
        // Verify the observer was only notified once (no duplicates)
        verify(mockObserver, times(1)).onAlert("Test alert");
    }
    
    /**
     * Test unregistering an observer that was never registered.
     */
    @Test
    public void testUnregisteringNonRegisteredObserver() {
        // Create two observers
        AlertObserver mockObserver1 = mock(AlertObserver.class);
        AlertObserver mockObserver2 = mock(AlertObserver.class);
        
        // Only register the first one
        controller.registerAlertObserver(mockObserver1);
        
        // Try to unregister the second one (should not throw exception)
        controller.unregisterAlertObserver(mockObserver2);
        
        // Send an alert
        controller.sendAlert("Test alert");
        
        // Verify the first observer still receives alerts
        verify(mockObserver1).onAlert("Test alert");
        verify(mockObserver2, never()).onAlert(anyString());
    }
    
    /**
     * Test sending an alert to a lambda observer.
     */
    @Test
    public void testLambdaObserver() {
        // Create a holder for the received message
        final AtomicReference<String> receivedMessage = new AtomicReference<>("");
        
        // Register a lambda observer
        controller.registerAlertObserver(alertMessage -> receivedMessage.set(alertMessage));
        
        // Send an alert
        String alertMessage = "Test lambda observer";
        controller.sendAlert(alertMessage);
        
        // Verify the message was received
        assertEquals(alertMessage, receivedMessage.get(), "Lambda observer should receive the correct message");
    }
    
    /**
     * Test sending an alert with no registered observers.
     */
    @Test
    public void testNoRegisteredObservers() {
        // Don't register any observers
        
        // Sending an alert should not throw an exception
        try {
            controller.sendAlert("Test with no observers");
            assertTrue(true, "No exception should be thrown");
        } catch (Exception e) {
            fail("Exception should not be thrown when sending alert with no observers");
        }
    }
    
    /**
 * Test observer handling behavior when an exception occurs.
 */
@Test
public void testObserverWithException() {
    // Create an observer that throws an exception
    AlertObserver badObserver = alertMessage -> {
        throw new RuntimeException("Test exception from observer");
    };
    
    // Register the observer
    controller.registerAlertObserver(badObserver);
    
    // Sending an alert should cause the exception to propagate
    Exception exception = assertThrows(RuntimeException.class, () -> {
        controller.sendAlert("Test exception handling");
    });
    
    // Verify the exception message
    assertEquals("Test exception from observer", exception.getMessage());
}
    
    /**
     * Test unregistering all observers.
     */
    @Test
    public void testUnregisterAllObservers() {
        // Create several mock observers
        AlertObserver observer1 = mock(AlertObserver.class);
        AlertObserver observer2 = mock(AlertObserver.class);
        AlertObserver observer3 = mock(AlertObserver.class);
        
        // Register all observers
        controller.registerAlertObserver(observer1);
        controller.registerAlertObserver(observer2);
        controller.registerAlertObserver(observer3);
        
        // Unregister them all
        controller.unregisterAlertObserver(observer1);
        controller.unregisterAlertObserver(observer2);
        controller.unregisterAlertObserver(observer3);
        
        // Send an alert
        controller.sendAlert("After unregistering all");
        
        // Verify no observers received the alert
        verify(observer1, never()).onAlert(anyString());
        verify(observer2, never()).onAlert(anyString());
        verify(observer3, never()).onAlert(anyString());
    }
    
    /**
     * Test custom implementation of AlertObserver interface.
     */
    @Test
    public void testCustomAlertObserverImplementation() {
        // Create a custom implementation of AlertObserver
        class CustomAlertObserver implements AlertObserver {
            private final AtomicReference<String> lastMessage = new AtomicReference<>("");
            private final AtomicInteger alertCount = new AtomicInteger(0);
            
            @Override
            public void onAlert(String alertMessage) {
                lastMessage.set(alertMessage);
                alertCount.incrementAndGet();
            }
            
            public String getLastMessage() {
                return lastMessage.get();
            }
            
            public int getAlertCount() {
                return alertCount.get();
            }
        }
        
        // Create an instance of our custom observer
        CustomAlertObserver customObserver = new CustomAlertObserver();
        
        // Register it
        controller.registerAlertObserver(customObserver);
        
        // Send multiple alerts
        controller.sendAlert("First custom alert");
        controller.sendAlert("Second custom alert");
        controller.sendAlert("Third custom alert");
        
        // Verify the observer tracked the alerts correctly
        assertEquals("Third custom alert", customObserver.getLastMessage(), "Last message should be correct");
        assertEquals(3, customObserver.getAlertCount(), "Alert count should be correct");
    }
}