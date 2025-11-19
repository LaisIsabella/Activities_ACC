package catalog;

import java.util.List;
import model.Activity;

public class ActivityCatalog {
    private List<Activity> activities;

    public ActivityCatalog() {
    }

    public ActivityCatalog(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}
