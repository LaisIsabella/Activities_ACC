package model;

public class Student extends User {
    private String cpf;
    private String ra;
    private int totalHours;

    public Student() {
    }

    public Student(String name, String email, String password, String cpf, String ra) {
        super(name, email, password);
        this.cpf = cpf;
        this.ra = ra;
        this.totalHours = 0;
    }
    
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }
    
    public int getTotalHours(){
        return totalHours;
    }
    
    public void setTotalHours(int totalHours){
        this.totalHours = totalHours;
    }
}