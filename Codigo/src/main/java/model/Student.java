package model;

public class Student extends User {
    private String cpf;
    private int ra;

    public Student() {
    }

    public Student(String name, String email, String password, String cpf, int ra) {
        super(name, email, password);
        this.cpf = cpf;
        this.ra = ra;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public int getRa() {
        return ra;
    }

    public void setRa(int ra) {
        this.ra = ra;
    }
}
