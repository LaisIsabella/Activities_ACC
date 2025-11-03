package model;

import java.util.Date;

public class Activity {
    private String name;
    private String description;
    private Date date;
    private int hours;
    private String status;
    private String response;

    private ActivityType type; // Associação com ActivityType

    public Activity() {
    }

    public Activity(String name, String description, Date date, int hours, 
                    String status, String response, ActivityType type) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.hours = hours;
        this.status = status;
        this.response = response;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ActivityType getType() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }
}
