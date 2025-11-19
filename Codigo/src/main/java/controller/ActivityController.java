package controller;

import catalog.ActivityCatalog;

public class ActivityController {
    private ActivityCatalog activityCatalog;

    public ActivityController() {
    }

    public ActivityController(ActivityCatalog activityCatalog) {
        this.activityCatalog = activityCatalog;
    }

    public ActivityCatalog getActivityCatalog() {
        return activityCatalog;
    }

    public void setActivityCatalog(ActivityCatalog activityCatalog) {
        this.activityCatalog = activityCatalog;
    }
}
