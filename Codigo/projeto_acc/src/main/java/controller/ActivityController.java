package controller;

import catalog.ActivityCatalog;
import model.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class ActivityController {

    private final ActivityCatalog activityCatalog;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public ActivityController(ActivityCatalog catalog) {
        this.activityCatalog = catalog;
    }

    // CE11 – criar atividade
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
                    false // toda atividade inicia NÃO verificada
            );

            return activityCatalog.addActivity(activity);

        } catch (Exception e) {
            return false;
        }
    }

    public boolean validateActivity(String name, String description, Date date, int hours,
            Status status, ActivityType activityType,
            Student student, Document document) {

        return validateActivityName(name)
                && description != null && !description.isBlank()
                && date != null
                && hours > 0
                && validateStatus(status)
                && validateActivityType(activityType)
                && validateStudent(student)
                && document != null;
    }

    public boolean validateStatus(Status status) {
        return status != null;
    }

    public boolean validateActivityName(String name) {
        return name != null && !name.isBlank();
    }

    public boolean validateActivityType(ActivityType type) {
        return type != null;
    }

    public boolean validateStudent(Student student) {
        return student != null;
    }

    public List<Activity> searchFilteredActivities(Student student, Status status, ActivityType type, boolean isVerified) {
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
            if (isVerified && !a.getIsVerified()) {
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

        activity.setStatus(Status.DENIED); // O enum pode continuar existindo, apenas não usar nesse fluxo
        activity.setResponse(response);
        activityCatalog.save(); // Salva alterações

        // Registra mensagem na caixa do aluno (adicionado no Student)
        activity.getStudent().addMessage("Atividade \"" + activity.getName() + "\" foi negada.\nMotivo: " + response);

        return true;
    }

    // CE12 – excluir atividade (somente pendente)
    public boolean deleteActivity(Activity activity) {
        if (activity.getStatus() != Status.PENDING) {
            return false;
        }
        return activityCatalog.deleteActivity(activity);
    }

    // ============================
    // CE05 – NEGAR ATIVIDADE
    // ============================
    public boolean denyActivity(Activity activity, String response) {

        if (activity.getStatus() != Status.PENDING) {
            throw new IllegalArgumentException("Atividade já foi avaliada.");
        }

        if (response == null || response.isBlank()) {
            throw new IllegalArgumentException("Justificativa é obrigatória.");
        }

        return activityCatalog.updateActivity(activity, Status.DENIED, response);
    }

    // ============================
    // CE06 – NEGAR PARCIALMENTE
    // ============================
    public boolean partiallyDenyActivity(Activity activity, String response) {

        if (activity.getStatus() != Status.PENDING) {
            throw new IllegalArgumentException("Atividade já foi avaliada.");
        }

        if (response == null || response.isBlank()) {
            throw new IllegalArgumentException("Descreva os ajustes necessários.");
        }

        return activityCatalog.updateActivity(activity, Status.PARTIALLY_DENIED, response);
    }

    // ============================
    // CE07 – APROVAR ATIVIDADE
    // ============================
    public boolean approveActivity(Activity activity, int approvedHours) {

        if (activity.getStatus() != Status.PENDING) {
            throw new IllegalArgumentException("Atividade já foi avaliada.");
        }

        // aplica limite automaticamente
        int limit = activity.getActivityType().getLimit();
        if (approvedHours > limit) {
            approvedHours = limit;
        }

        activity.setHours(approvedHours);

        return activityCatalog.updateActivity(activity, Status.APPROVED, "Horas aprovadas: " + approvedHours);
    }

    public boolean verifyActivity(Activity activity) {
        if (activity == null) {
            return false;
        }
        return activityCatalog.verifyActivity(activity, true); // Marca como verificada
    }

    // listar apenas as do aluno logado
    public List<Activity> getByStudent(Student student) {
        List<Activity> result = new ArrayList<>();
        if (student == null) {
            return result;
        }

        for (Activity a : activityCatalog.listActivities()) {
            if (a.getStudent() != null
                    && a.getStudent().getEmail().equalsIgnoreCase(student.getEmail())) {
                result.add(a);
            }
        }
        return result;
    }

}
