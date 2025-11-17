package model;

public class Supervisor extends User {
    private String cpf;

    public Supervisor() {
    }

    public Supervisor(String name, String email, String password, String cpf) {
        super(name, email, password);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
