package catalog;

import model.Activity;
import repository.ActivityRepository;

import java.util.ArrayList;
import java.util.List;

public class ActivityCatalog {

    private List<Activity> activities;
    private final ActivityRepository repo;

    public ActivityCatalog() {
        this.repo = new ActivityRepository();
        this.activities = repo.loadAll();  // carrega do TXT
    }

    public boolean addActivity(Activity activity) {
        if (activity == null) return false;
        activities.add(activity);
        repo.saveAll(activities);         // persiste
        return true;
    }

    public boolean deleteActivity(Activity activity) {
        if (activity == null) return false;
        boolean removed = activities.remove(activity);
        if (removed) {
            repo.saveAll(activities);
        }
        return removed;
    }

    public List<Activity> getAll() {
        return new ArrayList<>(activities);
    }
}
