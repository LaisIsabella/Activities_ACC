package catalog;

import model.Coordinator;
import repository.CoordinatorRepository;

import java.util.ArrayList;
import java.util.List;

public class CoordinatorCatalog {

    private final CoordinatorRepository repo;
    private final List<Coordinator> coordinators;

    public CoordinatorCatalog() {
        this.repo = new CoordinatorRepository();
        this.coordinators = repo.loadAll(); // CARREGA DO ARQUIVO
    }

    public boolean addCoordinator(Coordinator c) {
        boolean added = coordinators.add(c);
        if (added) {
            repo.saveAll(coordinators); // SALVA AUTOMATICAMENTE
        }
        return added;
    }

    // CE03 - Busca coordenador por e-mail
    public Coordinator findCoordinatorByEmail(String email) {
        return coordinators.stream()
                .filter(c -> c.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public List<Coordinator> getAll() {
        return new ArrayList<>(coordinators);
    }
}
