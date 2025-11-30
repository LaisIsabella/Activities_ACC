package controller;

import catalog.ActivityCatalog;
import model.*;

import java.text.SimpleDateFormat;
import java.util.*;
import util.EmailUtil;
import util.PDFUtil;
import util.ValidatorUtil;

public class ActivityController {

    private final ActivityCatalog activityCatalog;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private StudentController studentController;

    public ActivityController(ActivityCatalog catalog) {
        this.activityCatalog = catalog;
    }

    public void setStudentController(StudentController studentController) {
        this.studentController = studentController;
    }

    public boolean createActivity(
            String name,
            String description,
            Date date,
            int hours,
            Status status,
            ActivityType activityType,
            Student student,
            Document document
    ) {
        try {
            boolean validData = ValidatorUtil.validateActivity(
                    name, description, date, hours, status, activityType, student, document
            );

            if (!validData) {
                return false;
            }

            Activity activity = new Activity(
                    name,
                    description,
                    date,
                    hours,
                    status,
                    "",
                    activityType,
                    student,
                    document,
                    false
            );

            return activityCatalog.addActivity(activity);

        } catch (Exception e) {
            return false;
        }
    }

    public List<Activity> searchFilteredActivities(Student student, Status status, ActivityType type, Boolean isVerified) {
        List<Activity> result = new ArrayList<>();

        for (Activity a : activityCatalog.listActivities()) {
            if (student != null && a.getStudent() != null && !a.getStudent().equals(student)) {
                continue;
            }
            if (status != null && a.getStatus() != status) {
                continue;
            }
            if (type != null && !a.getActivityType().getName().equalsIgnoreCase(type.getName())) {
                continue;
            }
            if (isVerified != null && a.getIsVerified() != isVerified) {
                continue;
            }
            result.add(a);
        }

        return result;
    }

    public boolean denyActivitySupervisor(Activity activity, String response) {
        if (activity == null || response == null || response.isBlank()) {
            return false;
        }

        boolean updated = activityCatalog.updateActivity(activity, Status.DENIED, response);

        if (!updated) {
            return false;
        }

        if (activity.getStudent() != null) {
            EmailUtil.sendActivityDeniedBySupervisorEmail(
                    activity.getStudent().getEmail(),
                    activity.getStudent().getName(),
                    activity.getName(),
                    response
            );
        }

        return true;
    }

    public boolean deleteActivity(Activity activity) {
        if (activity == null) {
            return false;
        }

        if (!canDeleteActivity(activity)) {
            if (activity.getStatus() != Status.PENDING) {
                throw new IllegalArgumentException(
                        "Não é possível excluir esta atividade.\n"
                        + "Status atual: " + getStatusText(activity.getStatus()) + "\n\n"
                        + "Apenas atividades PENDENTES podem ser excluídas."
                );
            }

            if (activity.getIsVerified()) {
                throw new IllegalArgumentException(
                        "Não é possível excluir esta atividade.\n\n"
                        + "A atividade já foi validada pelo supervisor e está em processo de avaliação."
                );
            }
        }

        return activityCatalog.deleteActivity(activity);
    }

    public boolean canDeleteActivity(Activity activity) {
        if (activity == null) {
            return false;
        }
        // Só pode excluir se estiver PENDING e NÃO verificada
        return activity.getStatus() == Status.PENDING && !activity.getIsVerified();
    }

    private String getStatusText(Status status) {
        return switch (status) {
            case PENDING ->
                "PENDENTE";
            case APPROVED ->
                "APROVADA";
            case DENIED ->
                "NEGADA";
            case PARTIALLY_DENIED ->
                "AJUSTES NECESSÁRIOS";
        };
    }

    public boolean denyActivity(Activity activity, String response) {

        if (activity.getStatus() != Status.PENDING) {
            throw new IllegalArgumentException("Atividade já foi avaliada.");
        }

        if (response == null || response.isBlank()) {
            throw new IllegalArgumentException("Justificativa é obrigatória.");
        }

        boolean updated = activityCatalog.updateActivity(activity, Status.DENIED, response);

        if (updated && activity.getStudent() != null) {
            EmailUtil.sendActivityDeniedEmail(
                    activity.getStudent().getEmail(),
                    activity.getStudent().getName(),
                    activity.getName(),
                    response
            );
        }

        return updated;
    }

    public boolean partiallyDenyActivity(Activity activity, String response) {

        if (activity.getStatus() != Status.PENDING) {
            throw new IllegalArgumentException("Atividade já foi avaliada.");
        }

        if (response == null || response.isBlank()) {
            throw new IllegalArgumentException("Descreva os ajustes necessários.");
        }

        boolean updated = activityCatalog.updateActivity(activity, Status.PARTIALLY_DENIED, response);

        if (updated && activity.getStudent() != null) {
            EmailUtil.sendActivityPartiallyDeniedEmail(
                    activity.getStudent().getEmail(),
                    activity.getStudent().getName(),
                    activity.getName(),
                    response
            );
        }

        return updated;
    }

    public boolean approveActivity(Activity activity, int approvedHours) {

        if (activity.getStatus() != Status.PENDING) {
            throw new IllegalArgumentException("Atividade já foi avaliada.");
        }

        int limit = activity.getActivityType().getLimit();
        if (approvedHours > limit) {
            approvedHours = limit;
        }

        List<Activity> approvedActivitiesOfSameType = new ArrayList<>();

        for (Activity a : activityCatalog.getAllStudentApprovedActivities(activity.getStudent())) {
            if (a.getActivityType().getName().equalsIgnoreCase(activity.getActivityType().getName())) {
                approvedActivitiesOfSameType.add(a);
            }
        }

        int totalHoursInCategory = 0;
        for (Activity a : approvedActivitiesOfSameType) {
            totalHoursInCategory += a.getHours();
        }

        int hoursToAdd = approvedHours;

        if (totalHoursInCategory + approvedHours > limit) {
            hoursToAdd = limit - totalHoursInCategory;

            if (hoursToAdd <= 0) {
                throw new IllegalArgumentException(
                        "O aluno já atingiu o limite de " + limit
                        + " horas para atividades do tipo \"" + activity.getActivityType().getName() + "\"."
                );
            }

            approvedHours = hoursToAdd;
        }

        activity.setHours(approvedHours);

        boolean updated = activityCatalog.updateActivity(
                activity,
                Status.APPROVED,
                "Horas aprovadas: " + approvedHours
        );

        if (!updated) {
            return false;
        }

        if (studentController != null) {
            boolean hoursAdded = studentController.addStudentHours(activity.getStudent(), approvedHours);

            if (!hoursAdded) {
                activityCatalog.updateActivity(activity, Status.PENDING, "");
                throw new IllegalArgumentException("Erro ao adicionar horas ao estudante.");
            }
        }

        if (activity.getStudent() != null) {
            EmailUtil.sendActivityApprovedEmail(
                    activity.getStudent().getEmail(),
                    activity.getStudent().getName(),
                    activity.getName(),
                    approvedHours
            );
        }

        return true;
    }

    public boolean verifyActivity(Activity activity) {
        if (activity == null) {
            return false;
        }
        return activityCatalog.verifyActivity(activity, true);
    }

    public List<Activity> getByStudent(Student student) {
        List<Activity> result = new ArrayList<>();
        if (student == null) {
            return result;
        }

        result = activityCatalog.findAllUserActivity(student);
        return result;
    }

    public boolean generateUserReport(Student student, String filePath) {
        try {
            if (student == null || filePath == null || filePath.isBlank()) {
                return false;
            }

            List<Activity> approvedActivities
                    = activityCatalog.getAllStudentApprovedActivities(student);

            if (approvedActivities == null || approvedActivities.isEmpty()) {
                return false;
            }

            return PDFUtil.generateUserReport(filePath, approvedActivities);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Activity> getAllStudentApprovedActivities(Student student) {
        return activityCatalog.getAllStudentApprovedActivities(student);
    }

}
