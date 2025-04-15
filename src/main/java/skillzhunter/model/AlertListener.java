package skillzhunter.model;

    /**
     * Interface for an alert listener that can receive alerts from the model.
     * Used as composition for the model to notify listeners about alerts.
     */
    public interface AlertListener {
        /**
         * Called when the model needs to send an alert.
         * @param alertMessage The alert message
         */
        void onAlert(String alertMessage);
    }