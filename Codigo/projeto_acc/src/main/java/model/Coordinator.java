package model;

public class Coordinator extends User {
    private String rc;

    public Coordinator() {
    }

    public Coordinator(String name, String email, String password, String rc) {
        super(name, email, password);
        this.rc = rc;
    }

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }
}
