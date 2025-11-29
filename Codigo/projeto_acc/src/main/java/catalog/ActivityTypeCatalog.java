/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package catalog;

import java.util.List;
import model.ActivityType;
import repository.ActivityTypeRepository;

/**
 *
 * @author Laís Isabella
 */
public class ActivityTypeCatalog {

    private final ActivityTypeRepository repo;
    private List<ActivityType> types;

    public ActivityTypeCatalog() {
        this.repo = new ActivityTypeRepository();
        this.types = repo.loadAll();
    }

    public List<ActivityType> getAll() {
        return types;
    }

    public ActivityType findByName(String name) {
        return types.stream()
                .filter(t -> t.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void updateLimit(String name, int limit) {
        repo.updateLimit(name, limit);
        types = repo.loadAll();
    }
}
