package catalog;

import java.util.ArrayList;
import java.util.List;
import model.Supervisor;

public class SupervisorCatalog {
    private List<Supervisor> supervisors;

    public SupervisorCatalog() {
        this.supervisors = new ArrayList<>();
    }

    public SupervisorCatalog(List<Supervisor> supervisors) {
        this.supervisors = supervisors;
    }

    public boolean addSupervisor(Supervisor supervisor) {
        if (supervisor != null) {
            supervisors.add(supervisor);
            return true;
        }
        return false;
    }

    public Supervisor findSupervisorByEmail(String email) {
        for (Supervisor supervisor : supervisors) {
            if (supervisor.getEmail().equalsIgnoreCase(email)) {
                return supervisor;
            }
        }
        return null;
    }

    public List<Supervisor> getSupervisors() {
        return supervisors;
    }

    public void setSupervisors(List<Supervisor> supervisors) {
        this.supervisors = supervisors;
    }
}
