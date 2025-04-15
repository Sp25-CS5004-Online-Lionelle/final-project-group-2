package skillzhunter.controller;

    /**
     * Interface for an alert observer that receives notifications from the controller.
     * Used as composition in the controller to notify observers about alerts.
     */
    public interface AlertObserver {
        /**
         * Called when an alert needs to be shown.
         * @param alertMessage The alert message to show
         */
        void onAlert(String alertMessage);
    }
