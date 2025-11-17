package repository;

import model.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ActivityRepository {

    private final File file = new File("data/activities.txt");
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public ActivityRepository() {
        File folder = new File("data");
        if (!folder.exists()) folder.mkdir();
    }

    public List<Activity> loadAll() {
        List<Activity> list = new ArrayList<>();

        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {

                String[] p = line.split(";");

                // Formato:
                // 0 - studentEmail
                // 1 - name
                // 2 - description
                // 3 - date
                // 4 - hours
                // 5 - status
                // 6 - typeName
                // 7 - typeLimit
                // 8 - fileName
                // 9 - filePath
                if (p.length < 10) continue;

                String studentEmail = p[0];
                String name         = p[1];
                String desc         = p[2];
                Date   date         = sdf.parse(p[3]);
                int    hours        = Integer.parseInt(p[4]);
                Status status       = Status.valueOf(p[5]);
                String typeName     = p[6];
                int    typeLimit    = Integer.parseInt(p[7]);
                String fileName     = p[8];
                String filePath     = p[9];

                ActivityType type = new ActivityType(typeName, typeLimit);

                Student student = new Student();
                student.setEmail(studentEmail);

                Document doc = new Document(fileName, filePath);

                Activity a = new Activity(
                        name, desc, date, hours,
                        status, "",   // response vazio por enquanto
                        type, student, doc
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
                        a.getStudentEmail() + ";" +
                        a.getName() + ";" +
                        a.getDescription() + ";" +
                        sdf.format(a.getDate()) + ";" +
                        a.getHours() + ";" +
                        a.getStatus().name() + ";" +
                        a.getActivityType().getName() + ";" +
                        a.getActivityType().getLimit() + ";" +
                        a.getDocument().getFileName() + ";" +
                        a.getDocument().getFilePath()
                );
                bw.newLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
