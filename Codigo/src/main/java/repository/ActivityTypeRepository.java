package repository;

import model.ActivityType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityTypeRepository {

    private final File file = new File("data/activity_types.txt");

    public List<ActivityType> loadAll() {
        List<ActivityType> list = new ArrayList<>();

        if (!file.exists()) {
            // cria tipos padrão
            list.add(new ActivityType("Pesquisa", 120));
            list.add(new ActivityType("Extensão", 120));
            list.add(new ActivityType("Ensino", 120));
            saveAll(list);
            return list;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                list.add(new ActivityType(p[0], Integer.parseInt(p[1])));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void saveAll(List<ActivityType> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (ActivityType t : list) {
                bw.write(t.getName() + ";" + t.getLimit());
                bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean updateLimit(String typeName, int limit) {
        List<ActivityType> all = loadAll();

        for (ActivityType t : all) {
            if (t.getName().equals(typeName)) {
                t.setLimit(limit);
            }
        }

        saveAll(all);
        return true;
    }
}
