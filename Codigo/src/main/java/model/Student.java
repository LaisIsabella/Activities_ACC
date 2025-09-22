package model;

public class Student extends User {
    private int cpf;
    private int ra;

    public Student(String name, String email, String password, int cpf, int ra) {
        super(name, email, password);
        this.cpf = cpf;
        this.ra = ra;
    }

    // Getters e Setters
    public int getCpf() { return cpf; }
    public void setCpf(int cpf) { this.cpf = cpf; }

    public int getRa() { return ra; }
    public void setRa(int ra) { this.ra = ra; }
}