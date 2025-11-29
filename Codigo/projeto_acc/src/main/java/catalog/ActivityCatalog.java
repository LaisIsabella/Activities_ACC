package catalog;

import model.Activity;
import repository.ActivityRepository;

import java.util.ArrayList;
import java.util.List;
import model.ActivityType;
import model.Status;
import model.Student;
import repository.ActivityTypeRepository;

public class ActivityCatalog {

    private List<Activity> activities;
    private final ActivityRepository repo;

    public ActivityCatalog(StudentCatalog sc, ActivityTypeRepository typeRepo) {
        this.repo = new ActivityRepository();
        this.activities = repo.loadAll(sc, typeRepo);
    }

    public ActivityType findActivityType(Activity activity) {
        if (activity == null) {
            return null;
        }
        return activity.getActivityType();
    }

    public boolean addActivity(Activity activity) {
        if (activity == null) {
            return false;
        }
        activities.add(activity);
        repo.saveAll(activities);         // persiste
        return true;
    }

    public List<Activity> findAllUserActivity(Student student) {
        List<Activity> result = new ArrayList<>();
        for (Activity a : activities) {
            if (a.getStudent() != null
                    && a.getStudent().getEmail().equalsIgnoreCase(student.getEmail())) {
                result.add(a);
            }
        }
        return result;
    }

    public boolean deleteActivity(Activity activity) {
        if (activity == null) {
            return false;
        }
        boolean removed = activities.remove(activity);
        if (removed) {
            repo.saveAll(activities);
        }
        return removed;
    }

    public List<Activity> listActivities() {
        return activities; // retorna todas
    }

    public boolean updateActivity(Activity activity, Status status, String response) {
        if (activity == null) {
            return false;
        }

        activity.setStatus(status);
        activity.setResponse(response);

        repo.saveAll(activities);
        return true;
    }

    public List<Activity> findForApproval() {//esse metodo recebe por parametro Activitie
        List<Activity> result = new ArrayList<>();

        for (Activity a : activities) {
            if (a.getStatus() == Status.PENDING) {
                result.add(a);
            }
        }
        return result;
    }

    public boolean verifyActivity(Activity activity, boolean isVerified) {
        if (activity == null) {
            return false;
        }
        activity.setIsVerified(isVerified);
        save();
        return true;
    }

    public void save() {
        repo.saveAll(activities);
    }

    public List<Activity> getAllStudentApprovedActivities(Student student) {
        List<Activity> result = new ArrayList<>();

        if (student == null) {
            return result;
        }

        for (Activity a : activities) {
            // Verifica se a atividade pertence ao estudante E está aprovada
            if (a.getStudent() != null
                    && a.getStudent().getEmail().equalsIgnoreCase(student.getEmail())
                    && a.getStatus() == Status.APPROVED) {
                result.add(a);
            }
        }

        return result;
    }

}
