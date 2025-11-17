package model;

public class Coordinator extends User {
    private int rc;

    public Coordinator() {
    }

    public Coordinator(String name, String email, String password, int rc) {
        super(name, email, password);
        this.rc = rc;
    }

    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }
}
