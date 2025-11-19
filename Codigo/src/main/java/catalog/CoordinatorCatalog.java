package catalog;

import java.util.ArrayList;
import java.util.List;
import model.Coordinator;

public class CoordinatorCatalog {
    private List<Coordinator> coordinators;

    public CoordinatorCatalog() {
        this.coordinators = new ArrayList<>();
    }

    public CoordinatorCatalog(List<Coordinator> coordinators) {
        this.coordinators = coordinators;
    }

    public boolean addCoordinator(Coordinator coordinator) {
        if (coordinator != null) {
            coordinators.add(coordinator);
            return true;
        }
        return false;
    }

    public Coordinator findCoordinatorByEmail(String email) {
        for (Coordinator coordinator : coordinators) {
            if (coordinator.getEmail().equalsIgnoreCase(email)) {
                return coordinator;
            }
        }
        return null;
    }

    public List<Coordinator> getCoordinators() {
        return coordinators;
    }

    public void setCoordinators(List<Coordinator> coordinators) {
        this.coordinators = coordinators;
    }
}
