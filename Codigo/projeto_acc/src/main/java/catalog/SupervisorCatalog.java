package catalog;

import java.util.List;
import model.Supervisor;
import repository.SupervisorRepository;

public class SupervisorCatalog {

    private List<Supervisor> supervisors;
    private SupervisorRepository repo;

    public SupervisorCatalog() {
        this.repo = new SupervisorRepository();
        this.supervisors = repo.loadAll();
    }

    public SupervisorCatalog(List<Supervisor> supervisors) {
        this.supervisors = supervisors;
    }

    public boolean addSupervisor(Supervisor supervisor) {
        if (supervisor == null) {
            return false;
        }
        supervisors.add(supervisor);
        repo.saveAll(supervisors);
        return true;
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
