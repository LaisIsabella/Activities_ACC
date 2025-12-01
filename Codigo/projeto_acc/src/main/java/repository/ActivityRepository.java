package repository;

import catalog.StudentCatalog;
import model.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ActivityRepository {

    private final File file = new File("data/activities.txt");
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public ActivityRepository() {
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

  public List<Activity> loadAll(StudentCatalog studentCatalog, ActivityTypeRepository typeRepo) {
    List<Activity> list = new ArrayList<>();

    if (!file.exists()) return list;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {

        String line;
        while ((line = br.readLine()) != null) {

            String[] p = line.split(";");

            if (p.length < 10) continue;

            String studentEmail = p[0];
            Student st = studentCatalog.findStudentByEmail(studentEmail);

            String name = p[1];
            String desc = p[2];
            Date date = sdf.parse(p[3]);
            int hours = Integer.parseInt(p[4]);
            Status status = Status.valueOf(p[5]);
            String response = p[6];
            String typeName = p[7];

            // Buscar tipo
            ActivityType type = null;
            for (ActivityType t : typeRepo.loadAll()) {
                if (t.getName().equals(typeName)) {
                    type = t;
                    break;
                }
            }

            String docName = p[8];
            String docPath = p[9];
            Document doc = new Document(docName, docPath);

            boolean isVerified = false;
            if (p.length >= 11) {
                isVerified = Boolean.parseBoolean(p[10]);
            }

            Activity a = new Activity(
                    name, desc, date, hours,
                    status, response,
                    type, st, doc,
                    isVerified
            );

            list.add(a);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}


    public void saveAll(List<Activity> list) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            for (Activity a : list) {

                bw.write(
                        a.getStudent().getEmail() + ";"
                        + a.getName() + ";"
                        + a.getDescription() + ";"
                        + sdf.format(a.getDate()) + ";"
                        + a.getHours() + ";"
                        + a.getStatus() + ";"
                        + (a.getResponse() == null ? "" : a.getResponse()) + ";"
                        + a.getActivityType().getName() + ";"
                        + a.getDocument().getFileName() + ";"
                        + a.getDocument().getFilePath() + ";"
                        + a.getIsVerified()
                );

                bw.newLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
