package model;

public class ActivityType {

    private String name;
    private int limit;

    public ActivityType(String name, int limit) {
        this.name = name;
        this.limit = limit;
    }

    public String getName() {
        return name;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return name + " (limite: " + limit + ")";
    }
}
