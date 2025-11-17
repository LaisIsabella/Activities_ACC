package repository;

import model.Student;
import java.io.*;
import java.util.*;

public class StudentRepository {

    private final File file = new File("data/students.txt");

    public StudentRepository() {
        File folder = new File("data");
        if (!folder.exists()) folder.mkdir();
    }

    public List<Student> loadAll() {
        List<Student> list = new ArrayList<>();

        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(";");

                if (p.length < 5) continue;

                list.add(new Student(
                        p[0],  // name
                        p[1],  // email
                        p[2],  // password
                        p[3],  // cpf
                        Integer.parseInt(p[4]) // ra
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void saveAll(List<Student> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {

            for (Student s : list) {
                bw.write(
                        s.getName() + ";" +
                        s.getEmail() + ";" +
                        s.getPassword() + ";" +
                        s.getCpf() + ";" +
                        s.getRa()
                );
                bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
