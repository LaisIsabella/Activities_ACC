package repository;

import model.Supervisor;
import java.io.*;
import java.util.*;

public class SupervisorRepository {
    private final File file = new File("data/supervisors.txt");

    public List<Supervisor> loadAll() {
        List<Supervisor> list = new ArrayList<>();

        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");
                if (p.length < 4) continue;

                Supervisor s = new Supervisor(p[0], p[1], p[2], p[3]);
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void saveAll(List<Supervisor> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Supervisor s : list) {
                bw.write(s.getName() + ";" + s.getEmail() + ";" + s.getPassword() + ";" + s.getCpf());
                bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
