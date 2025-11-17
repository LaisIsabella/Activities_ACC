package repository;

import model.Coordinator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CoordinatorRepository {

    private final File file = new File("data/coordinators.txt");

    public CoordinatorRepository() {
        File folder = new File("data");
        if (!folder.exists()) folder.mkdir();
    }

    // ============================
    // LOAD
    // ============================
    public List<Coordinator> loadAll() {
        List<Coordinator> list = new ArrayList<>();

        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {

                String[] p = line.split(";");

                // Formato esperado:
                // 0 - nome
                // 1 - email
                // 2 - senha
                // 3 - rc (Registro do Coordenador)

                if (p.length < 4) continue;

                Coordinator coord = new Coordinator(
                        p[0],       // name
                        p[1],       // email
                        p[2],       // password
                        Integer.parseInt(p[3]) // rc
                );

                list.add(coord);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ============================
    // SAVE
    // ============================
    public void saveAll(List<Coordinator> list) {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            for (Coordinator c : list) {
                bw.write(
                        c.getName() + ";" +
                        c.getEmail() + ";" +
                        c.getPassword() + ";" +
                        c.getRc()
                );
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
