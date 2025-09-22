package model;


public class ActivityType {
    private String name;
    private int limit;

    public ActivityType(String name, int limit) {
        this.name = name;
        this.limit = limit;
    }

    // Getters e Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getLimit() { return limit; }
    public void setLimit(int limit) { this.limit = limit; }
}
