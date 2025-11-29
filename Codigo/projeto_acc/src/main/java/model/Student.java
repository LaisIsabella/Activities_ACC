package model;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private String cpf;
    private String ra;
    private int totalHours;
    private List<String> messages = new ArrayList<>();

    public Student() {
    }

    public Student(String name, String email, String password, String cpf, String ra) {
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
    
    public void addMessage(String msg) {
        if (msg != null) messages.add(msg);
    }

    public List<String> getMessages() {
        return messages;
    }
    
}
