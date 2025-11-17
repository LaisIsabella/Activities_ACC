package controller;

import catalog.ActivityCatalog;
import model.*;

import java.io.File;
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
            Student student,
            String name,
            String description,
            String dateStr,
            int hours,
            ActivityType type,
            File attachment
    ) {

        try {
            if (student == null)
                throw new IllegalArgumentException("Aluno inválido.");

            if (name == null || name.isBlank())
                throw new IllegalArgumentException("Nome obrigatório.");

            if (description == null || description.isBlank())
                throw new IllegalArgumentException("Descrição obrigatória.");

            if (dateStr == null || !dateStr.matches("\\d{2}/\\d{2}/\\d{4}"))
                throw new IllegalArgumentException("Data inválida. Use DD/MM/AAAA.");

            if (hours < 0)
                throw new IllegalArgumentException("Horas inválidas.");

            if (type == null)
                throw new IllegalArgumentException("Tipo de atividade obrigatório.");

            // regra do limite
            if (hours > type.getLimit())
                throw new IllegalArgumentException(
                        "O tipo \"" + type.getName() + "\" permite no máximo "
                                + type.getLimit() + " horas. Valor informado: " + hours
                );

            if (attachment == null)
                throw new IllegalArgumentException("O comprovante é obrigatório.");

            if (!attachment.getName().toLowerCase().endsWith(".pdf"))
                throw new IllegalArgumentException("O comprovante deve ser um arquivo PDF.");

            Date date = sdf.parse(dateStr);

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date today = cal.getTime();

            if (date.before(today))
                throw new IllegalArgumentException("A data não pode ser passada.");

            // salvar anexo em pasta local
            File destFolder = new File("attachments");
            destFolder.mkdirs();

            File destFile = new File(destFolder, System.currentTimeMillis() + "_" + attachment.getName());
            if (!attachment.renameTo(destFile)) {
                throw new IllegalArgumentException("Falha ao salvar o comprovante.");
            }

            Document doc = new Document(attachment.getName(), destFile.getAbsolutePath());

            Activity activity = new Activity(
                    name,
                    description,
                    date,
                    hours,
                    Status.PENDING,
                    "",
                    type,
                    student,
                    doc
            );

            return activityCatalog.addActivity(activity);

        } catch (IllegalArgumentException e) {
            // repassa erro para a tela
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao criar atividade: " + e.getMessage());
        }
    }

    // CE12 – excluir atividade (somente pendente)
    public boolean deleteActivity(Activity activity) {
        if (activity.getStatus() != Status.PENDING) {
            return false;
        }
        return activityCatalog.deleteActivity(activity);
    }

    // listar todas
    public List<Activity> getAll() {
        return activityCatalog.getAll();
    }

    // listar apenas as do aluno logado
    public List<Activity> getByStudent(Student student) {
        List<Activity> result = new ArrayList<>();
        if (student == null) return result;

        for (Activity a : activityCatalog.getAll()) {
            if (a.getStudent() != null &&
                a.getStudent().getEmail().equalsIgnoreCase(student.getEmail())) {
                result.add(a);
            }
        }
        return result;
    }
}
